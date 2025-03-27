import sys
from enum import Enum, auto
from dataclasses import dataclass
from typing import List, Tuple

@dataclass
class TokenBase:
    kod: str
    opis: str

class Stage(Enum):
    START = auto()
    LICZBA = auto()
    IDENTYFIKATOR = auto()

stan = Stage.START
current_token: Tuple[TokenBase, str] = (TokenBase("", ""), "")
current_ind = 0

def skaner(source: str, tokeny: List[Tuple[TokenBase, str]]) -> int:
    global stan, current_token, current_ind

    if current_ind >= len(source):
        if stan == Stage.LICZBA:
            tokeny.append(current_token)
            stan = Stage.START
        elif stan == Stage.IDENTYFIKATOR:
            raise ValueError("Niezakończony identyfikator")
        return 0

    current_char = source[current_ind]

    if current_char.isspace():
        tokeny.append((TokenBase("WHITESPACE", "biały znak"), current_char))
        current_ind += 1
        return 1

    if stan == Stage.START:
        if current_char.isdigit():
            stan = Stage.LICZBA
            current_token = (TokenBase("LC", "liczba calkowita"), current_char)
        elif current_char == '+':
            tokeny.append((TokenBase("+", "plus"), "+"))
        elif current_char == '-':
            tokeny.append((TokenBase("-", "minus"), "-"))
        elif current_char == '*':
            tokeny.append((TokenBase("*", "mnożenie"), "*"))
        elif current_char == '/':
            tokeny.append((TokenBase("/", "dzielenie"), "/"))
        elif current_char == '(':
            tokeny.append((TokenBase("(", "lewy nawias"), "("))
        elif current_char == ')':
            tokeny.append((TokenBase(")", "prawy nawias"), ")"))
        elif current_char == '"':
            stan = Stage.IDENTYFIKATOR
            current_token = (TokenBase("ID", "identyfikator"), "")
        else:
            raise ValueError(f"Unexpected character [{current_char}] at {current_ind}")
    elif stan == Stage.LICZBA:
        if current_char.isdigit():
            current_token = (current_token[0], current_token[1] + current_char)
        else:
            tokeny.append(current_token)
            stan = Stage.START
            current_ind -= 1  
    elif stan == Stage.IDENTYFIKATOR:
        if current_char.isalnum():
            current_token = (current_token[0], current_token[1] + current_char)
        elif current_char == '"':
            tokeny.append(current_token)
            stan = Stage.START
        else:
            raise ValueError(f"Unexpected character [{current_char}] at {current_ind}")

    current_ind += 1
    return 1

def token_to_html(ttype: str, tvalue: str) -> str:
    """
    Zamienia pojedynczy token na fragment HTML z odpowiednim stylem.
    """
    style_map = {
        "WHITESPACE":  "",
        "LC":          "color: blue;",        
        "ID":          "color: black;",       
        "+":           "color: orange;",
        "-":           "color: orange;",
        "*":           "color: orange;",
        "/":           "color: orange;",
        "(":           "color: purple;",
        ")":           "color: purple;",
        "UNKNOWN":     "color: red; background-color: #fdd;"
    }
    
    if ttype == "WHITESPACE":
        return (tvalue
                .replace(" ", "&nbsp;")
                .replace("\t", "&nbsp;&nbsp;&nbsp;")
                .replace("\n", "<br/>"))
    css = style_map.get(ttype, "color: red;")
    safe_text = (
        tvalue
        .replace("&", "&amp;")
        .replace("<", "&lt;")
        .replace(">", "&gt;")
    )
    return f'<span style="{css}">{safe_text}</span>'

def generate_html(tokens: List[Tuple[TokenBase, str]]) -> str:
    """
    Generuje kompletny dokument HTML na podstawie listy tokenów.
    """
    html_parts = []
    for token_base, tvalue in tokens:
        html_parts.append(token_to_html(token_base.kod, tvalue))
    
    result_html = """\
<!DOCTYPE html>
<html lang="pl">
<head>
    <meta charset="utf-8">
    <title>Podświetlenie składni</title>
    <style>
        body {
            font-family: monospace;
            white-space: pre-wrap;
        }
    </style>
</head>
<body>
"""
    result_html += "".join(html_parts)
    result_html += """
</body>
</html>
"""
    return result_html

def main():
    global stan, current_ind
    stan = Stage.START
    current_ind = 0

    source = input()
    tokeny: List[Tuple[TokenBase, str]] = []

    while True:
        try:
            result = skaner(source, tokeny)
            if result == 0:
                break
        except ValueError as e:
            print(f"Error: {e}", file=sys.stderr)
            break

    colored_html = generate_html(tokeny)
    with open("wynik.html", "w", encoding="utf-8") as f:
        f.write(colored_html)
    
    for tok in tokeny:
        print(f"{tok[0].kod}: {tok[1]} ({tok[0].opis})")

if __name__ == "__main__":
    main()
