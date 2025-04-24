lexer grammar PlotScriptLexerPY;

/*─────────────────────────
  Lexer default (PlotScript)
  ─────────────────────────*/

PY : 'PY' ;

FUNC_PY_START : '$' PY '$' -> pushMode(FUNC_PY) ;

String     : '\'' Character* '\'' ;
Number     : '-'? Digit+ ('.' Digit+)? ;
Identifier : Letter Character* ;

fragment Letter    : [a-zA-Z] ;
fragment Digit     : [0-9] ;
fragment Character : Letter | Digit | '_' | '-' ;

Whitespace : [ \t\r\n]+ -> skip ;

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