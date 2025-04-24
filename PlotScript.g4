grammar PlotScript;
import PlotScriptLexer;
// options{
//   tokenVocab=PlotScriptLexer;
// }

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
    | 'func' '(' functionBlock ')'          
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

functionBlock  : FUNC_START funcDeclaration* FUNC_END ;

funcDeclaration
    : typeSpecifier Identifier LPAREN paramList? RPAREN compoundBlock
    ;

funcStatement
    : assignment ';'             
    | returnStmt ';'             
    | controlStructure          
    ;

typeSpecifier
    : TYPE_INT
    | TYPE_DOUBLE
    | TYPE_BOOL
    | TYPE_VOID
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
    | FOR LPAREN assignment? SEMI expr? SEMI assignment? RPAREN compoundBlock
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

