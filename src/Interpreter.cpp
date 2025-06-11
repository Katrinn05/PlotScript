#include "Interpreter.h"
#include "Plotter.h"
#include "Errors.h" 
#include <fstream>
#include <cmath>
#include <cstdlib>
#include <string>
#include <vector>
#include <utility>
#include <windows.h>

void Interpreter::interpret(const Program* program) {
    if (!program) return;
    bool seenExport = false;
    for (const auto* node : program->statements) {
        if (auto assign = dynamic_cast<const AssignmentStmt*>(node)) {
            auto values = evaluate(assign->valueExpr);
            env_.set(assign->varName, values);
        }
        else if (auto plot = dynamic_cast<const PlotCommandStmt*>(node)) {
            const std::string& nm = plot->name;
            if (plotDefs_.count(nm)) {
                throw SemanticError("Duplicate plot name: " + nm);
            }
            plotDefs_[nm] = plot;
        }
        else if (auto expStmt = dynamic_cast<const ExportStmt*>(node)) {
            seenExport = true;
            for (const std::string& plotName : expStmt->plotNames) {
                auto it = plotDefs_.find(plotName);
                if (it == plotDefs_.end()) {
                    throw SemanticError("Export: unknown plot name \"" + plotName + "\"");
                }
                const PlotCommandStmt* plotDef = it->second;

                auto x = evaluate(plotDef->axis1Expr);
                env_.set("__last_x__", x);
                auto y = evaluate(plotDef->axis2Expr);
                if (x.size() != y.size()) {
                    throw RuntimeError("axis1 and axis2 size mismatch");
                }

                double xScale = 1.0, yScale = 1.0;
                if (plotDef->axis1ScaleExpr) {
                    xScale = evalScalar(plotDef->axis1ScaleExpr);
                    if (xScale <= 0) throw SemanticError("axis1-scale must be positive");
                }
                if (plotDef->axis2ScaleExpr) {
                    yScale = evalScalar(plotDef->axis2ScaleExpr);
                    if (yScale <= 0) throw SemanticError("axis2-scale must be positive");
                }

                std::vector<std::pair<double,double>> pts;
                pts.reserve(x.size());
                for (size_t i = 0; i < x.size(); ++i) {
                    pts.emplace_back(x[i] * xScale, y[i] * yScale);
                }

                Color customColor = {0, 0, 0};
                bool hasCustomColor = false;
                if (plotDef->colorExpr) {
                    auto colVals = evaluate(plotDef->colorExpr);
                    if (colVals.size() != 3) {
                        throw SemanticError("color: expected 3 components for RGB color");
                    }
                    for (double v : colVals) {
                        if (v < 0 || v > 255) {
                            throw SemanticError("color: components must be in range [0, 255]");
                        }
                    }
                    customColor.r = static_cast<unsigned char>(std::lround(colVals[0]));
                    customColor.g = static_cast<unsigned char>(std::lround(colVals[1]));
                    customColor.b = static_cast<unsigned char>(std::lround(colVals[2]));
                    hasCustomColor = true;
                }

                Plotter plt(800, 600);
                plt.addSeries(pts, true);
                if (hasCustomColor) plt.overrideLastSeriesColor(customColor);

                plt.save(plotDef->outputFile);
            }
        }
        else {
            throw LogicError("Unknown AST node in interpret");
        }
    }

    if (!seenExport) {
        for (const auto& kv : plotDefs_) {
            const PlotCommandStmt* plotDef = kv.second;

            auto x = evaluate(plotDef->axis1Expr);
            env_.set("__last_x__", x);
            auto y = evaluate(plotDef->axis2Expr);
            if (x.size() != y.size()) {
                throw RuntimeError("axis1 and axis2 size mismatch for a plot");
            }

            double xScale = 1.0, yScale = 1.0;
            if (plotDef->axis1ScaleExpr) {
                xScale = evalScalar(plotDef->axis1ScaleExpr);
                if (xScale <= 0) throw SemanticError("axis1-scale must be positive");
            }
            if (plotDef->axis2ScaleExpr) {
                yScale = evalScalar(plotDef->axis2ScaleExpr);
                if (yScale <= 0) throw SemanticError("axis2-scale must be positive");
            }

            std::vector<std::pair<double,double>> pts;
            pts.reserve(x.size());
            for (size_t i = 0; i < x.size(); ++i) {
                pts.emplace_back(x[i] * xScale, y[i] * yScale);
            }

            Color customColor = {0, 0, 0};
            bool hasCustomColor = false;
            if (plotDef->colorExpr) {
                auto colVals = evaluate(plotDef->colorExpr);
                if (colVals.size() != 3) {
                    throw SemanticError("color: expected 3 components for RGB color");
                }
                for (double v : colVals) {
                    if (v < 0 || v > 255) {
                        throw SemanticError("color: components must be in range [0, 255]");
                    }
                }
                customColor.r = static_cast<unsigned char>(std::lround(colVals[0]));
                customColor.g = static_cast<unsigned char>(std::lround(colVals[1]));
                customColor.b = static_cast<unsigned char>(std::lround(colVals[2]));
                hasCustomColor = true;
            }

            Plotter plt(800, 600);
            plt.addSeries(pts, true);
            if (hasCustomColor) {
                plt.overrideLastSeriesColor(customColor);
            }

            plt.save(plotDef->outputFile);
        }
    }
}

std::vector<double> Interpreter::evaluate(const Expr* expr) {
    if (!expr) return {};
    if (auto num = dynamic_cast<const NumberExpr*>(expr)) return evalNumber(num);
    if (auto list = dynamic_cast<const ListExpr*>(expr)) return evalList(list);
    if (auto id = dynamic_cast<const IdentifierExpr*>(expr)) return evalIdentifier(id);
    if (auto arr = dynamic_cast<const ArrangeExpr*>(expr)) return evalArrange(arr);
    if (auto inp = dynamic_cast<const InputExpr*>(expr)) return evalInput(inp);
    if (auto cppf = dynamic_cast<const CppFuncExpr*>(expr)) return evalCppFunction(cppf);
    throw LogicError("Unsupported expression type");
}

std::vector<double> Interpreter::evalArrange(const ArrangeExpr* expr) {
    double f = expr->first;
    double l = expr->last;
    double s = expr->hasStep ? expr->step : 1.0;

    if (s <= 0) {
        throw SemanticError("arrange: step must be positive");
    }
    std::vector<double> out;
    if (f <= l) {
        for (double x = f; x <= l; x += s) {
            out.push_back(x);
        }
    } else {
        for (double x = f; x >= l; x -= s) {
            out.push_back(x);
        }
    }
    return out;
}

std::vector<double> Interpreter::evalNumber(const NumberExpr* expr) {
    return { expr->value };
}

std::vector<double> Interpreter::evalList(const ListExpr* expr) {
    std::vector<double> out;
    for (auto* e : expr->elements) {
        auto part = evaluate(e);
        out.insert(out.end(), part.begin(), part.end());
    }
    return out;
}

std::vector<double> Interpreter::evalIdentifier(const IdentifierExpr* expr) {
    return env_.get(expr->name);
}

std::vector<double> Interpreter::evalCppFunction(const CppFuncExpr* expr) {
    auto xValues = env_.get("__last_x__");
    size_t N = xValues.size();

    std::string tmpCpp, tmpExe, tmpOut;
#ifdef _WIN32
    const char* tempEnv = std::getenv("TEMP");
    if (tempEnv && tempEnv[0] != '\0') {
        tmpCpp = std::string(tempEnv) + "\\plotscript_tmp.cpp";
        tmpExe = std::string(tempEnv) + "\\plotscript_tmp.exe";
        tmpOut = std::string(tempEnv) + "\\plotscript_tmp_out.txt";
    } else {
        tmpCpp = "plotscript_tmp.cpp";
        tmpExe = "plotscript_tmp.exe";
        tmpOut = "plotscript_tmp_out.txt";
    }
#else
    tmpCpp = "/tmp/plotscript_tmp.cpp";
    tmpExe = "/tmp/plotscript_tmp_exec";
    tmpOut = "/tmp/plotscript_tmp_out.txt";
#endif

    std::ofstream ofs(tmpCpp);
    if (!ofs) {
        throw IOError("Failed to open temp C++ file for writing: " + tmpCpp);
    }
    ofs << "#include <iostream>\n";
    ofs << "#include <cmath>\n";
    ofs << "using namespace std;\n\n";
    ofs << expr->rawCode << "\n\n";
    ofs << "int main() {\n";
    ofs << "    const int N = " << N << ";\n";
    ofs << "    double xs[N] = {";
    for (size_t i = 0; i < N; ++i) {
        ofs << xValues[i];
        if (i + 1 < N) ofs << ", ";
    }
    ofs << "};\n";
    ofs << "    for (int i = 0; i < N; ++i) {\n";
    ofs << "        double y = f(xs[i]);\n";
    ofs << "        cout << y;\n";
    ofs << "        if (i + 1 < N) cout << '\\n';\n";
    ofs << "    }\n";
    ofs << "    return 0;\n";
    ofs << "}\n";
    ofs.close();

    int ret;
#ifdef _WIN32
    char shortExe[MAX_PATH];
    if (GetShortPathNameA(tmpExe.c_str(), shortExe, MAX_PATH) == 0) {
        strcpy_s(shortExe, tmpExe.c_str());
    }

    std::string compileCmd =
        "g++ -O2 -std=c++17 \"" + tmpCpp + "\" -o \"" + std::string(shortExe) + "\"";
    ret = system(compileCmd.c_str());
    if (ret != 0) {
        throw IOError("C++ function compilation failed: " + tmpCpp);
    }

    std::string runCmd = std::string(shortExe) + " > \"" + tmpOut + "\"";
    ret = system(runCmd.c_str());
    if (ret != 0) {
        throw IOError("C++ function execution failed: " + std::string(shortExe));
    }

#else
    std::string compileCmd = "g++ -O2 -std=c++17 " + tmpCpp + " -o " + tmpExe;
    ret = system(compileCmd.c_str());
    if (ret != 0) {
        throw IOError("C++ function compilation failed: " + tmpCpp);
    }

    std::string runCmd = tmpExe + " > " + tmpOut;
    ret = system(runCmd.c_str());
    if (ret != 0) {
        throw IOError("C++ function execution failed: " + tmpExe);
    }
#endif 

    std::ifstream ifs(tmpOut);
    if (!ifs) {
        throw IOError("Failed to open C++ output file: " + tmpOut);
    }
    std::vector<double> yValues;
    double y;
    while (ifs >> y) {
        yValues.push_back(y);
    }
    ifs.close();

    std::remove(tmpCpp.c_str());
    std::remove(tmpExe.c_str());
    std::remove(tmpOut.c_str());

    return yValues;
}

double Interpreter::evalScalar(const Expr* expr) {
    auto vec = evaluate(expr);
    if (vec.size() != 1) {
        throw RuntimeError("scale expression must evaluate to a single numeric value");
    }
    return vec[0];
}

std::vector<double> Interpreter::evalInput(const InputExpr* expr) {
    const std::string& fname = expr->filename;
    std::ifstream ifs(fname);
    if (!ifs) {
        throw IOError("input: cannot open file \"" + fname + "\"");
    }
    std::vector<double> data;
    double value;
    while (ifs >> value) {
        data.push_back(value);
    }
    if (ifs.bad()) {
        throw IOError("input: error while reading file \"" + fname + "\"");
    }
    return data;
}
