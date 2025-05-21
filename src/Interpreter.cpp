#include "Interpreter.h"
#include <stdexcept>

// run all statements in the program
void Interpreter::interpret(const Program* program) {
    if (!program) return;
    for (const auto* node : program->statements) {
        if (auto assign = dynamic_cast<const AssignmentStmt*>(node)) {
            auto values = evaluate(assign->valueExpr);
            env_.set(assign->varName, values);
        } else if (auto plot = dynamic_cast<const PlotCommandStmt*>(node)) {
            auto x = evaluate(plot->axis1Expr);
            auto y = evaluate(plot->axis2Expr);
            if (x.size() != y.size()) {
                throw std::runtime_error("axis1 and axis2 size mismatch");
            }
            // Plotter::drawPlot(x, y, plot->outputFile);
        } else {
            throw std::runtime_error("Unknown AST node in interpret");
        }
    }
}

// dispatch evaluation to specific handlers
std::vector<double> Interpreter::evaluate(const Expr* expr) {
    if (!expr) return {};
    if (auto num = dynamic_cast<const NumberExpr*>(expr)) return evalNumber(num);
    if (auto list = dynamic_cast<const ListExpr*>(expr)) return evalList(list);
    if (auto id = dynamic_cast<const IdentifierExpr*>(expr)) return evalIdentifier(id);
    throw std::invalid_argument("Unsupported expression type");
}

// return single value as array
std::vector<double> Interpreter::evalNumber(const NumberExpr* expr) {
    return { expr->value };
}

// flatten list elements into numeric array
std::vector<double> Interpreter::evalList(const ListExpr* expr) {
    std::vector<double> out;
    for (auto* e : expr->elements) {
        auto part = evaluate(e);
        out.insert(out.end(), part.begin(), part.end());
    }
    return out;
}

// lookup variable values via environment
std::vector<double> Interpreter::evalIdentifier(const IdentifierExpr* expr) {
    return env_.get(expr->name);
}