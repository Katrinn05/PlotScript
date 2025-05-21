#include "ASTBuilderVisitor.h"
#include "PlotScriptLexer.h"
#include "PlotScriptParser.h"
#include "AST.h"
#include <gtest/gtest.h>
#include <antlr4-runtime.h>

using namespace antlrcpp;
using namespace antlr4;

static Program* buildAST(const std::string& src) {
  ANTLRInputStream input(src);
  PlotScriptLexer lexer(&input);
  CommonTokenStream tokens(&lexer);
  PlotScriptParser parser(&tokens);
  auto* tree = parser.program();            // entry-point is 'program'
  ASTBuilderVisitor vis;
  return std::any_cast<Program*>(vis.visit(tree));
}

TEST(ASTBuilderTests, SimplePlotCommand) {
  // 1) plot name "myPlot" then '{' ... '}' 
  // 2) axis1, axis2, output (must be single-quoted) 
  // 3) every statement ends with ';'
  const char* src = R"(
    myPlot {
      axis1: [0, 1];
      axis2: [2, 3];
      output: 'out.png';
    }
  )";

  Program* prog = buildAST(src);
  ASSERT_EQ(prog->statements.size(), 1u);

  auto* plotCmd = dynamic_cast<PlotCommandStmt*>(prog->statements[0]);
  ASSERT_NE(plotCmd, nullptr);

  // Check axis1 list
  auto* list1 = dynamic_cast<ListExpr*>(plotCmd->axis1Expr);
  ASSERT_NE(list1, nullptr);
  EXPECT_EQ(list1->elements.size(), 2u);
  EXPECT_DOUBLE_EQ(static_cast<NumberExpr*>(list1->elements[0])->value, 0.0);
  EXPECT_DOUBLE_EQ(static_cast<NumberExpr*>(list1->elements[1])->value, 1.0);

  // Check axis2 list
  auto* list2 = dynamic_cast<ListExpr*>(plotCmd->axis2Expr);
  ASSERT_NE(list2, nullptr);
  EXPECT_EQ(list2->elements.size(), 2u);
  EXPECT_DOUBLE_EQ(static_cast<NumberExpr*>(list2->elements[0])->value, 2.0);
  EXPECT_DOUBLE_EQ(static_cast<NumberExpr*>(list2->elements[1])->value, 3.0);

  // Check output file
  EXPECT_EQ(plotCmd->outputFile, "out.png");

  delete prog;
}

TEST(ASTBuilderTests, EmptyPlotBlock) {
  // Even an empty block parses (but will create a PlotCommandStmt with null exprs)
  const char* src = "foo {}";
  Program* prog = buildAST(src);
  ASSERT_EQ(prog->statements.size(), 1u);
  auto* plotCmd = dynamic_cast<PlotCommandStmt*>(prog->statements[0]);
  ASSERT_NE(plotCmd, nullptr);
  EXPECT_EQ(plotCmd->axis1Expr, nullptr);
  EXPECT_EQ(plotCmd->axis2Expr, nullptr);
  EXPECT_TRUE(plotCmd->outputFile.empty());
  delete prog;
}
