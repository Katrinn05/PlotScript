parser grammar PlotScriptParser;

options{
  tokenVocab=PlotScriptLexer;
}

/*──────────────────────────
  Parser rules – PlotScript
  ──────────────────────────*/

program          : plotDefinition* exportStatement? EOF ;

plotDefinition   : plotName plotBlock ;
plotName         : ID ;

plotBlock        : PLOT_LBRACKET plotStatement* PLOT_RBRACKET ;
plotStatement    : plotFunctionIdentifier ASSIGN expression BLOCK_DELIMITER ;

plotFunctionIdentifier
                 : AXIS1 | AXIS2 | COLOR | OUTPUT | AXIS1_SCALE | AXIS2_SCALE ;

expression       : value | list | functionCall ;
value            : STRING | NUMBER | ID ;
list             : LIST_LBRACKET (value (VALUE_DELIMITER value)*) LIST_RBRACKET ;

functionCall     : ARRANGE FUNC_CALL_LBRACKET rangeArgs FUNC_CALL_RBRACKET
                 | stringLikeFunction  FUNC_CALL_LBRACKET stringLikeFunctionParam FUNC_CALL_RBRACKET
                 | FUNC FUNC_CALL_LBRACKET embeddedFunctionBlock FUNC_CALL_RBRACKET
                 ;

stringLikeFunction  : INPUT ;
stringLikeFunctionParam : STRING ;


rangeArgs        : FIRST FUNC_CALL_LBRACKET NUMBER FUNC_CALL_RBRACKET VALUE_DELIMITER
                   LAST FUNC_CALL_LBRACKET NUMBER FUNC_CALL_RBRACKET 
                 | FIRST FUNC_CALL_LBRACKET NUMBER FUNC_CALL_RBRACKET VALUE_DELIMITER
                   LAST FUNC_CALL_LBRACKET NUMBER FUNC_CALL_RBRACKET VALUE_DELIMITER
                   STEP FUNC_CALL_LBRACKET NUMBER FUNC_CALL_RBRACKET
                 ;

/*─────────────
  Embedded C++ code
  ─────────────*/

embeddedFunctionBlock  : CPP_FUNC_START cpp_funcDeclaration CPP_FUNC_END ;

cpp_funcDeclaration
                 : cpp_funcReturnType CPP_ID CPP_LPAREN cpp_paramList? CPP_RPAREN cpp_funcBody
                 ;

cpp_funcReturnType
                 : CPP_TYPE_VOID
                 | cpp_typeSpecifier
                 ;

cpp_typeSpecifier
                 : CPP_TYPE_INT
                 | CPP_TYPE_DOUBLE
                 | CPP_TYPE_BOOL
                 ;

cpp_paramList : cpp_parameter (CPP_COMMA cpp_parameter)* ;
cpp_parameter : cpp_typeSpecifier CPP_ID ;

cpp_funcBody  : CPP_LBRACE cpp_funcStatement* CPP_RBRACE ;

cpp_funcStatement
                 : cpp_varDeclaration CPP_SEMI
                 | cpp_assignment CPP_SEMI
                 | cpp_return CPP_SEMI
                 | cpp_controlStructure
                 ;

cpp_assignment : CPP_ID CPP_ASSIGN cpp_expr ;
cpp_varDeclaration : cpp_typeSpecifier CPP_ID (CPP_ASSIGN cpp_expr)? ;
cpp_return : CPP_RETURN cpp_expr? ;
cpp_controlStructure
                 : CPP_IF CPP_LPAREN cpp_expr CPP_RPAREN cpp_funcBody
                   (CPP_ELSE cpp_funcBody)?
                 | CPP_FOR CPP_LPAREN cpp_assignment? CPP_SEMI cpp_expr? CPP_SEMI
                   cpp_assignment? CPP_RPAREN cpp_funcBody
                 ;

cpp_expr         : cpp_expr (CPP_PLUS | CPP_MINUS | CPP_STAR | CPP_DIV) cpp_expr
                 | CPP_LPAREN cpp_expr CPP_RPAREN
                 | CPP_ID
                 | CPP_NUMBER
                 | cpp_expr (CPP_AND | CPP_OR) cpp_expr
                 | CPP_NOT cpp_expr
                 | CPP_TRUE_KW
                 | CPP_FALSE_KW
                 ;

/*─────────────
  Embedded Python code
  ─────────────*/
pyEmbeddedFunctionBlock
    : PY_FUNC_START py_funcDef PY_FUNC_END
    ;

py_funcDef
    : PY_DEF PY_ID PY_LPAREN py_paramList? PY_RPAREN PY_COLON py_funcBody
    ;

py_paramList
    : PY_ID (PY_COMMA PY_ID)*
    ;

py_funcBody
    : PY_LBRACE py_statement* PY_RBRACE
    ;

py_statement
    : py_varAssign PY_COLON
    | py_return PY_COLON
    | py_ifStatement
    | py_forStatement
    | py_whileStatement
    | py_expr PY_COLON
    ;

py_varAssign
    : PY_ID PY_ASSIGN py_expr
    ;

py_return
    : PY_RETURN py_expr?
    ;

py_ifStatement
    : PY_IF py_expr PY_COLON py_funcBody (PY_ELSE PY_COLON py_funcBody)?
    ;

py_forStatement
    : PY_FOR PY_ID PY_IN py_expr PY_COLON py_funcBody
    ;

py_whileStatement
    : PY_WHILE py_expr PY_COLON py_funcBody
    ;

py_expr
    : py_expr PY_PLUS py_expr
    | py_expr PY_MINUS py_expr
    | py_expr PY_STAR py_expr
    | py_expr PY_DIV py_expr
    | PY_LPAREN py_expr PY_RPAREN
    | PY_ID
    | PY_NUMBER
    | PY_STRING
    | PY_TRUE_KW
    | PY_FALSE_KW
    | PY_NONE_KW
    | PY_NOT py_expr
    | py_expr PY_AND py_expr
    | py_expr PY_OR py_expr
    ;


/*────────────
  Export
  ────────────*/

exportStatement : EXPORT FUNC_CALL_LBRACKET plotName (VALUE_DELIMITER plotName)* FUNC_CALL_RBRACKET ;
