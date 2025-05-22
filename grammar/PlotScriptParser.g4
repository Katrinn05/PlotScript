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

embeddedFunctionBlock  : C_FUNC_START c_funcDeclaration C_FUNC_END ;

c_funcDeclaration
                 : c_funcReturnType C_ID C_TOKEN_LPAREN c_paramList? C_TOKEN_RPAREN c_funcBody
                 ;

c_funcReturnType
                 : C_TOKEN_TYPE_VOID
                 | c_typeSpecifier
                 ;

c_typeSpecifier
                 : C_TOKEN_TYPE_INT
                 | C_TOKEN_TYPE_DOUBLE
                 | C_TOKEN_TYPE_BOOL
                 ;

c_mathFunction 
                 : C_TOKEN_SQRT
                 | C_TOKEN_SIN
                 | C_TOKEN_COS
                 | C_TOKEN_TAN
                 | C_TOKEN_LOG
                 | C_TOKEN_EXP
                 ; 

c_paramList : c_parameter (C_TOKEN_COMMA c_parameter)* ;
c_parameter : c_typeSpecifier C_ID ;

c_funcBody  : C_TOKEN_LBRACE c_funcStatement* C_TOKEN_RBRACE ;

c_funcStatement
                 : c_varDeclaration C_TOKEN_SEMI
                 | c_assignment C_TOKEN_SEMI
                 | c_return C_TOKEN_SEMI
                 | c_controlStructure
                 ;

c_assignment : C_ID C_TOKEN_ASSIGN c_expr ;
c_varDeclaration : c_typeSpecifier C_ID (C_TOKEN_ASSIGN c_expr)? ;
c_return : C_TOKEN_RETURN c_expr? ;



c_mathFunctionCall
                 : 
                  c_mathFunction C_TOKEN_LPAREN c_expr C_TOKEN_RPAREN
                 ;

c_controlStructure
                 : C_TOKEN_IF C_TOKEN_LPAREN c_expr C_TOKEN_RPAREN c_funcBody
                      (C_TOKEN_ELSE c_funcBody)?
                 | C_TOKEN_FOR C_TOKEN_LPAREN c_assignment? C_TOKEN_SEMI c_expr? C_TOKEN_SEMI
                      c_assignment? C_TOKEN_RPAREN c_funcBody
                 | C_TOKEN_WHILE C_TOKEN_LPAREN c_expr C_TOKEN_RPAREN c_funcBody
                 | C_TOKEN_DO c_funcBody C_TOKEN_WHILE C_TOKEN_LPAREN c_expr C_TOKEN_RPAREN 
                 ;

c_expr         : c_expr (C_TOKEN_PLUS | C_TOKEN_MINUS | C_TOKEN_STAR | C_TOKEN_DIV) c_expr
                 | c_expr (C_TOKEN_LT | C_TOKEN_GT | C_TOKEN_LTE | C_TOKEN_GTE | C_TOKEN_EQ | C_TOKEN_NEQ ) c_expr
                 | C_TOKEN_LPAREN c_expr C_TOKEN_RPAREN
                 | C_ID
                 | C_NUMBER
                 | c_expr (C_TOKEN_AND | C_TOKEN_OR) c_expr
                 | C_TOKEN_NOT c_expr
                 | C_TOKEN_TRUE_KW
                 | C_TOKEN_FALSE_KW
                 ;

/*─────────────
  Embedded Python code
  ─────────────*/
//TODO


/*────────────
  Export
  ────────────*/

exportStatement : EXPORT FUNC_CALL_LBRACKET plotName (VALUE_DELIMITER plotName)* FUNC_CALL_RBRACKET ;
