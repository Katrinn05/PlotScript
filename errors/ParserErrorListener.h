#pragma once
#include "antlr4-runtime.h"
#include "Errors.h"

class ParserErrorListener : public antlr4::BaseErrorListener {
    void syntaxError(antlr4::Recognizer*, antlr4::Token* tok,
                     size_t line, size_t col,
                     const std::string& msg,
                     std::exception_ptr) override {
        throw ParserError{msg,
                          tok->getTokenSource()->getSourceName(),
                          static_cast<int>(line),
                          static_cast<int>(col)};
    }
};
