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
- Allow embedding of user-defined mathematical functions in C++ and Python,
- Export plots to image files (e.g., PNG),
- Support grouped exporting of multiple plots,
- Provide simple customization options (e.g., axis scale, color).

### Implementation Language
The interpreter will be implemented in **C++**, using the following tools and libraries:
- `to be determined =(` for rendering plots / images,
- `ANTLR 4` for lexer and parser generation,
- `Standard C++ STL` for data handling and execution logic.

### Scanner/Parser Implementation
The lexical and syntactic analysis will be done using **ANTLR 4**, a powerful parser generator that supports C++ as a target language.

ANTLR-generated C++ code will be integrated into the interpreter runtime.

## Token Overview

[antlr4 file with lekser](PlotScriptLexer.g4)

### PlotScript tokens

| Token           | Opis                                               |
|------------------|------------------------------------------------------------|
| `PLOT_LBRACKET`     | `'{'`|
| `PLOT_RBRACKET`     | `'}'`|
| `LIST_LBRACKET`     | `'['`|
| `LIST_RBRACKET`     | `']'`|
| `FUNC_CALL_LBRACKET`|`'('`|
| `FUNC_CALL_RBRACKET`| `')'`|
| `BLOCK_DELIMITER`   | `';'`|
| `VALUE_DELIMITER`   | `','`|
| `ASSIGN`            | `':'`|
| `AXIS1`             | `'axis1', 'x', 'X'`|
| `AXIS2`             | `'axis2', 'y', 'Y'`|
| `COLOR`             | `'color'`|
| `OUTPUT`            | `'output'`|
| `ARRANGE`           | `'arrange'`|
| `INPUT`             | `'input'`| 
| `AXIS1_SCALE`       | `'axis1-scale', 'x-scale', 'X-scale'`|
| `AXIS2_SCALE`       | `'axis2-scale', 'y-scale', 'Y-scale'`|
| `FUNC`              | `'func'`|
| `FIRST`             | `'first'`|
| `LAST`              | `'last'`|
| `STEP`              | `'step'`|
| `EXPORT`            | `'export'`|
| `CPP_FUNC_START`    | `'$CPP$'`|
| `PY_FUNC_START`     | `'$PY$'`|
| `STRING`            | `'\'' (Character, SpecialChar)* '\''`|
| `NUMBER`            | `'-'? Digit+ ('.' Digit+)? 'f'?`|
| `ID`                | `'[a-zA-Z][a-zA-Z0-9_-]*'`|
| `Letter`            | `'[a-zA-Z]'`|
| `Digit`             | `'[0-9]'`|
| `Character`         |  `'Letter, Digit, '_', '-''` |
| `SpecialChar`       | `[,.!@#$%^&()+={}`'~]`|
| `WS`                | `'[\\t\\r\\n]+'`|
### Embedded CPP function tokens

| Token             | Opis                        |
|-------------------|-----------------------------|
| `CPP_TYPE_INT`    | `'int'`                     |
| `CPP_TYPE_DOUBLE` | `'double' \| 'float'`       |
| `CPP_TYPE_BOOL`   | `'bool'`                    |
| `CPP_TYPE_VOID`   | `'void'`                    |
| `CPP_PLUS`        | `'+'`                       |
| `CPP_MINUS`       | `'-'`                       |
| `CPP_STAR`        | `'*'`                       |
| `CPP_DIV`         | `'/'`                       |
| `CPP_ASSIGN`      | `'='`                       |
| `CPP_COMMA`       | `','`                       |
| `CPP_SEMI`        | `';'`                       |
| `CPP_LPAREN`      | `'('`                       |
| `CPP_LBRACE`      | `'{'`                       |
| `CPP_RPAREN`      | `')'`                       |
| `CPP_RBRACE`      | `'}'`                       |
| `CPP_IF`          | `'if'`                      |
| `CPP_ELSE`        | `'else'`                    |
| `CPP_FOR`         | `'for'`                     |
| `CPP_RETURN`      | `'return'`                  |
| `CPP_AND`         | `'&&'`                      |
| `CPP_OR`          | `'\||'`                      |
| `CPP_NOT`         | `'!'`                       |
| `CPP_TRUE_KW`     | `'true'`                    |
| `CPP_FALSE_KW`    | `'false'`                   |
| `CPP_NUMBER`      | `'-'? Digit+ ('.' Digit+)?` |
| `CPP_ID`          | `[a-zA-Z_][a-zA-Z0-9_]*`    |

### Embedded PY function tokens (WORK IN PROGRESS)
<!-- 
| Token              | Opis        |
|--------------------|----------------------------------------------|
| `DEF`              | `'def'`                                      |
| `IMPORT`           | `'import'`          |
| `AS`               | `'as'`             |
| `PASS`             | `'pass'`                  |
| `TRUE_KW`          | `'True'`             |
| `FALSE_KW`         | `'False'`            |
| `NONE_KW`          | `'None'`     |
| `COLON`            | `':'`       |
| `PLUS`             | `'+'`                                        |
| `MINUS`            | `'-'`                                        |
| `STAR`             | `'*'`                                        |
| `DIV`              | `'/'`                                        |
| `ASSIGN`           | `'='`                                        |
| `COMMA`            | `','`                                        |
| `LPAREN`           | `'('`                                        |
| `RPAREN`           | `')'`                                        |
| `NEWLINE`          | `'\r'? '\n'`                |
| `NUMBER`           | `'-'? Digit+ ('.' Digit+)?`                  |
| `ID`               | `[a-zA-Z_][a-zA-Z0-9_]*`                     |
| `WS_PY`            | `[ \\t\\r]+`        |
| `LINE_COMMENT_PY`  | `'#' ~[\\r\\n]*` | -->

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

// EMBEDDED PYTHON FUNCTIONS - WORK IN PROGRESS
sinePy{
    axis1: arrange(first(-6.28), last(6.28), step(0.05));
    axis2: func(
        $PY$
        import math
        def f(x: float) -> float:
            return math.sin(x)
        $$
    );
    color: '#3366FF';
    output: 'py_sine.png';
}

export(plot1, wykres2, my_plot3, sinePy)
```
## Grammar
[antlr4 file with grammar](PlotScriptParser.g4)
