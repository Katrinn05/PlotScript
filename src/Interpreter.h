#ifndef PLOTSCRIPT_INTERPRETER_H
#define PLOTSCRIPT_INTERPRETER_H

#include "AST.h"
#include "Plotter.h"
#include "Environment.h"
#include <vector>
#include <string>
#include <stdexcept>

// Interpreter for executing the AST using Environment
class Interpreter {
public:
    Interpreter() = default;

    // execute the program AST
    void interpret(const Program* program);

    // evaluate an expression AST and return numeric values
    std::vector<double> evaluate(const Expr* expr);

private:
    Environment env_;

    // helpers for concrete expression types
    std::vector<double> evalNumber(const NumberExpr* expr);
    std::vector<double> evalList(const ListExpr* expr);
    std::vector<double> evalIdentifier(const IdentifierExpr* expr);
    std::vector<double> evalCppFunction(const CppFuncExpr* expr);
    std::vector<double> evalArrange(const ArrangeExpr* expr);
    std::vector<double> evalInput(const InputExpr* expr);
    double evalScalar(const Expr* expr);

};

#endif
