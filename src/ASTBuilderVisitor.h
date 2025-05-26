#ifndef PLOTSCRIPT_ASTBUILDERVISITOR_H
#define PLOTSCRIPT_ASTBUILDERVISITOR_H

#include <any>
#include <algorithm>
#include <cctype>

#include "antlr4-runtime.h"
#include "PlotScriptParserBaseVisitor.h"
#include "PlotScriptParser.h"
#include "AST.h"

class ASTBuilderVisitor : public PlotScriptParserBaseVisitor {
public:
    ASTBuilderVisitor() = default;
    ~ASTBuilderVisitor() override = default;

    std::any visitProgram(PlotScriptParser::ProgramContext* ctx) override;
    std::any visitPlotDefinition(PlotScriptParser::PlotDefinitionContext* ctx) override;
    std::any visitPlotName(PlotScriptParser::PlotNameContext* ctx) override;
    std::any visitPlotBlock(PlotScriptParser::PlotBlockContext* ctx) override;
    std::any visitPlotStatement(PlotScriptParser::PlotStatementContext* ctx) override;
    std::any visitPlotFunctionIdentifier(PlotScriptParser::PlotFunctionIdentifierContext* ctx) override;
    std::any visitExpression(PlotScriptParser::ExpressionContext* ctx) override;
    std::any visitValue(PlotScriptParser::ValueContext* ctx) override;
    std::any visitList(PlotScriptParser::ListContext* ctx) override;
    std::any visitFunctionCall(PlotScriptParser::FunctionCallContext* ctx) override;
    std::any visitStringLikeFunction(PlotScriptParser::StringLikeFunctionContext* ctx) override;
    std::any visitStringLikeFunctionParam(PlotScriptParser::StringLikeFunctionParamContext* ctx) override;
    std::any visitRangeArgs(PlotScriptParser::RangeArgsContext* ctx) override;
    std::any visitEmbeddedFunctionBlock(PlotScriptParser::EmbeddedFunctionBlockContext* ctx) override;
    std::any visitExportStatement(PlotScriptParser::ExportStatementContext* ctx) override;

//     std::any visitCpp_funcDeclaration(PlotScriptParser::Cpp_funcDeclarationContext* ctx) override;
//     std::any visitCpp_funcReturnType(PlotScriptParser::Cpp_funcReturnTypeContext* ctx) override;
//     std::any visitCpp_typeSpecifier(PlotScriptParser::Cpp_typeSpecifierContext* ctx) override;
//     std::any visitCpp_paramList(PlotScriptParser::Cpp_paramListContext* ctx) override;
//     std::any visitCpp_parameter(PlotScriptParser::Cpp_parameterContext* ctx) override;
//     std::any visitCpp_funcBody(PlotScriptParser::Cpp_funcBodyContext* ctx) override;
//     std::any visitCpp_funcStatement(PlotScriptParser::Cpp_funcStatementContext* ctx) override;
//     std::any visitCpp_assignment(PlotScriptParser::Cpp_assignmentContext* ctx) override;
//     std::any visitCpp_varDeclaration(PlotScriptParser::Cpp_varDeclarationContext* ctx) override;
//     std::any visitCpp_return(PlotScriptParser::Cpp_returnContext* ctx) override;
//     std::any visitCpp_controlStructure(PlotScriptParser::Cpp_controlStructureContext* ctx) override;
//     std::any visitCpp_expr(PlotScriptParser::Cpp_exprContext* ctx) override;
 };

#endif

