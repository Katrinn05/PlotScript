# PlotScript – Interpreter for a Custom Plotting Language

## Student Information
* Katsiaryna Naumovich
* Maksim Dziatkou
* Anton Perakhod

## Contact
* naumovich@student.agh.edu.pl
* mdziatkou@student.agh.edu.pl
* aperakhod@student.agh.edu.pl

## Project Description

### General Goals
The aim of this project is to develop an interpreter for a custom-designed programming language called **PlotScript**. The language is intended for easy generation of 2D plots from both static and dynamic data sources. PlotScript supports mathematical functions, data import from external files, and exporting charts to image formats.

### Type of Translator
The program will be implemented as an **interpreter**, which analyzes and executes PlotScript code at runtime.

### Expected Output of the Program
The PlotScript interpreter will:
- Generate 2D charts from arrays or function expressions,
- Import data from external files,
- Allow embedding of user-defined mathematical functions in C,
- Export plots to image files (e.g., PNG),
- Support grouped exporting of multiple plots,
- Provide simple customization options (e.g., axis scale, color).

### Implementation Language
The interpreter will be implemented in **C++**, using the following tools and libraries:
- `to be determined` for rendering plots / images,
- `ANTLR 4` for lexer and parser generation,
- `Standard C++ STL` for data handling and execution logic.

### Scanner/Parser Implementation
The lexical and syntactic analysis will be done using **ANTLR 4**, a powerful parser generator that supports C++ as a target language.

ANTLR-generated C++ code will be integrated into the interpreter runtime.

## Token Overview

[antlr4 file with lekser](PlotScriptLexer.g4)

### PlotScript tokens

| Token               | Opis                                     |
|---------------------|------------------------------------------|
| `TOKEN_PLOT_LBRACKET`     | `'{'`|
| `TOKEN_PLOT_RBRACKET`     | `'}'`|
| `TOKEN_LIST_LBRACKET`     | `'['`|
| `TOKEN_LIST_RBRACKET`     | `']'`|
| `TOKEN_FUNC_CALL_LBRACKET`|`'('`|
| `TOKEN_FUNC_CALL_RBRACKET`| `')'`|
| `TOKEN_BLOCK_DELIMITER`   | `';'`|
| `TOKEN_VALUE_DELIMITER`   | `','`|
| `TOKEN_ASSIGN`            | `':'`|
| `TOKEN_AXIS1`             | `'axis1', 'x', 'X'`|
| `TOKEN_AXIS2`             | `'axis2', 'y', 'Y'`|
| `TOKEN_COLOR`             | `'color'`|
| `TOKEN_OUTPUT`            | `'output'`|
| `TOKEN_ARRANGE`           | `'arrange'`|
| `TOKEN_INPUT`             | `'input'`| 
| `TOKEN_AXIS1_SCALE`       | `'axis1-scale', 'x-scale', 'X-scale'`|
| `TOKEN_AXIS2_SCALE`       | `'axis2-scale', 'y-scale', 'Y-scale'`|
| `TOKEN_FUNC`              | `'func'`|
| `TOKEN_FIRST`             | `'first'`|
| `TOKEN_LAST`              | `'last'`|
| `TOKEN_STEP`              | `'step'`|
| `TOKEN_EXPORT`            | `'export'`|
| `TOKEN_C_FUNC_START`      | `'$CPP$'`|
| `TOKEN_C_FUNC_END`        | `'$$'`|
| `STRING`                  | `'\'' (Character, SpecialChar)* '\''`|
| `NUMBER`                  | `'-'? Digit+ ('.' Digit+)? 'f'?`|
| `ID`                      | `'[a-zA-Z][a-zA-Z0-9_-]*'`|
| `Character (fragment)`    |  `'Letter, Digit, '_', '-''` |
| `SpecialChar (fragment)`  | `[,.!@#$%^&()+={}'"~]`|


### Embedded C function tokens

| Token                 | Opis                        |
|-----------------------|-----------------------------|
| `C_TOKEN_TYPE_INT`    | `'int'`                     |
| `C_TOKEN_TYPE_DOUBLE` | `'double' \| 'float'`       |
| `C_TOKEN_TYPE_BOOL`   | `'bool'`                    |
| `C_TOKEN_TYPE_VOID`   | `'void'`                    |
| `C_TOKEN_PLUS`        | `'+'`                       |
| `C_TOKEN_MINUS`       | `'-'`                       |
| `C_TOKEN_STAR`        | `'*'`                       |
| `C_TOKEN_DIV`         | `'/'`                       |
| `C_TOKEN_ASSIGN`      | `'='`                       |
| `C_TOKEN_COMMA`       | `','`                       |
| `C_TOKEN_SEMI`        | `';'`                       |
| `C_TOKEN_LPAREN`      | `'('`                       |
| `C_TOKEN_LBRACE`      | `'{'`                       |
| `C_TOKEN_RPAREN`      | `')'`                       |
| `C_TOKEN_RBRACE`      | `'}'`                       |
| `C_TOKEN_IF`          | `'if'`                      |
| `C_TOKEN_ELSE`        | `'else'`                    |
| `C_TOKEN_FOR`         | `'for'`                     |
| `C_TOKEN_WHILE`       | `'while'`                   |
| `C_TOKEN_DO`          | `'do'`                      |
| `C_TOKEN_RETURN`      | `'return'`                  |
| `C_TOKEN_AND`         | `'&&'`                      |
| `C_TOKEN_OR`          | `'\|\|'`                    |
| `C_TOKEN_NOT`         | `'!'`                       |
| `C_TOKEN_GT`          | `'>'`                       |
| `C_TOKEN_LT`          | `'<'`                       |
| `C_TOKEN_GTE`         | `'>='`                      |
| `C_TOKEN_LTE`         | `'<='`                      |
| `C_TOKEN_EQ`          | `'=='`                      |
| `C_TOKEN_NEQ`         | `'!='`                      |
| `C_TOKEN_TRUE_KW`     | `'true'`                    |
| `C_TOKEN_FALSE_KW`    | `'false'`                   |
| `C_TOKEN_SQRT`        | `'sqrt'`                    |
| `C_TOKEN_SIN`         | `'sin'`                     |
| `C_TOKEN_COS`         | `'cos'`                     |
| `C_TOKEN_TAN`         | `'tan'`                     |
| `C_TOKEN_LOG`         | `'log'`                     |
| `C_TOKEN_EXP`         | `'exp'`                     |
| `C_NUMBER`            | `'-'? Digit+ ('.' Digit+)?` |
| `C_ID`                | `[a-zA-Z_][a-zA-Z0-9_]*`    |


## Example PlotScript Code

```plotscript
plot1{ 
    axis1: [1,2,3,4,5];
    axis2: [1,4,9,16,25];
    output: 'output1.png';
}

wykres2{
    axis1: arrange(first(0), last(10), step(0.5f)); 
    axis2: func(
        $CPP$
        double f(double x){
            return x*x;
        }
        $$
    );
    output: 'output2.png';
}

my_plot3{ 
    output: 'my_output3.png';
    axis1: input('X1.txt');
    axis1-scale: arrange(first(-100), last(100));
    axis2: input('Y.txt');
    color: '#FF0000';
}

ex4{
    axis1: arrange(first(0), last(10), step(0.5f)); 
    axis2: func(
        $CPP$
        double f(double x){
            int b = x * 2 + 6;
            return b*x/1.5;
        }
        $$
    );
    output: 'output2.png';
}

export(plot1, wykres2, my_plot3)
```
## Grammar
[antlr4 file with grammar](PlotScriptParser.g4)
