parser grammar PlotScriptParser;

options{
  tokenVocab=PlotScriptLexer;
}

/*─────────────────────────
  Parser rules – PlotScript
  ─────────────────────────*/

program          : plotDefinition* exportStatement? EOF ;

plotDefinition   : plotName plotBlock ;
plotName         : ID ;

plotBlock        : TOKEN_PLOT_LBRACKET plotStatement* TOKEN_PLOT_RBRACKET ;
plotStatement    : plotFunctionIdentifier TOKEN_ASSIGN expression TOKEN_BLOCK_DELIMITER ;

plotFunctionIdentifier
                 : TOKEN_AXIS1 | TOKEN_AXIS2 | TOKEN_COLOR | TOKEN_OUTPUT | TOKEN_AXIS1_SCALE | TOKEN_AXIS2_SCALE ;

expression       : value | list | functionCall ;
value            : STRING | NUMBER | ID ;
list             : TOKEN_LIST_LBRACKET (value (TOKEN_VALUE_DELIMITER value)*) TOKEN_LIST_RBRACKET ;

functionCall     : TOKEN_ARRANGE TOKEN_FUNC_CALL_LBRACKET rangeArgs TOKEN_FUNC_CALL_RBRACKET
                 | stringLikeFunction  TOKEN_FUNC_CALL_LBRACKET stringLikeFunctionParam TOKEN_FUNC_CALL_RBRACKET
                 | TOKEN_FUNC TOKEN_FUNC_CALL_LBRACKET embeddedFunctionBlock TOKEN_FUNC_CALL_RBRACKET
                 ;

stringLikeFunction  : TOKEN_INPUT ;
stringLikeFunctionParam : STRING ;


rangeArgs        : TOKEN_FIRST TOKEN_FUNC_CALL_LBRACKET NUMBER TOKEN_FUNC_CALL_RBRACKET TOKEN_VALUE_DELIMITER
                   TOKEN_LAST TOKEN_FUNC_CALL_LBRACKET NUMBER TOKEN_FUNC_CALL_RBRACKET 
                 | TOKEN_FIRST TOKEN_FUNC_CALL_LBRACKET NUMBER TOKEN_FUNC_CALL_RBRACKET TOKEN_VALUE_DELIMITER
                   TOKEN_LAST TOKEN_FUNC_CALL_LBRACKET NUMBER TOKEN_FUNC_CALL_RBRACKET TOKEN_VALUE_DELIMITER
                   TOKEN_STEP TOKEN_FUNC_CALL_LBRACKET NUMBER TOKEN_FUNC_CALL_RBRACKET
                 ;

/*─────────────────────────
  Embedded C++ code
  ─────────────────────────*/

embeddedFunctionBlock  : TOKEN_C_FUNC_START c_funcDeclaration C_FUNC_END ;

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

/*─────────────────────────
  Export
  ─────────────────────────*/

exportStatement : TOKEN_EXPORT TOKEN_FUNC_CALL_LBRACKET plotName (TOKEN_VALUE_DELIMITER plotName)* TOKEN_FUNC_CALL_RBRACKET ;
