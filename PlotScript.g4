grammar PlotScript;

Program : PlotDefinition* ExportStatement? EOF ;

PlotDefinition : PlotName PlotBlock ;

PlotName : Identifier ;

PlotBlock : '{' PlotStatement* '}' ;

PlotStatement : PlotFunctionIdentifier ':' Expression ';';

PlotFunctionIdentifier : 'axis1' | 'axis2' | 'color' | 'output' ;

Expression : Value | List | FunctionCall ;

Value : String | Number | Identifier ;

List : '[' (Value (',' Value)*)? ']' ;

FunctionCall : 
    ArrangeLikeFunction '(' RangeArgs ')' |
    StringLikeFunction '(' String ')' |
    'func' '(' LanguageDecl FunctionBody ')'
;

ArrangeLikeFunction : 'arrange' | 'axis-scale' ;

StringLikeFunction : 'input' ;

RangeArgs : 
    'first' '(' Number ')' ',' 'last' '(' Number ')' |
    'first' '(' Number ')' ',' 'last' '(' Number ')' ',' 'step' '(' Number ')'
;

LanguageDecl : 
    'lang' '(' String ')' ;

FunctionBody : MultiLineCode ;

ExportStatement : 'export' '(' PlotName (',' PlotName)* ')' ;

MultiLineCode :
    '$$' .*? '$$' -> channel(HIDDEN)
;



Letter : [a-zA-Z]+ ;

Digit : [0-9]+ ;

Number : '-'? Digit ('.' Digit+)? ;

Character : Letter | Digit | '_' | '-' ;

String : '\'' Character* '\'' ;

Identifier : Letter (Character*) ;

Whitespace : [ \t\r\n]+ -> skip ;
