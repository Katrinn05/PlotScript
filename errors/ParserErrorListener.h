#pragma once
#include "antlr4-runtime.h"
#include "Errors.h"

class ParserErrorListener : public antlr4::BaseErrorListener {
    void syntaxError(antlr4::Recognizer*, antlr4::Token* tok,
                     size_t line, size_t col,
                     const std::string& msg,
                     std::exception_ptr) override {
        std::string src = (tok && tok->getTokenSource())
                            ? tok->getTokenSource()->getSourceName()
                            : "";
        int l = tok ? static_cast<int>(line) : -1;
        int c = tok ? static_cast<int>(col)  : -1;
        throw ParserError{msg, src, l, c};
                     }
};
