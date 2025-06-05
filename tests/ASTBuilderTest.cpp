// ASTBuilderVisitorTest.cpp
//
// Tests for ASTBuilderVisitor (the ANTLR visitor that builds our AST).
// Uses Google Test. Comments are in English.
//
// To compile, make sure you link against:
// - gtest / gtest_main
// - antlr4-runtime
// - your PlotScript parser and lexer object files/libraries
//
// Example CMake snippet:
//   add_executable(ASTBuilderVisitorTest
//       ASTBuilderVisitorTest.cpp
//       # plus any needed .o or .cpp for ANTLR-generated parser/lexer
//   )
//   target_link_libraries(ASTBuilderVisitorTest
//       gtest_main
//       antlr4_runtime
//       # plus your project library that contains ASTBuilderVisitor.o, AST.o, Parser.o, etc.
//   )
//   add_test(NAME ASTBuilderVisitorTest COMMAND ASTBuilderVisitorTest)

#include <gtest/gtest.h>
#include <antlr4-runtime.h>

#include "PlotScriptLexer.h"
#include "PlotScriptParser.h"
#include "ASTBuilderVisitor.h"
#include "AST.h"

using namespace antlr4;

// Helper to run the visitor on a given script string.
static Program* buildASTFromString(const std::string& script) {
    ANTLRInputStream input(script);
    PlotScriptLexer lexer(&input);
    CommonTokenStream tokens(&lexer);
    PlotScriptParser parser(&tokens);

    // Remove default error listeners and add a ConsoleErrorListener to catch syntax errors.
    parser.removeErrorListeners();
    parser.addErrorListener(new ConsoleErrorListener());

    auto* tree = parser.program();
    if (parser.getNumberOfSyntaxErrors() > 0) {
        // If parsing fails, we return nullptr (the test can check for null).
        return nullptr;
    }

    ASTBuilderVisitor visitor;
    std::any rootAny;
    try {
        rootAny = visitor.visit(tree);
    } catch (const std::exception& ex) {
        // Propagate exceptions to the test.
        throw;
    }

    try {
        return std::any_cast<Program*>(rootAny);
    } catch (const std::bad_any_cast&) {
        return nullptr;
    }
}

// Test that a minimal single-plot definition is parsed correctly.
TEST(ASTBuilderVisitorTest, SinglePlotDefinition) {
    const std::string script = R"plotscript(
        plotFoo {
            axis1: [1, 2, 3];
            axis2: [4, 5, 6];
            output: 'foo.png';
        }
    )plotscript";

    Program* program = buildASTFromString(script);
    ASSERT_NE(program, nullptr) << "AST should not be null for valid script";

    // The Program should contain exactly one ASTNode, which is a PlotCommandStmt.
    ASSERT_EQ(program->statements.size(), 1u)
        << "Program should have exactly one statement";

    // Down-cast to PlotCommandStmt
    auto* stmt = dynamic_cast<PlotCommandStmt*>(program->statements[0]);
    ASSERT_NE(stmt, nullptr) << "First statement should be PlotCommandStmt";

    // Check the plot name:
    EXPECT_EQ(stmt->name, "plotFoo");

    // Check that axis1Expr is a ListExpr with three NumberExpr children: 1, 2, 3
    auto* axis1List = dynamic_cast<ListExpr*>(stmt->axis1Expr);
    ASSERT_NE(axis1List, nullptr) << "axis1Expr should be a ListExpr";

    std::vector<double> expected1 = {1.0, 2.0, 3.0};
    ASSERT_EQ(axis1List->elements.size(), expected1.size());
    for (size_t i = 0; i < expected1.size(); ++i) {
        auto* num = dynamic_cast<NumberExpr*>(axis1List->elements[i]);
        ASSERT_NE(num, nullptr) << "Each element of axis1 list should be NumberExpr";
        EXPECT_DOUBLE_EQ(num->value, expected1[i]);
    }

    // Check that axis2Expr is a ListExpr with values 4, 5, 6
    auto* axis2List = dynamic_cast<ListExpr*>(stmt->axis2Expr);
    ASSERT_NE(axis2List, nullptr) << "axis2Expr should be a ListExpr";

    std::vector<double> expected2 = {4.0, 5.0, 6.0};
    ASSERT_EQ(axis2List->elements.size(), expected2.size());
    for (size_t i = 0; i < expected2.size(); ++i) {
        auto* num = dynamic_cast<NumberExpr*>(axis2List->elements[i]);
        ASSERT_NE(num, nullptr);
        EXPECT_DOUBLE_EQ(num->value, expected2[i]);
    }

    // Check that outputFile is 'foo.png'
    EXPECT_EQ(stmt->outputFile, "foo.png");

    // Clean up
    delete program;
}

// Test that missing semicolons or braces produce a syntax error (visitor returns nullptr).
TEST(ASTBuilderVisitorTest, SyntaxErrorReturnsNull) {
    // Missing closing brace
    const std::string badScript = R"plotscript(
        plotBad {
            axis1: [1, 2, 3];
            axis2: [4, 5, 6];
            output: 'bad.png';
        // missing closing brace here
    )plotscript";

    Program* program = buildASTFromString(badScript);
    EXPECT_EQ(program, nullptr) << "Invalid syntax should produce a null AST";
}

// Test that arrange/function-call constructs are parsed into ArrangeExpr or CppFuncExpr.
TEST(ASTBuilderVisitorTest, ArrangeAndEmbeddedFunction) {
    const std::string script = R"plotscript(
        myPlot {
            axis1: arrange(first(0), last(2), step(1));
            axis2: func(
                $CPP$
                double f(double x){
                    return x * x;
                }
                $$
            );
            output: 'out.png';
        }
    )plotscript";

    Program* program = buildASTFromString(script);
    ASSERT_NE(program, nullptr);

    ASSERT_EQ(program->statements.size(), 1u);
    auto* stmt = dynamic_cast<PlotCommandStmt*>(program->statements[0]);
    ASSERT_NE(stmt, nullptr);

    // axis1Expr should be an ArrangeExpr
    auto* arrange = dynamic_cast<ArrangeExpr*>(stmt->axis1Expr);
    ASSERT_NE(arrange, nullptr) << "axis1Expr should be ArrangeExpr";
    EXPECT_DOUBLE_EQ(arrange->first, 0.0);
    EXPECT_DOUBLE_EQ(arrange->last, 2.0);
    EXPECT_DOUBLE_EQ(arrange->step, 1.0);
    EXPECT_TRUE(arrange->hasStep);

    // axis2Expr should be a CppFuncExpr
    auto* cppExpr = dynamic_cast<CppFuncExpr*>(stmt->axis2Expr);
    ASSERT_NE(cppExpr, nullptr) << "axis2Expr should be CppFuncExpr";
    // Quick sanity check: rawCode contains "double f(double x)"
    EXPECT_NE(cppExpr->rawCode.find("double f(double x)"), std::string::npos);

    delete program;
}

