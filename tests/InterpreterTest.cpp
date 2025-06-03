#include "Interpreter.h"
#include "AST.h"
#include "Environment.h"
#include "gtest/gtest.h"

static NumberExpr* num(double v) { return new NumberExpr{v}; }
static ListExpr* list(std::initializer_list<Expr*> elems) {
  return new ListExpr{std::vector<Expr*>(elems)};
}
static IdentifierExpr* id(const std::string& name) {
  return new IdentifierExpr{name};
}

static AssignmentStmt* assign(const std::string& var, Expr* expr) {
  return new AssignmentStmt{ var, expr };
}

// we won’t test PlotCommandStmt here since it invokes Plotter::drawPlot

TEST(EvaluateTests, Number) {
  Interpreter I;
  auto vec = I.evaluate(num(42.5));
  ASSERT_EQ(vec.size(), 1);
  EXPECT_DOUBLE_EQ(vec[0], 42.5);
}

TEST(EvaluateTests, ListFlat) {
  Interpreter I;
  Expr* e = list({ num(1), list({ num(2), num(3) }), num(4) });
  auto vec = I.evaluate(e);
  EXPECT_EQ(vec, (std::vector<double>{1,2,3,4}));
}

TEST(EvaluateTests, UndefinedIdentifier) {
  Interpreter I;
  EXPECT_THROW(I.evaluate(id("x")), std::runtime_error);
}

TEST(InterpretTests, AssignmentAndLookup) {
  Interpreter I;
  Program P;
  P.statements.push_back(assign("a", list({ num(3), num(5) })));
  I.interpret(&P);

  auto result = I.evaluate(id("a"));
  EXPECT_EQ(result, (std::vector<double>{3,5}));
}

/*TEST(InterpretTests, PlotSizeMismatchThrows) {
  Interpreter I;
  // fake a PlotCommandStmt with mismatched list lengths
  PlotCommandStmt* plt = new PlotCommandStmt{
    list({ num(1), num(2) }),
    list({ num(3) }),
    std::string("dummy.png")
  };
  Program P;
  P.statements.push_back(plt);

  EXPECT_THROW(I.interpret(&P), std::runtime_error);
}*/

TEST(EvaluateEdgeCases, NullptrExprReturnsEmpty) {
  Interpreter I;
  auto v = I.evaluate(nullptr);
  EXPECT_TRUE(v.empty());
}

TEST(InterpretEdgeCases, EmptyProgramDoesNothing) {
  Interpreter I;
  Program P;
  EXPECT_NO_THROW(I.interpret(&P));
}

TEST(InterpreterTests, ReassignVariable) {
  Interpreter I;
  Program P;
  P.statements.push_back(assign("x", num(1)));        // x = [1]
  P.statements.push_back(assign("x", list({num(2), num(3)}))); // x = [2,3]
  I.interpret(&P);
  EXPECT_EQ(I.evaluate(id("x")), (std::vector<double>{2,3}));
}

TEST(InterpreterTests, ResultIsCopy) {
  Interpreter I;
  Program P; P.statements.push_back(assign("v", list({num(5)})));
  I.interpret(&P);
  auto out = I.evaluate(id("v"));
  out[0] = 99;                              
  EXPECT_EQ(I.evaluate(id("v"))[0], 5);
}

TEST(InterpreterTests, ListWithIdentifier) {
  Interpreter I;
  Program P;
  P.statements.push_back(assign("a", list({num(1), num(2)})));
  // b = [a, 3] → [1,2,3]
  P.statements.push_back(assign("b", list({ id("a"), num(3) })));
  I.interpret(&P);
  EXPECT_EQ(I.evaluate(id("b")), (std::vector<double>{1,2,3}));
}

TEST(InterpreterTests, NestedUndefinedIdentifierThrows) {
  Interpreter I;
  Program P;
  P.statements.push_back(assign("c", list({ num(1), id("missing") })));
  EXPECT_THROW(I.interpret(&P), std::runtime_error);
}





