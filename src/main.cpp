#include <iostream>
#include <fstream>
#include <memory>
#include <any>

#include "antlr4-runtime.h"
#include "PlotScriptLexer.h"
#include "PlotScriptParser.h"

#include "ASTBuilderVisitor.h"
#include "AST.h"

using namespace antlr4;
using namespace std;

static void printExpr(const Expr* expr) {
    if (auto num = dynamic_cast<const NumberExpr*>(expr)) {
        cout << num->value;
    }
    else if (auto id = dynamic_cast<const IdentifierExpr*>(expr)) {
        cout << id->name;
    }
    else if (auto list = dynamic_cast<const ListExpr*>(expr)) {
        cout << "[";
        for (size_t i = 0; i < list->elements.size(); ++i) {
            printExpr(list->elements[i]);
            if (i + 1 < list->elements.size()) cout << ", ";
        }
        cout << "]";
    }
    else {
        cout << "<unknown expr>";
    }
}

int main(int argc, const char* argv[]) {
    if (argc != 2) {
        cerr << "Usage: " << argv[0] << " <script.plot>\n";
        return 1;
    }

    // 1. Read input file
    ifstream stream(argv[1]);
    if (!stream) {
        cerr << "Failed to open file: " << argv[1] << "\n";
        return 1;
    }
    ANTLRInputStream input(stream);

    // 2. Lexer and token stream
    PlotScriptLexer lexer(&input);
    CommonTokenStream tokens(&lexer);

    // 3. Parser
    PlotScriptParser parser(&tokens);
    parser.removeErrorListeners();
    parser.addErrorListener(new ConsoleErrorListener());

    // 4. Parse the top-level rule
    PlotScriptParser::ProgramContext* tree = parser.program();

    // Check for syntax errors
    if (parser.getNumberOfSyntaxErrors() > 0) {
        cerr << "Parsing failed with syntax errors.\n";
        return 1;
    }

    // 5. Build AST with fine-grained error handling
    ASTBuilderVisitor builder;
    std::any rootAny;
    try {
        rootAny = builder.visit(tree);
    } catch (const std::bad_any_cast& e) {
        cerr << "std::bad_any_cast inside ASTBuilderVisitor: " << e.what() << "\n";
        return 1;
    } catch (const std::exception& e) {
        cerr << "Exception during AST construction: " << e.what() << "\n";
        return 1;
    }

    // 6. Cast to Program* and report if mismatch
    Program* program = nullptr;
    try {
        program = std::any_cast<Program*>(rootAny);
    } catch (const std::bad_any_cast& e) {
        cerr << "Unexpected type returned by visitor: " << rootAny.type().name() << "\n";
        return 1;
    }

    // 7. Simple AST dump
    cout << "Parsed " << program->statements.size() << " statement(s)\n";
    for (auto* stmt : program->statements) {
        if (auto* plot = dynamic_cast<PlotCommandStmt*>(stmt)) {
            cout << "Plot command:\n";
            cout << "  axis1 = "; printExpr(plot->axis1Expr); cout << "\n";
            cout << "  axis2 = "; printExpr(plot->axis2Expr); cout << "\n";
            cout << "  output = " << plot->outputFile << "\n";
        }
        else if (auto* assign = dynamic_cast<AssignmentStmt*>(stmt)) {
            cout << "Assignment: " << assign->varName << " = ";
            printExpr(assign->valueExpr);
            cout << "\n";
        }
        else {
            cout << "Unknown statement type\n";
        }
    }

    // 7. Cleanup
    delete program;

    return 0;
}


