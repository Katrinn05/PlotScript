lexer grammar PlotScriptLexer;

/*─────────────────────────
  Lexer default (PlotScript)
  ─────────────────────────*/

CPP : 'CPP' ;
PY : 'PY' ;

FUNC_CPP_START : '$' CPP '$' -> pushMode(FUNC_CPP) ;
FUNC_PY_START : '$' PY '$' -> pushMode(FUNC_PY) ;

String     : '\'' Character* '\'' ;
Number     : '-'? Digit+ ('.' Digit+)? ;
Identifier : Letter Character* ;

fragment Letter    : [a-zA-Z] ;
fragment Digit     : [0-9] ;
fragment Character : Letter | Digit | '_' | '-' ;

Whitespace : [ \t\r\n]+ -> skip ;

/*─────────────────────────
  Lexer MODE for embedded C++ code
  ─────────────────────────*/

mode FUNC_CPP;

FUNC_END   : '$$' -> popMode ;

TYPE_INT      : 'int';
TYPE_DOUBLE   : 'double' | 'float';
TYPE_BOOL     : 'bool';
TYPE_VOID     : 'void';

PLUS    : '+';    MINUS  : '-';
STAR    : '*';    DIV    : '/';
ASSIGN  : '=';
COMMA   : ',';
SEMI    : ';';
LPAREN  : '(';    RPAREN : ')';
LBRACE  : '{';    RBRACE : '}';

IF      : 'if';
ELSE    : 'else';
FOR     : 'for';
RETURN  : 'return';

NUMBER  : '-'? Digit+ ('.' Digit+)? ;
ID      : [a-zA-Z_][a-zA-Z0-9_]* ;

WS_FUNC : [ \t\r\n]+       -> skip ;
LINE_COMMENT
        : '//' ~[\r\n]*    -> skip ;
BLOCK_COMMENT
        : '/*' .*? '*/'    -> skip ;

/*─────────────────────────
  Lexer MODE for embedded Python code
  ─────────────────────────*/

mode FUNC_PY;

FUNC_END   : '$$' -> popMode ;

DEF      : 'def';
IMPORT   : 'import';
AS       : 'as';
PASS     : 'pass';
TRUE_KW  : 'True';
FALSE_KW : 'False';
NONE_KW  : 'None';

COLON    : ':' ;
PLUS     : '+' ; MINUS : '-' ;
STAR     : '*' ; DIV   : '/' ;
ASSIGN   : '=' ;
LPAREN   : '(' ; RPAREN : ')' ;
COMMA    : ',' ;
NEWLINE  : '\r'? '\n' ;

NUMBER   : '-'? Digit+ ('.' Digit+)? ;
ID       : [a-zA-Z_][a-zA-Z0-9_]* ;

WS_PY    : [ \t\r]+         -> skip ;
LINE_COMMENT_PY
         : '#' ~[\r\n]*     -> skip ;
