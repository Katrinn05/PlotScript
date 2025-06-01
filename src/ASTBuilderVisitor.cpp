#include "ASTBuilderVisitor.h"
#include "AST.h"
#include "PlotScriptParser.h"

using namespace antlrcpp;
using namespace std;

std::any ASTBuilderVisitor::visitProgram(PlotScriptParser::ProgramContext *ctx) {
    auto *program = new Program();
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
    Expr *axis1ScaleExpr = nullptr;
    Expr *axis2ScaleExpr = nullptr; 
    string outputFile;

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
        else if (name == "axis1-scale" || name == "x-scale" || name == "X-scale") {
            axis1ScaleExpr = val;
        }
        else if (name == "axis2-scale" || name == "y-scale" || name == "Y-scale") {
            axis2ScaleExpr = val;
        }
        else {
            delete val;                
        }

        delete assign;
    }
    auto *node = new PlotCommandStmt(axis1, axis2, outputFile);
    node->axis1ScaleExpr = axis1ScaleExpr;
    node->axis2ScaleExpr = axis2ScaleExpr;
    return node;
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
    string varName = std::any_cast<string>( visit(ctx->plotFunctionIdentifier()) );
    Expr *expr = std::any_cast<Expr*>( visit(ctx->expression()) );
    return new AssignmentStmt(varName, expr);
}

std::any ASTBuilderVisitor::visitPlotFunctionIdentifier(PlotScriptParser::PlotFunctionIdentifierContext *ctx) {
    if (ctx->TOKEN_AXIS1())       return ctx->TOKEN_AXIS1()->getText();
    if (ctx->TOKEN_AXIS2())       return ctx->TOKEN_AXIS2()->getText();
    if (ctx->TOKEN_COLOR())       return ctx->TOKEN_COLOR()->getText();
    if (ctx->TOKEN_OUTPUT())      return ctx->TOKEN_OUTPUT()->getText();
    if (ctx->TOKEN_AXIS1_SCALE()) return ctx->TOKEN_AXIS1_SCALE()->getText();
    if (ctx->TOKEN_AXIS2_SCALE()) return ctx->TOKEN_AXIS2_SCALE()->getText();
    return std::string{};
}


std::any ASTBuilderVisitor::visitExpression(PlotScriptParser::ExpressionContext *ctx) {
    if (ctx->value()) {
        return visit(ctx->value());
    }
    if (ctx->list()) {
        return visit(ctx->list());
    }
    if (ctx->functionCall()) {
        return visit(ctx->functionCall());
    }
    return std::any{ static_cast<Expr*>(nullptr) };
}

std::any ASTBuilderVisitor::visitValue(PlotScriptParser::ValueContext *ctx) {
    if (ctx->NUMBER()) {
        double v = std::stod(ctx->NUMBER()->getText());
        return static_cast<Expr*>( new NumberExpr(v) );
    }
    if (ctx->STRING()) {
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

std::any ASTBuilderVisitor::visitFunctionCall(PlotScriptParser::FunctionCallContext* ctx) {
    if (ctx->getToken(PlotScriptParser::TOKEN_ARRANGE, 0)) {
        auto anyTup = visit(ctx->rangeArgs());
        auto tup = std::any_cast<std::tuple<double,double,double,bool>>(anyTup);
        double firstVal = std::get<0>(tup);
        double lastVal  = std::get<1>(tup);
        double stepVal  = std::get<2>(tup);
        bool   hasStep  = std::get<3>(tup);

        return static_cast<Expr*>( new ArrangeExpr(firstVal, lastVal, stepVal, hasStep) );
    }

    if (ctx->stringLikeFunction()) {
        auto anyParam = visit(ctx->stringLikeFunctionParam());
        std::string filename = std::any_cast<std::string>(anyParam);
        return static_cast<Expr*>( new InputExpr(filename) );
    }

    if (ctx->getToken(PlotScriptParser::TOKEN_FUNC, 0)) {
        return visit(ctx->embeddedFunctionBlock());
    }
    return visitChildren(ctx);
}

std::any ASTBuilderVisitor::visitStringLikeFunction(PlotScriptParser::StringLikeFunctionContext *ctx) {
    return visitChildren(ctx);
}

std::any ASTBuilderVisitor::visitStringLikeFunctionParam(PlotScriptParser::StringLikeFunctionParamContext *ctx) {
    std::string txt = ctx->STRING()->getText();
    if (txt.size() >= 2 && txt.front()=='\'' && txt.back()=='\'') {
        txt = txt.substr(1, txt.size() - 2);
    } 
    return txt;
}

std::any ASTBuilderVisitor::visitRangeArgs(PlotScriptParser::RangeArgsContext *ctx) {
    auto numNodes = ctx->getTokens(PlotScriptParser::NUMBER); 
    if (numNodes.size() < 2) {
        throw runtime_error("rangeArgs: expected at least two numeric arguments");
    }

    double firstVal = std::stod(numNodes[0]->getText());
    double lastVal  = std::stod(numNodes[1]->getText());

    double stepVal = 1.0;
    bool   hasStep = false;

    if (ctx->getToken(PlotScriptParser::TOKEN_STEP, 0)) {
        if (numNodes.size() < 3) {
            throw runtime_error("rangeArgs: STEP specified but no step value provided");
        }
        stepVal = std::stod(numNodes[2]->getText());
        hasStep = true;
    }

    return std::make_tuple(firstVal, lastVal, stepVal, hasStep);
}

std::any ASTBuilderVisitor::visitEmbeddedFunctionBlock(PlotScriptParser::EmbeddedFunctionBlockContext* ctx) {
    antlr4::Token* startToken = ctx->getStart();
    antlr4::Token*   stopToken = ctx->getStop();
    if (!startToken || !stopToken) {
        throw runtime_error("EmbeddedFunctionBlock: did not find start or stop token");
    }

    antlr4::CharStream* charStream =
        startToken->getTokenSource()->getInputStream();
    if (!charStream) {
        throw runtime_error("EmbeddedFunctionBlock: no input stream found");
    }

    antlr4::misc::Interval interval{
        startToken->getStartIndex(),
        stopToken->getStopIndex()
    };
    std::string fullText = charStream->getText(interval);
    const std::string prefix = "$CPP$";
    const std::string suffix = "$$";

    if (fullText.rfind(prefix, 0) != 0) {
        throw runtime_error("EmbeddedFunctionBlock: expected prefix \"$CPP$\"");
    }
    if (fullText.size() < prefix.size() + suffix.size()) {
        throw runtime_error("EmbeddedFunctionBlock: too short for embedded function block");
    }
    if (fullText.substr(fullText.size() - suffix.size()) != suffix) {
        throw runtime_error("EmbeddedFunctionBlock: expected suffix \"$$\"");
    }

    std::string rawCode = fullText.substr(
        prefix.size(),
        fullText.size() - prefix.size() - suffix.size()
    );
    auto* cppNode = new CppFuncExpr(rawCode);
    return static_cast<Expr*>(cppNode);
}

std::any ASTBuilderVisitor::visitExportStatement(PlotScriptParser::ExportStatementContext *ctx) {
    return visitChildren(ctx);
}

// C++-embedded code visitors remain unimplemented
// std::any ASTBuilderVisitor::visitCpp_funcDeclaration(PlotScriptParser::Cpp_funcDeclarationContext *ctx)        { return visitChildren(ctx); }
// std::any ASTBuilderVisitor::visitCpp_funcReturnType(PlotScriptParser::Cpp_funcReturnTypeContext *ctx)          { return visitChildren(ctx); }
// std::any ASTBuilderVisitor::visitCpp_typeSpecifier(PlotScriptParser::Cpp_typeSpecifierContext *ctx)            { return visitChildren(ctx); }
// std::any ASTBuilderVisitor::visitCpp_paramList(PlotScriptParser::Cpp_paramListContext *ctx)                   { return visitChildren(ctx); }
// std::any ASTBuilderVisitor::visitCpp_parameter(PlotScriptParser::Cpp_parameterContext *ctx)                   { return visitChildren(ctx); }
// std::any ASTBuilderVisitor::visitCpp_funcBody(PlotScriptParser::Cpp_funcBodyContext *ctx)                     { return visitChildren(ctx); }
// std::any ASTBuilderVisitor::visitCpp_funcStatement(PlotScriptParser::Cpp_funcStatementContext *ctx)           { return visitChildren(ctx); }
// std::any ASTBuilderVisitor::visitCpp_assignment(PlotScriptParser::Cpp_assignmentContext *ctx)                 { return visitChildren(ctx); }
// std::any ASTBuilderVisitor::visitCpp_varDeclaration(PlotScriptParser::Cpp_varDeclarationContext *ctx)         { return visitChildren(ctx); }
// std::any ASTBuilderVisitor::visitCpp_return(PlotScriptParser::Cpp_returnContext *ctx)                         { return visitChildren(ctx); }
// std::any ASTBuilderVisitor::visitCpp_controlStructure(PlotScriptParser::Cpp_controlStructureContext *ctx)     { return visitChildren(ctx); }
// std::any ASTBuilderVisitor::visitCpp_expr(PlotScriptParser::Cpp_exprContext *ctx)                             { return visitChildren(ctx); }
