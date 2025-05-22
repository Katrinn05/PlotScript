lexer grammar PlotScriptLexer;

/*─────────────────────────
  Lexer default (PlotScript)
  ─────────────────────────*/

PLOT_LBRACKET  : '{';
PLOT_RBRACKET : '}';
LIST_LBRACKET : '[';
LIST_RBRACKET : ']';
FUNC_CALL_LBRACKET : '(';
FUNC_CALL_RBRACKET : ')';
BLOCK_DELIMITER    : ';';
VALUE_DELIMITER   : ',';
ASSIGN   : ':' ;

AXIS1 : 'axis1' | 'x' | 'X';
AXIS2 : 'axis2' | 'y' | 'Y';
COLOR : 'color' ;
OUTPUT : 'output' ;
ARRANGE : 'arrange' ;
INPUT : 'input' ;
AXIS1_SCALE : 'axis1-scale' | 'x-scale' | 'X-scale' ;
AXIS2_SCALE : 'axis2-scale' | 'y-scale' | 'Y-scale' ;
FUNC : 'func' ;

FIRST : 'first' ;
LAST : 'last' ;
STEP : 'step' ;

EXPORT : 'export' ;

C_FUNC_START : '$CPP$'  -> pushMode(FUNC_C) ;

STRING    : '\'' (Character | SpecialChar)* '\'' ;
NUMBER     : '-'? Digit+ ('.' Digit+)? 'f'? ;
ID : Letter Character* ;

fragment Letter    : [a-zA-Z] ;
fragment Digit     : [0-9] ;
fragment Character : Letter | Digit | '_' | '-' ;
fragment SpecialChar : [,.!@#$%^&()+={}`'~] ;

WS : [ \t\r\n]+ -> skip ;

/*─────────────────────────
  Lexer MODE for embedded C++ code
  ─────────────────────────*/

mode FUNC_C;

C_FUNC_END   : '$$' -> popMode ;

C_TOKEN_TYPE_INT : 'int' ;
C_TOKEN_TYPE_DOUBLE : 'double' | 'float' ;
C_TOKEN_TYPE_BOOL : 'bool' ;
C_TOKEN_TYPE_VOID : 'void' ;

C_TOKEN_PLUS    : '+' ;
C_TOKEN_MINUS  : '-' ;
C_TOKEN_STAR    : '*' ;
C_TOKEN_DIV    : '/' ;
C_TOKEN_ASSIGN  : '=' ;
C_TOKEN_COMMA   : ',' ;
C_TOKEN_SEMI    : ';' ;
C_TOKEN_LPAREN  : '(' ;
C_TOKEN_RPAREN : ')' ;
C_TOKEN_LBRACE  : '{' ;
C_TOKEN_RBRACE : '}' ;

C_TOKEN_IF      : 'if' ;
C_TOKEN_ELSE    : 'else' ;
C_TOKEN_FOR     : 'for' ;
C_TOKEN_WHILE   : 'while' ;
C_TOKEN_DO      : 'do' ;
C_TOKEN_RETURN  : 'return' ;

C_TOKEN_AND     : '&&' ;
C_TOKEN_OR      : '||' ;
C_TOKEN_NOT     : '!' ;

C_TOKEN_LT      : '<' ;
C_TOKEN_GT      : '>' ;
C_TOKEN_LTE     : '<=' ;
C_TOKEN_GTE     : '>=' ;
C_TOKEN_EQ      : '==' ;
C_TOKEN_NEQ     : '!=' ;

C_TOKEN_SQRT : 'sqrt' ;
C_TOKEN_SIN  : 'sin' ;
C_TOKEN_COS  : 'cos' ;
C_TOKEN_TAN  : 'tan' ;
C_TOKEN_LOG  : 'log' ;
C_TOKEN_EXP  : 'exp' ;

C_NUMBER  : '-'? Digit+ ('.' Digit+)? ;
C_ID      : [a-zA-Z_][a-zA-Z0-9_]* ;

C_TOKEN_TRUE_KW  : 'true' ;
C_TOKEN_FALSE_KW : 'false' ;

C_WS : [ \t\r\n]+        -> skip ;
C_LINE_COMMENT
        : '//' ~[\r\n]*    -> skip ;
C_BLOCK_COMMENT
        : '/*' .*? '*/'    -> skip ;

/*─────────────────────────*/