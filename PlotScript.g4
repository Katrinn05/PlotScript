grammar PlotScript;

/*──────────────────────────
  Parser rules – PlotScript
  ──────────────────────────*/

program          : plotDefinition* exportStatement? EOF ;

plotDefinition   : plotName plotBlock ;
plotName         : Identifier ;

plotBlock        : '{' plotStatement* '}' ;
plotStatement    : plotFunctionIdentifier ':' expression ';' ;

plotFunctionIdentifier
                 : 'axis1' | 'axis2' | 'color' | 'output' ;

expression       : value | list | functionCall ;
value            : String | Number | Identifier ;
list             : '[' (value (',' value)*)? ']' ;

functionCall
    : arrangeLikeFunction '(' rangeArgs ')'                 
    | stringLikeFunction  '(' String ')'                    
    | 'func' '(' languageDecl ',' functionBody ')'          
    ;

arrangeLikeFunction : 'arrange' | 'axis-scale' ;
stringLikeFunction  : 'input' ;

rangeArgs
    : 'first' '(' Number ')' ',' 'last' '(' Number ')'                
    | 'first' '(' Number ')' ',' 'last' '(' Number ')' ','            
      'step'  '(' Number ')'                                          
    ;

/*─────────────
  Embedded code
  ─────────────*/

languageDecl : 'language' '(' ( CPP | PY ) ')' ;

functionBody  : FUNC_START funcStatement* FUNC_END ;

funcStatement
    : funcDeclaration ';'        
    | assignment ';'             
    | returnStmt ';'             
    | controlStructure          
    ;

typeSpecifier
    : TYPE_INT
    | TYPE_DOUBLE
    | TYPE_BOOL
    | TYPE_VOID
    ;

funcDeclaration
    : typeSpecifier Identifier LPAREN paramList? RPAREN compoundBlock
    ;

paramList
    : parameter (',' parameter)*
    ;
parameter
    : typeSpecifier Identifier
    ;

assignment     : Identifier ASSIGN expr ;
returnStmt     : RETURN expr? ;
controlStructure
    : IF LPAREN expr RPAREN compoundBlock
      (ELSE compoundBlock)?
    | FOR LPAREN assignment expr SEMI assignment RPAREN compoundBlock
    ;

compoundBlock  : LBRACE funcStatement* RBRACE ;

expr
    : expr (PLUS | MINUS) expr
    | expr (STAR | DIV)  expr
    | LPAREN expr RPAREN
    | Identifier
    | Number
    ;

/*────────────
  Export
  ────────────*/

exportStatement : 'export' '(' plotName (',' plotName)* ')' ;

/*─────────────────────────
  Lexer default (PlotScript)
  ─────────────────────────*/

CPP : 'CPP' ;
PY  : 'PY'  ;

FUNC_START : '$$' -> pushMode(FUNC) ;
FUNC_END   : '$$' -> popMode ;

String     : '\'' Character* '\'' ;
Number     : '-'? Digit+ ('.' Digit+)? ;
Identifier : Letter Character* ;

fragment Letter    : [a-zA-Z] ;
fragment Digit     : [0-9] ;
fragment Character : Letter | Digit | '_' | '-' ;

Whitespace : [ \t\r\n]+ -> skip ;

/*─────────────────────────
  Lexer MODE for embedded C++/Python code
  ─────────────────────────*/

mode FUNC;

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

DEF      : 'def';
IMPORT   : 'import';
AS       : 'as';
PASS     : 'pass';
TRUE_KW  : 'True';
FALSE_KW : 'False';
NONE_KW  : 'None';
COLON    : ':';