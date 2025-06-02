cmake_minimum_required(VERSION 3.15)
cmake_policy(SET CMP0135 NEW)

project(PlotScriptInterpreter LANGUAGES CXX)

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED ON)
set(CMAKE_EXPORT_COMPILE_COMMANDS ON)

find_program(GPP_EXECUTABLE g++)
if (MSVC)
    message(STATUS "MSVC detected, skip g++ check.")
elseif (NOT GPP_EXECUTABLE)
    message(FATAL_ERROR "g++ not found. Please install g++ and ensure it is in your PATH.")
endif()

if(WIN32)
  set(ANTLR4_JAR_PATH "C:/tools/antlr/antlr-4.13.2-complete.jar")
else()
  set(ANTLR4_JAR_PATH "$ENV{HOME}/.local/lib/antlr/antlr-4.13.2-complete.jar")
endif()

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

if(WIN32)
  set(ANTLR4_RUNTIME_INCLUDE "C:/tools/vcpkg/installed/x64-windows/include/antlr4-runtime")
  set(ANTLR4_RUNTIME_LIB     "C:/tools/vcpkg/installed/x64-windows/lib/antlr4-runtime.lib")
else()
  set(ANTLR4_RUNTIME_INCLUDE "/usr/local/include/antlr4-runtime")
  set(ANTLR4_RUNTIME_LIB     "/usr/local/lib/libantlr4-runtime.a")
endif()

include_directories(${ANTLR4_RUNTIME_INCLUDE})

set(SRC_DIR "${CMAKE_CURRENT_SOURCE_DIR}/src")
include_directories(${SRC_DIR})
set(GEN_DIR "${CMAKE_CURRENT_BINARY_DIR}/generated")

add_library(PlotScriptLib
  ${SRC_DIR}/AST.cpp
  ${SRC_DIR}/ASTBuilderVisitor.cpp
  ${SRC_DIR}/Environment.cpp
  ${SRC_DIR}/Interpreter.cpp
  ${SRC_DIR}/Plotter.cpp
  ${GEN_DIR}/PlotScriptLexer.cpp
  ${GEN_DIR}/PlotScriptParser.cpp
)

add_dependencies(PlotScriptLib antlr4_gen)

if(WIN32)
  target_link_libraries(PlotScriptLib PRIVATE ${ANTLR4_RUNTIME_LIB})
else()
  target_link_libraries(PlotScriptLib PRIVATE ${ANTLR4_RUNTIME_LIB})
endif()

add_executable(PlotScript src/main.cpp)
target_link_libraries(PlotScript PRIVATE PlotScriptLib)


if(CMAKE_CONFIGURATION_TYPES) 
  set_target_properties(PlotScript PROPERTIES
    RUNTIME_OUTPUT_DIRECTORY_DEBUG   ${CMAKE_SOURCE_DIR}
    RUNTIME_OUTPUT_DIRECTORY_RELEASE ${CMAKE_SOURCE_DIR}
    RUNTIME_OUTPUT_DIRECTORY_MINSIZEREL ${CMAKE_SOURCE_DIR}
    RUNTIME_OUTPUT_DIRECTORY_RELWITHDEBINFO ${CMAKE_SOURCE_DIR}
  )
else()
  set_target_properties(PlotScript PROPERTIES
    RUNTIME_OUTPUT_DIRECTORY ${CMAKE_SOURCE_DIR}
  )
endif()

enable_testing()
find_package(GTest REQUIRED)
include_directories(${GTEST_INCLUDE_DIRS})

add_executable(InterpreterTest
  tests/InterpreterTest.cpp
)
target_link_libraries(InterpreterTest
  PRIVATE
    GTest::gtest_main
    PlotScriptLib
)
include(GoogleTest)
gtest_discover_tests(InterpreterTest)

add_executable(EnvironmentTest
  tests/EnvironmentTest.cpp
)
target_link_libraries(EnvironmentTest
  PRIVATE
    GTest::gtest_main
    PlotScriptLib
)
gtest_discover_tests(EnvironmentTest)

add_executable(ASTBuilderTest
  tests/ASTBuilderTest.cpp
)
target_link_libraries(ASTBuilderTest
  PRIVATE
    GTest::gtest_main
    PlotScriptLib
)
gtest_discover_tests(ASTBuilderTest)

add_definitions(-DTEST_SRCDIR="${CMAKE_CURRENT_SOURCE_DIR}/tests")
