#pragma once
#include "PlotScriptError.h"

#define PSL_THROW(type, ctx, msg)                                         \
    throw type{ (msg),                                                    \
                (ctx)->getStart()->getTokenSource()->getSourceName(),     \
                (int)(ctx)->getStart()->getLine(),                        \
                (int)(ctx)->getStart()->getCharPositionInLine() }


struct LexerError    : PlotScriptError { using PlotScriptError::PlotScriptError; };
struct ParserError   : PlotScriptError { using PlotScriptError::PlotScriptError; };
struct SemanticError : PlotScriptError { using PlotScriptError::PlotScriptError; };
struct RuntimeError  : PlotScriptError { using PlotScriptError::PlotScriptError; };
struct IOError       : PlotScriptError { using PlotScriptError::PlotScriptError; };
struct LogicError    : PlotScriptError { using PlotScriptError::PlotScriptError; };
