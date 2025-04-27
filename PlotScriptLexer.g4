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



CPP_FUNC_START : '$CPP$'  -> pushMode(FUNC_CPP) ;
PY_FUNC_START : '$PY$'  -> pushMode(FUNC_PY) ;

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

mode FUNC_CPP;

CPP_FUNC_END   : '$$' -> popMode ;

CPP_TYPE_INT : 'int' ;
CPP_TYPE_DOUBLE : 'double' | 'float' ;
CPP_TYPE_BOOL : 'bool' ;
CPP_TYPE_VOID : 'void' ;

CPP_PLUS    : '+' ;
CPP_MINUS  : '-' ;
CPP_STAR    : '*' ;
CPP_DIV    : '/' ;
CPP_ASSIGN  : '=' ;
CPP_COMMA   : ',' ;
CPP_SEMI    : ';' ;
CPP_LPAREN  : '(' ;
CPP_RPAREN : ')' ;
CPP_LBRACE  : '{' ;
CPP_RBRACE : '}' ;

CPP_IF      : 'if' ;
CPP_ELSE    : 'else' ;
CPP_FOR     : 'for' ;
CPP_RETURN  : 'return' ;

CPP_AND     : '&&' ;
CPP_OR      : '||' ;
CPP_NOT     : '!' ;

CPP_NUMBER  : '-'? Digit+ ('.' Digit+)? ;
CPP_ID      : [a-zA-Z_][a-zA-Z0-9_]* ;

CPP_TRUE_KW  : 'true' ;
CPP_FALSE_KW : 'false' ;

CPP_WS : [ \t\r\n]+        -> skip ;
CPP_LINE_COMMENT
        : '//' ~[\r\n]*    -> skip ;
CPP_BLOCK_COMMENT
        : '/*' .*? '*/'    -> skip ;

/*─────────────────────────
  Lexer MODE for embedded Python code
  ─────────────────────────*/
mode FUNC_PY;

PY_FUNC_END   : '$$' -> popMode ;

PY_DEF        : 'def' ;
PY_IMPORT     : 'import' ;
PY_AS         : 'as' ;
PY_PASS       : 'pass' ;
PY_RETURN     : 'return' ;

PY_TRUE_KW    : 'True' ;
PY_FALSE_KW   : 'False' ;
PY_NONE_KW    : 'None' ;

PY_IF         : 'if' ;
PY_ELSE       : 'else' ;
PY_FOR        : 'for' ;
PY_IN         : 'in' ;
PY_WHILE      : 'while' ;

PY_AND        : 'and' ;
PY_OR         : 'or' ;
PY_NOT        : 'not' ;

PY_PLUS       : '+' ;
PY_MINUS      : '-' ;
PY_STAR       : '*' ;
PY_DIV        : '/' ;
PY_ASSIGN     : '=' ;
PY_COLON      : ':' ;
PY_COMMA      : ',' ;
PY_DOT        : '.' ;

PY_LPAREN     : '(' ;
PY_RPAREN     : ')' ;
PY_LBRACE     : '{' ;
PY_RBRACE     : '}' ;
PY_LBRACK     : '[' ;
PY_RBRACK     : ']' ;

PY_NUMBER     : '-'? Digit+ ('.' Digit+)? ;
PY_ID         : [a-zA-Z_][a-zA-Z0-9_]* ;

PY_STRING     : '\'' ( ~['\\] | '\\' . )* '\''   
              | '"' ( ~["\\] | '\\' . )* '"'
              ;

PY_WS         : [ \t\r\n]+ -> skip ;
PY_LINE_COMMENT : '#' ~[\r\n]* -> skip ;
