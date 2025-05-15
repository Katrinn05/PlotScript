cmake_minimum_required(VERSION 3.15)

project(PlotScriptInterpreter LANGUAGES CXX)
set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED ON)
## you should set the path to your antlr4 jar file
set(ANTLR4_JAR_PATH "$ENV{HOME}/.local/lib/antlr/antlr-4.13.2-complete.jar")
set(GRAMMAR_DIR ${CMAKE_SOURCE_DIR}/grammar)
set(LEXER_GRAMMAR_FILE ${GRAMMAR_DIR}/PlotScriptLexer.g4)
set(PARSER_GRAMMAR_FILE ${GRAMMAR_DIR}/PlotScriptParser.g4)
set(ANTLR4_OUTPUT_DIR "${CMAKE_CURRENT_BINARY_DIR}/generated")

file(MAKE_DIRECTORY ${ANTLR4_OUTPUT_DIR})

add_custom_command(
  OUTPUT
    ${ANTLR4_OUTPUT_DIR}/PlotScriptLexer.cpp
    ${ANTLR4_OUTPUT_DIR}/PlotScriptLexer.h
    ${ANTLR4_OUTPUT_DIR}/PlotScriptParser.cpp
    ${ANTLR4_OUTPUT_DIR}/PlotScriptParser.h
    ${ANTLR4_OUTPUT_DIR}/PlotScriptVisitor.h
    ${ANTLR4_OUTPUT_DIR}/PlotScriptBaseVisitor.h

  COMMAND
    java -jar ${ANTLR4_JAR_PATH} -Dlanguage=Cpp -visitor -no-listener
    -o ${ANTLR4_OUTPUT_DIR}
    ${LEXER_GRAMMAR_FILE}
    ${PARSER_GRAMMAR_FILE}

  DEPENDS
    ${LEXER_GRAMMAR_FILE}
    ${PARSER_GRAMMAR_FILE}

  COMMENT "Generating ANTLR4 parser and lexer"
  VERBATIM
)

add_custom_target(antlr4_gen ALL DEPENDS 
    "${ANTLR4_OUTPUT_DIR}/PlotScriptParser.cpp" 
    "${ANTLR4_OUTPUT_DIR}/PlotScriptLexer.cpp" 
    "${ANTLR4_OUTPUT_DIR}/PlotScriptParserBaseVisitor.h"
)

include_directories(${ANTLR4_OUTPUT_DIR})
## you should set the path to your antlr4 runtime library
include_directories("/usr/local/include/antlr4-runtime")


set(SRC_DIR "${CMAKE_CURRENT_SOURCE_DIR}/src")
set(GEN_DIR "${CMAKE_CURRENT_BINARY_DIR}/generated")

add_executable(PlotScript
  ${SRC_DIR}/AST.cpp
  ${SRC_DIR}/ASTBuilderVisitor.cpp
  ${SRC_DIR}/Interpreter.cpp
  ${SRC_DIR}/Plotter.cpp
  ${SRC_DIR}/main.cpp

  ${GEN_DIR}/PlotScriptLexer.cpp
  ${GEN_DIR}/PlotScriptParser.cpp
)

add_dependencies(PlotScript antlr4_gen)

target_link_libraries(PlotScript PRIVATE /usr/local/lib/libantlr4-runtime.a)




