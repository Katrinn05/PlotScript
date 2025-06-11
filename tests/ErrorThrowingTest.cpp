#include <gtest/gtest.h>
#include <antlr4-runtime.h>

#include "PlotScriptLexer.h"
#include "PlotScriptParser.h"

#include "ASTBuilderVisitor.h"
#include "ParserErrorListener.h"
#include "Errors.h"
#include "Interpreter.h"
#include "AST.h"
#include <any>

static void runScript(const std::string& code) {
    antlr4::ANTLRInputStream input(code);
    PlotScriptLexer         lexer(&input);
    antlr4::CommonTokenStream tokens(&lexer);
    PlotScriptParser         parser(&tokens);

    parser.removeErrorListeners();
    parser.addErrorListener(new ParserErrorListener());

    ASTBuilderVisitor v;
    auto  ast = v.visitProgram(parser.program());
    const Program* program = std::any_cast<Program*>(ast);

    Interpreter interp;
    interp.interpret(program);        
}

TEST(Semantic, UndefinedVariable) {
    std::string s = R"ps(
        plot {
            y: x + 1;
            output: 'o.bmp';
        }
    )ps";        
    EXPECT_THROW(runScript(s), SemanticError);
}

TEST(Runtime, AxisSizeMismatch) {
    std::string s =
        "plot p { axis1: [1,2]; axis2: [1]; }"
        "output p";
    EXPECT_THROW(runScript(s), RuntimeError);
}

TEST(Parser, BadToken) {
    std::string s = "$$bad_token$$";
    EXPECT_THROW(runScript(s), ParserError);
}

TEST(IO, MissingInputFile) {
    std::string s = "axis1: input('__definitely_no_file.plot')";
    EXPECT_THROW(runScript(s), IOError);
}

