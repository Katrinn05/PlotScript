#include "ASTBuilderVisitor.h"
#include "AST.h"
#include "PlotScriptParser.h"

using namespace antlrcpp;
using namespace std;

std::any ASTBuilderVisitor::visitProgram(PlotScriptParser::ProgramContext *ctx) {
    auto *program = new Program();
    // For each top-level plot definition, build a PlotCommandStmt and add to program
    for (auto *pd : ctx->plotDefinition()) {
        auto *plotCmd = std::any_cast<PlotCommandStmt*>( visit(pd) );
        program->statements.push_back(plotCmd);
    }
    // (Optionally handle exportStatement here)
    return program;
}

std::any ASTBuilderVisitor::visitPlotDefinition(PlotScriptParser::PlotDefinitionContext *ctx) {
    Expr *axis1 = nullptr;
    Expr *axis2 = nullptr;
    string outputFile;

    // Process each statement in the plot block
    auto *block = ctx->plotBlock();
    for (auto *stmtCtx : block->plotStatement()) {
        auto *assign = std::any_cast<AssignmentStmt*>( visit(stmtCtx) );
        Expr* val = assign->valueExpr;
        assign->valueExpr = nullptr;

        const auto& name = assign->varName;
        if (name == "axis1") {
            axis1 = val;
        }
        else if (name == "axis2") {
            axis2 = val;
        }  
        else if (name == "output") {
            if (auto* id = dynamic_cast<IdentifierExpr*>(val)) {
                outputFile = id->name;
            }
            delete val;                
        }
        else {
            delete val;                
        }

        delete assign;
    }

    return new PlotCommandStmt(axis1, axis2, outputFile);
}

std::any ASTBuilderVisitor::visitPlotName(PlotScriptParser::PlotNameContext *ctx) {
    return ctx->ID()->getText();
}

std::any ASTBuilderVisitor::visitPlotBlock(PlotScriptParser::PlotBlockContext *ctx) {
    std::vector<std::any> stmts;
    for (auto *stmtCtx : ctx->plotStatement()) {
        stmts.push_back( visit(stmtCtx) );
    }
    return stmts;
}

std::any ASTBuilderVisitor::visitPlotStatement(PlotScriptParser::PlotStatementContext *ctx) {
    // varName: expression;
    string varName = std::any_cast<string>( visit(ctx->plotFunctionIdentifier()) );
    Expr *expr = std::any_cast<Expr*>( visit(ctx->expression()) );
    return new AssignmentStmt(varName, expr);
}

std::any ASTBuilderVisitor::visitPlotFunctionIdentifier(PlotScriptParser::PlotFunctionIdentifierContext *ctx) {
    if (ctx->AXIS1())        return ctx->AXIS1()->getText();
    if (ctx->AXIS2())        return ctx->AXIS2()->getText();
    if (ctx->COLOR())        return ctx->COLOR()->getText();
    if (ctx->OUTPUT())       return ctx->OUTPUT()->getText();
    if (ctx->AXIS1_SCALE())  return ctx->AXIS1_SCALE()->getText();
    if (ctx->AXIS2_SCALE())  return ctx->AXIS2_SCALE()->getText();
    return string{};
}

std::any ASTBuilderVisitor::visitExpression(PlotScriptParser::ExpressionContext *ctx) {
    if (ctx->value()) {
        return visit(ctx->value());
    }
    if (ctx->list()) {
        return visit(ctx->list());
    }
    if (ctx->functionCall()) {
        // built-in or embedded functions: not yet represented in AST.h
        return visitChildren(ctx);
    }
    return std::any{ static_cast<Expr*>(nullptr) };
}

std::any ASTBuilderVisitor::visitValue(PlotScriptParser::ValueContext *ctx) {
    if (ctx->NUMBER()) {
        double v = std::stod(ctx->NUMBER()->getText());
        return static_cast<Expr*>( new NumberExpr(v) );
    }
    if (ctx->STRING()) {
        // strip surrounding single quotes
        auto txt = ctx->STRING()->getText();
        if (txt.size() >= 2 && txt.front()=='\'' && txt.back()=='\'') {
            txt = txt.substr(1, txt.size()-2);
        }
        return static_cast<Expr*>( new IdentifierExpr(txt) );
    }
    if (ctx->ID()) {
        return static_cast<Expr*>( new IdentifierExpr(ctx->ID()->getText()) );
    }
    return std::any{ static_cast<Expr*>(nullptr) };
}

std::any ASTBuilderVisitor::visitList(PlotScriptParser::ListContext *ctx) {
    vector<Expr*> elems;
    for (auto *valCtx : ctx->value()) {
        elems.push_back( std::any_cast<Expr*>( visit(valCtx) ) );
    }
    return static_cast<Expr*>( new ListExpr(elems) );
}

// The following visitors are not yet mapped to concrete AST nodes in AST.h.
// They delegate to the base implementation.

std::any ASTBuilderVisitor::visitFunctionCall(PlotScriptParser::FunctionCallContext *ctx) {
    return visitChildren(ctx);
}

std::any ASTBuilderVisitor::visitStringLikeFunction(PlotScriptParser::StringLikeFunctionContext *ctx) {
    return visitChildren(ctx);
}

std::any ASTBuilderVisitor::visitStringLikeFunctionParam(PlotScriptParser::StringLikeFunctionParamContext *ctx) {
    return visitChildren(ctx);
}

std::any ASTBuilderVisitor::visitRangeArgs(PlotScriptParser::RangeArgsContext *ctx) {
    return visitChildren(ctx);
}

std::any ASTBuilderVisitor::visitEmbeddedFunctionBlock(PlotScriptParser::EmbeddedFunctionBlockContext *ctx) {
    return visitChildren(ctx);
}

std::any ASTBuilderVisitor::visitExportStatement(PlotScriptParser::ExportStatementContext *ctx) {
    return visitChildren(ctx);
}

// C++-embedded code visitors remain unimplemented
std::any ASTBuilderVisitor::visitCpp_funcDeclaration(PlotScriptParser::Cpp_funcDeclarationContext *ctx)        { return visitChildren(ctx); }
std::any ASTBuilderVisitor::visitCpp_funcReturnType(PlotScriptParser::Cpp_funcReturnTypeContext *ctx)          { return visitChildren(ctx); }
std::any ASTBuilderVisitor::visitCpp_typeSpecifier(PlotScriptParser::Cpp_typeSpecifierContext *ctx)            { return visitChildren(ctx); }
std::any ASTBuilderVisitor::visitCpp_paramList(PlotScriptParser::Cpp_paramListContext *ctx)                   { return visitChildren(ctx); }
std::any ASTBuilderVisitor::visitCpp_parameter(PlotScriptParser::Cpp_parameterContext *ctx)                   { return visitChildren(ctx); }
std::any ASTBuilderVisitor::visitCpp_funcBody(PlotScriptParser::Cpp_funcBodyContext *ctx)                     { return visitChildren(ctx); }
std::any ASTBuilderVisitor::visitCpp_funcStatement(PlotScriptParser::Cpp_funcStatementContext *ctx)           { return visitChildren(ctx); }
std::any ASTBuilderVisitor::visitCpp_assignment(PlotScriptParser::Cpp_assignmentContext *ctx)                 { return visitChildren(ctx); }
std::any ASTBuilderVisitor::visitCpp_varDeclaration(PlotScriptParser::Cpp_varDeclarationContext *ctx)         { return visitChildren(ctx); }
std::any ASTBuilderVisitor::visitCpp_return(PlotScriptParser::Cpp_returnContext *ctx)                         { return visitChildren(ctx); }
std::any ASTBuilderVisitor::visitCpp_controlStructure(PlotScriptParser::Cpp_controlStructureContext *ctx)     { return visitChildren(ctx); }
std::any ASTBuilderVisitor::visitCpp_expr(PlotScriptParser::Cpp_exprContext *ctx)                             { return visitChildren(ctx); }
