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
- Allow embedding of user-defined mathematical functions in C++,
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

| Token           | Description                                               |
|------------------|------------------------------------------------------------|
| *`yourVarName`*  | A user-defined variable that holds a plot definition     |
| `{`, `}`         | Block delimiters for plot definitions                      |
| `:`              | Assignment operator                                        |
| `[`, `]`         | List of values (arrays)                                    |
| `output`         | File path for output image keyword                         |
| `axis1`, `axis2` | Plot axes                                                  |
| `arrange(...)`   | Function to generate a numeric range                       |
| `func(...)`      | Embedded function definition                               |
| `input(...)`     | Function to load data from a file                          |
| `axis-scale(...)`| Axis scaling configuration                                 |
| `color`          | Line color for the plot                                    |
| `export(...)`    | Export one or more plots to output                         |
| `language(...)`  | Specifies the language of the embedded function (e.g., C++)|
| `first(...)`, `last(...)`, `step(...)` | Parameters for defining ranges       |
| `'...'`          | String value (for defining file path, color etc.)          |
| `$$...$$`        | Multiline Block of Embedded function  
| `CPP`            | Language identifier C++ (used inside `language(...)`)      |
| `PY`             | Language identifier Python (used inside `language(...)`)|

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
        language('CPP'),
        $$
        double f(double x){
            return x*x;
        }
        $$
    );
    output: 'output2.png';
}

my_plot3{ 
    output: 'my_output3.png';
    axis1: [input('X1.txt'), axis-scale(first(-100), last(100))];
    axis2: input('Y.txt');
    color: '#FF0000';
}

sinePy{
    axis1: arrange(first(-6.28), last(6.28), step(0.05));
    axis2: func(
        language('PY'),
        $$
        import math
        def f(x: float) -> float:
            return math.sin(x)
        $$
    );
    color: '#3366FF';
    output: 'py_sine.png';
}

export(plot1, wykres2, my_plot3, sinePy);
```
## Grammar (Extended Backus–Naur form)
[.g4 file with grammar](PlotScript.g4)

|                        |                                                                                                                                                                                                                                                                                                                                                                              |
|------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Program                | = { PlotDefinition }, [ ExportStatement ] ;                                                                                                                                                                                                                                                                                                                                  |
| PlotDefinition         | = PlotName, PlotBlock ;                                                                                                                                                                                                                                                                                                                                                      |
| PlotName               | = Identifier ;                                                                                                                                                                                                                                                                                                                                                               |
| PlotBlock              | = "{", { PlotStatement }, "}" ;                                                                                                                                                                                                                                                                                                                                              |
| PlotStatement          | = PlotFunctionIdentifier, ":", Expression, ";" ;                                                                                                                                                                                                                                                                                                                             |
| PlotFunctionIdentifier | = "axis1" \| "axis2" \| "color" \| "output";                                                                                                                                                                                                                                                                                                                                 |
| Expression             | = Value \| List \| FunctionCall ;                                                                                                                                                                                                                                                                                                                               |
| Value                  | = String \| Number \| Identifier ;                                                                                                                                                                                                                                                                                                                                           |
| List                   | = "[", [ Value, { ",", Value } ], "]" ;                                                                                                                                                                                                                                                                                                                                      |
| FunctionCall           | = ArrangeLikeFunction, "(", RangeArgs, ")" \| StringLikeFunction, "(", String, ")" \| "func", "(", LanguageDecl, ",", FunctionBody ,")";                                                                                                                                                                                                                                     |
| ArrangeLikeFunction    | = "arange" \| "axis-scale";                                                                                                                                                                                                                                                                                                                                                  |
| StringLikeFunction     | = "input";                                                                                                                                                                                                                                                                                                                                                       |
| RangeArgs              | = "first", "(", Number, ")", ",", "last", "(", Number, ")", [",", "step", "(", Number, ")"]                                                                                                                                                                                                                                                                                  |
| LanguageDecl           | = "language", "(", String, ")" ;                                                                                                                                                                                                                                                                                                                                             |
| FunctionBody           | = MultiLineCode ; // Embedded C++ function body                                                                                                                                                                                                                                                                                                                              |
| ExportStatement        | = "export", "(", PlotName, { ",", PlotName }, ")", ";" ;                                                                                                                                                                                                                                                                                                                     |
| CPP | = "CPP" |
| PY | = "PY" |
| Identifier             | = Letter, {Character} ;                                                                                                                                                                                                                                                                                                                                                      |
| Number                 | = ["-"], Digit, { Digit }, [ ".", Digit, { Digit } ] ;                                                                                                                                                                                                                                                                                                                       |
| String                 | = "'", { Character }, "'" ;                                                                                                                                                                                                                                                                                                                                                  |
| Digit                  | = "0" \| "1" \| "2" \| "3" \| "4" \| "5" \| "6" \| "7" \| "8" \| "9" ;                                                                                                                                                                                                                                                                                                       |
| Letter                 | = "A" \| "B" \| "C" \| "D" \| "E" \| "F" \| "G" \| "H" \| "I" \| "J" \| "K" \| "L" \| "M" \| "N" \| "O" \| "P" \| "Q" \| "R" \| "S" \| "T" \| "U" \| "V" \| "W" \| "X" \| "Y" \| "Z" \| "a" \| "b" \| "c" \| "d" \| "e" \| "f" \| "g" \| "h" \| "i" \| "j" \| "k" \| "l" \| "m" \| "n" \| "o" \| "p" \| "q" \| "r" \| "s" \| "t" \| "u" \| "v" \| "w" \| "x" \| "y" \| "z" ; |
| Character              | = Letter \| Digit \| "_" \| "-" ;                                                                                                                                                                                                                                                                                                                                                   |
| Symbol                 | = "[" \| "]" \| "{" \| "}" \| "(" \| ")" \| "<" \| ">" \| "'" \| '"' \| "=" \| "\|" \| "." \| "," \| ";" \| "-" \| "+" \| "*" \| "\" \| "%" \| "&" \| "?" \| "\n" \| "\t" \| "\r" \| "\f" \| "\b" ;                                                                                                                                                                          |
| MultiLineCode          | = "$$", {Character \| Sybol}, "$$";                                                                                                                                                                                                                                                                                                                                          |                                                                                                                                                                                                                                                                                                 |
