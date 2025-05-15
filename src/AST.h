#ifndef PLOTSCRIPT_AST_H
#define PLOTSCRIPT_AST_H

#include <string>
#include <vector>
#include <iostream>

// Base class for all AST nodes
struct ASTNode {
    virtual ~ASTNode() = default;
};

// Base class for expressions
struct Expr : ASTNode { };

// Numeric literal
struct NumberExpr : Expr {
    double value;
    NumberExpr(double val) : value(val) {}
};

// List literal
struct ListExpr : Expr {
    std::vector<Expr*> elements;
    ListExpr(const std::vector<Expr*>& elems) : elements(elems) {}
    ~ListExpr() {
        for (Expr* e : elements) delete e;
    }
};

// Identifier (variable reference)
struct IdentifierExpr : Expr {
    std::string name;
    IdentifierExpr(const std::string& n) : name(n) {}
};

// Assignment statement: varName : expression
struct AssignmentStmt : ASTNode {
    std::string varName;
    Expr* valueExpr;
    AssignmentStmt(const std::string& name, Expr* expr)
        : varName(name), valueExpr(expr) {}
    ~AssignmentStmt() { delete valueExpr; }
};

// Plot command: plot axis1Expr, axis2Expr -> outputFile
struct PlotCommandStmt : ASTNode {
    Expr* axis1Expr;
    Expr* axis2Expr;
    std::string outputFile;
    PlotCommandStmt(Expr* a1, Expr* a2, const std::string& out)
        : axis1Expr(a1), axis2Expr(a2), outputFile(out) {}
    ~PlotCommandStmt() {
        delete axis1Expr;
        delete axis2Expr;
    }
};

// Program: list of statements
struct Program : ASTNode {
    std::vector<ASTNode*> statements;
    ~Program() {
        for (ASTNode* stmt : statements) delete stmt;
    }
};

#endif
