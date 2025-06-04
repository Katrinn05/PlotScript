// InterpreterTest.cpp
//
// Tests for the Interpreter class (evaluate methods). Comments in English.
// Uses Google Test. Ensure you link against gtest, antlr4-runtime, and your project library.
//
// Example CMake snippet:
//   add_executable(InterpreterTest
//       InterpreterTest.cpp
//       # plus project sources if needed
//   )
//   target_link_libraries(InterpreterTest
//       gtest_main
//       antlr4_runtime
//       # plus your project library containing Interpreter.o, AST.o, etc.
//   )
//   add_test(NAME InterpreterTest COMMAND InterpreterTest)

#include <gtest/gtest.h>
#include <fstream>
#include <cstdio>  // for std::remove

#include "Interpreter.h"
#include "AST.h"

using namespace std;

// Test that evaluating a NumberExpr yields a single-element vector.
TEST(InterpreterTest, EvaluateNumberExpr) {
    Interpreter interp;
    const NumberExpr num(42.5);
    vector<double> result = interp.evaluate(&num);
    ASSERT_EQ(result.size(), 1u);
    EXPECT_DOUBLE_EQ(result[0], 42.5);
}

// Test that evaluating a ListExpr with nested NumberExpr concatenates correctly.
TEST(InterpreterTest, EvaluateListExprNested) {
    Interpreter interp;

    // Build a nested list: [1, [2, 3], 4]
    Expr* innerList = new ListExpr({ new NumberExpr(2.0), new NumberExpr(3.0) });
    Expr* outerList = new ListExpr({ new NumberExpr(1.0), innerList, new NumberExpr(4.0) });

    vector<double> result = interp.evaluate(outerList);

    // The result should be [1, 2, 3, 4]
    vector<double> expected = {1.0, 2.0, 3.0, 4.0};
    EXPECT_EQ(result.size(), expected.size());
    for (size_t i = 0; i < expected.size(); ++i) {
        EXPECT_DOUBLE_EQ(result[i], expected[i]);
    }

    delete outerList;  // This also deletes innerList and its children
}

// Test that ArrangeExpr yields the correct sequence when first <= last.
TEST(InterpreterTest, EvaluateArrangeExprAscending) {
    Interpreter interp;

    // Arrange from 0 to 3 with step 1: should be [0,1,2,3]
    ArrangeExpr arr(0.0, 3.0, 1.0, true);
    vector<double> result = interp.evaluate(&arr);

    vector<double> expected = {0.0, 1.0, 2.0, 3.0};
    EXPECT_EQ(result.size(), expected.size());
    for (size_t i = 0; i < expected.size(); ++i) {
        EXPECT_DOUBLE_EQ(result[i], expected[i]);
    }
}

// Test that ArrangeExpr yields the correct descending sequence when first > last.
TEST(InterpreterTest, EvaluateArrangeExprDescending) {
    Interpreter interp;

    // Arrange from 3 down to 1 with step 1: should be [3,2,1]
    ArrangeExpr arr(3.0, 1.0, 1.0, true);
    vector<double> result = interp.evaluate(&arr);

    vector<double> expected = {3.0, 2.0, 1.0};
    EXPECT_EQ(result.size(), expected.size());
    for (size_t i = 0; i < expected.size(); ++i) {
        EXPECT_DOUBLE_EQ(result[i], expected[i]);
    }
}

// Test that ArrangeExpr with non-positive step throws an exception.
TEST(InterpreterTest, ArrangeExprZeroStepThrows) {
    Interpreter interp;

    // hasStep = true but step = 0.0 → should throw runtime_error
    ArrangeExpr arr(0.0, 5.0, 0.0, true);
    EXPECT_THROW(interp.evaluate(&arr), std::runtime_error);
}

// Test that IdentifierExpr with no prior assignment throws invalid_argument (via env_.get).
TEST(InterpreterTest, EvaluateIdentifierExprUndefined) {
    Interpreter interp;

    IdentifierExpr id("undefinedVar");
    EXPECT_THROW(interp.evaluate(&id), std::exception);
}

// Test InputExpr by creating a small temporary file on disk.
TEST(InterpreterTest, EvaluateInputExprReadsFile) {
    // Create a temporary file with a few numbers
    const std::string filename = "temp_test_input.txt";
    std::ofstream ofs(filename);
    ASSERT_TRUE(ofs.is_open());
    ofs << "10.0 20.5 30\n";
    ofs.close();

    Interpreter interp;
    InputExpr inpExpr(filename);

    vector<double> result = interp.evaluate(&inpExpr);

    // The file contained "10.0 20.5 30"
    vector<double> expected = {10.0, 20.5, 30.0};
    EXPECT_EQ(result.size(), expected.size());
    for (size_t i = 0; i < expected.size(); ++i) {
        EXPECT_DOUBLE_EQ(result[i], expected[i]);
    }

    // Clean up
    std::remove(filename.c_str());
}

// Test that evalIdentifier returns a previously set variable via interpret().
TEST(InterpreterTest, AssignAndEvaluateIdentifier) {
    // Build a Program manually: x: [1,2,3]; y: x
    Program* program = new Program();
    Expr* listExpr = new ListExpr({ new NumberExpr(1.0), new NumberExpr(2.0), new NumberExpr(3.0) });
    program->statements.push_back(new AssignmentStmt("x", listExpr));
    // y := x
    program->statements.push_back(new AssignmentStmt("y", new IdentifierExpr("x")));

    Interpreter interp;
    // interpret(program) will set env_["x"] = {1,2,3} and then env_["y"] = env_["x"]
    EXPECT_NO_THROW(interp.interpret(program));

    // Now evaluate IdentifierExpr("y") directly
    IdentifierExpr idY("y");
    vector<double> result = interp.evaluate(&idY);

    vector<double> expected = {1.0, 2.0, 3.0};
    EXPECT_EQ(result.size(), expected.size());
    for (size_t i = 0; i < expected.size(); ++i) {
        EXPECT_DOUBLE_EQ(result[i], expected[i]);
    }

    delete program;
}






