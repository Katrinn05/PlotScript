#pragma once
#include "PlotScriptError.h"

#define PSL_THROW(type, ctx, msg)                                                     \
    do {                                                                              \
        const auto* __tok = (ctx) ? (ctx)->getStart() : nullptr;                      \
        std::string __src = (__tok && __tok->getTokenSource())                        \
                              ? __tok->getTokenSource()->getSourceName()              \
                              : "";                                                   \
        const int  __line = __tok ? static_cast<int>(__tok->getLine()) : -1;          \
        const int  __col  = __tok ? static_cast<int>(__tok->getCharPositionInLine())  \
                                  : -1;                                               \
        throw type{ (msg), __src, __line, __col };                                    \
    } while (false)

struct LexerError    : PlotScriptError { using PlotScriptError::PlotScriptError; };
struct ParserError   : PlotScriptError { using PlotScriptError::PlotScriptError; };
struct SemanticError : PlotScriptError { using PlotScriptError::PlotScriptError; };
struct RuntimeError  : PlotScriptError { using PlotScriptError::PlotScriptError; };
struct IOError       : PlotScriptError { using PlotScriptError::PlotScriptError; };
struct LogicError    : PlotScriptError { using PlotScriptError::PlotScriptError; };
