def tokenize(expression):
    tokens = []
    i = 0 
    length = len(expression) 
    
    while i < length:
        char = expression[i]
        
        if char.isspace():
            i += 1
            continue
        
        elif char.isdigit():  
            start = i
            while i < length and expression[i].isdigit():
                i += 1
            tokens.append(("NUMBER", expression[start:i]))
            continue
        
        elif char.isalpha() or char == '_':  
            start = i
            while i < length and (expression[i].isalnum() or expression[i] == '_'):
                i += 1
            tokens.append(("ID", expression[start:i]))
            continue
        
        elif char in '+-*/()':  
            token_type = {
                '+': "PLUS",
                '-': "MINUS",
                '*': "TIMES",
                '/': "DIVIDE",
                '(': "LPAREN",
                ')': "RPAREN"
            }[char]
            tokens.append((token_type, char))
            i += 1
            continue
        
        else: 
            print(f"Nieznany token '{char}' w kolumnie {i + 1}")
            i += 1
            continue
    
    return tokens

expression = "2+3*(76+8/3)+ 3*(9-3)"
tokens = tokenize(expression)
for token in tokens:
    print(token)
