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
//TODO

/*────────────
  Export
  ────────────*/

exportStatement : EXPORT FUNC_CALL_LBRACKET plotName (VALUE_DELIMITER plotName)* FUNC_CALL_RBRACKET ;