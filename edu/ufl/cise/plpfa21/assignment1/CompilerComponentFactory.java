package edu.ufl.cise.plpfa21.assignment1;

import edu.ufl.cise.plpfa21.assignment2.IPLPParser;
import edu.ufl.cise.plpfa21.assignment2.Parser;

public class CompilerComponentFactory{
	
	public static IPLPParser getParser(String input) {
	   	 //Implement this in Assignment 2
	   	 //Your parser will create a lexer.
		IPLPLexer inputLexer = getLexer(input);
		Parser outputParser = null;
		try {
			outputParser = new Parser(inputLexer);
		} catch (LexicalException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return outputParser;
		}

	
	public static IPLPLexer getLexer(String input) {
		Lexer result;
		char[] chars; //holds characters with 0 at end
		char EOFchar = 0;
		int startPos = 0;
		int startPosInLine = 0;
		int startLine = 1;
		int pos = 0; // position in char array. Starts at zero
		int line = 1; // line number of token in source. Starts at 1
		int posInLine = 0; // position in line of source. Starts at 1
		String txt = ""; //temporary string to hold text of tokens
		int numChars = input.length();
		chars = new char[numChars + 1]; 
		System.arraycopy(input.toCharArray(), 0, chars, 0, numChars);
		chars[numChars] = EOFchar;
		boolean errorflag = false;
		
		result = new Lexer();
		result.head = new Token(Token.Kind.SEMI, 999, 999, 999, 999, "DUMMYHEAD");
		enum State {START, HAVE_EQUAL, DIGITS, IDENT_PART, AND, OR, STRING_LITERAL, COMMENT}
		
		State state = State.START;
		
		while(pos < chars.length) {
			char ch = chars[pos]; //get current char
			switch (state) {
				case START-> {
					txt = "";
					startLine = line;
					startPos = pos;
					startPosInLine = posInLine;
					errorflag = false;
					switch (ch) {
						case ' ', '\t'-> {
							pos++;
							posInLine++;
						}
						case '\n'-> {
							pos++;
							line++;
							posInLine = 0;
						}
						case '\r'-> {
							pos++;
							posInLine = 0;
						}
						case '('-> {
							txt = "(";
							result.add(new Token(Token.Kind.LPAREN, startPos, 1, startLine, startPosInLine, txt));
							result.current = result.current.next;
							pos++;
							posInLine++;
						}
						case ')'-> {
							txt = ")";
							result.add(new Token(Token.Kind.RPAREN, startPos, 1, startLine, startPosInLine, txt));
							result.current = result.current.next;
							pos++;
							posInLine++;
						}
						case '['-> {
							txt = "[";
							result.add(new Token(Token.Kind.LSQUARE, startPos, 1, startLine, startPosInLine, txt));
							result.current = result.current.next;
							pos++;
							posInLine++;
						}
						case ']'-> {
							txt = "]";
							result.add(new Token(Token.Kind.RSQUARE, startPos, 1, startLine, startPosInLine, txt));
							result.current = result.current.next;
							pos++;
							posInLine++;
						}
						case '+'-> {
							txt = "+";
							result.add(new Token(Token.Kind.PLUS, startPos, 1, startLine, startPosInLine, txt));
							result.current = result.current.next;
							pos++;
							posInLine++;
						}
						case '-'-> {
							txt = "-";
							result.add(new Token(Token.Kind.MINUS, startPos, 1, startLine, startPosInLine, txt));
							result.current = result.current.next;
							pos++;
							posInLine++;
						}
						case '/'-> {
							switch (chars[pos+1]) {
								case '*'-> {
									txt = txt + ch + chars[pos+1];
									pos+=2;
									posInLine+=2;
									state = State.COMMENT;
								}
								default -> {
									txt = "/";
									result.add(new Token(Token.Kind.DIV, startPos, 1, startLine, startPosInLine, txt));
									result.current = result.current.next;
									pos++;
									posInLine++;
								}
							}
							
						}
						case '*'-> {
							txt = "*";
							result.add(new Token(Token.Kind.TIMES, startPos, 1, startLine, startPosInLine, txt));
							result.current = result.current.next;
							pos++;
							posInLine++;
						}
						case '<'-> {
							txt = "<";
							result.add(new Token(Token.Kind.LT, startPos, 1, startLine, startPosInLine, txt));
							result.current = result.current.next;
							pos++;
							posInLine++;
						}
						case '>'-> {
							txt = ">";
							result.add(new Token(Token.Kind.GT, startPos, 1, startLine, startPosInLine, txt));
							result.current = result.current.next;
							pos++;
							posInLine++;
						}
						case ':'-> {
							txt = ":";
							result.add(new Token(Token.Kind.COLON, startPos, 1, startLine, startPosInLine, txt));
							result.current = result.current.next;
							pos++;
							posInLine++;
						}
						case ';'-> {
							txt = ";";
							result.add(new Token(Token.Kind.SEMI, startPos, 1, startLine, startPosInLine, txt));
							result.current = result.current.next;
							pos++;
							posInLine++;
						}
						case ','-> {
							txt = ",";
							result.add(new Token(Token.Kind.COMMA, startPos, 1, startLine, startPosInLine, txt));
							result.current = result.current.next;
							pos++;
							posInLine++;
						}
						case '&'-> {
							txt = "&";
							pos++;
							posInLine++;
							state = State.AND;
						}
						case '|'-> {
							txt = "|";
							pos++;
							posInLine++;
							state = State.OR;
						}
						case '0'-> {
							txt = "0";
							result.add(new Token(Token.Kind.INT_LITERAL, startPos, 1, startLine, startPosInLine, txt));
							result.current = result.current.next;
							pos++;
							posInLine++;
						}
						case'=' -> {
							txt = "=";
							pos++;  
							posInLine++;  
							state = State.HAVE_EQUAL;
						}
						case'!' -> {
							if(chars[pos+1] == '=') {
								txt = "!";
								pos++;  
								posInLine++;  
								state = State.HAVE_EQUAL;
							}
							
							else {
								txt = "!";
								result.add(new Token(Token.Kind.BANG, startPos, 1, startLine, startPosInLine, txt));
								result.current = result.current.next;
								pos++;
								posInLine++;
							}
						}
						case '\"' -> {
							txt = txt + ch;
							pos++;
							posInLine++;
							state = State.STRING_LITERAL;
						}
						case '\'' -> {
							txt = txt + ch;
							pos++;
							posInLine++;
							state = State.STRING_LITERAL;
						}
						case'1', '2', '3', '4', '5', '6', '7', '8', '9' -> {
							txt = "" + ch;
							pos++;
							posInLine++;
							state = State.DIGITS;
						}
						default -> {
							if (Character.isJavaIdentifierStart(ch)) {
								txt = txt + ch;
								pos++;
								posInLine++;
								state = State.IDENT_PART;
								
							}
							else {
								if(ch != EOFchar) {
									txt = "" + ch;
									result.add(new Token(Token.Kind.ERROR, startPos, 1, startLine, startPosInLine, txt));
									result.current = result.current.next;
									result.current.errorMessage = ch + " is an unrecognized character for this langauge";
								}
								pos++;
							}
						}
					}	
				}
				case HAVE_EQUAL-> {
					int equalsPos = pos;
					switch (ch) {
						case '=' -> {
							if(txt.equals("!")) {
								txt = "!=";
								result.add(new Token(Token.Kind.NOT_EQUALS, equalsPos, 2, startLine, startPosInLine, txt));
								result.current = result.current.next;
								pos++;
								posInLine++;
								txt = "";
								state = State.START;
							}
							else if(txt.equals("=")) {
								txt = "==";
								result.add(new Token(Token.Kind.EQUALS, equalsPos, 2, startLine, startPosInLine, txt));
								result.current = result.current.next;
								pos++;
								posInLine++;
								txt = "";
								state = State.START;
							}
						}
						default -> {
							txt = "=";
							result.add(new Token(Token.Kind.ASSIGN, equalsPos, 1, startLine, startPosInLine, txt));
							result.current = result.current.next;
							pos++;
							posInLine++;
							txt = "";
							state = State.START;
							
						}
					}
					
					
				}
				
				case COMMENT-> {
					switch (ch) {
						case '*'-> {
							if(chars[pos+1] == '/') {
								pos+=2;
								posInLine+=2;
								txt="";
								state = State.START;
							}
							else {
								pos++;
								posInLine++;
							}
						}
						case '\n'-> {
							pos++;
							line++;
							posInLine = 0;
						}
						case '\r'-> {
							pos++;
							posInLine = 0;
						}
						default -> {
							pos++;
							posInLine++;
						}
					}
				}
				/**abd*/
				
				case STRING_LITERAL-> {
					switch (ch) {
						case '\n'-> {
							pos++;
							line++;
							posInLine = 0;
						}
						case '\r'-> {
							pos++;
							posInLine = 0;
						}
						case '\\' -> {
							if (chars[pos+1] == 'b' || chars[pos+1] == 't' || chars[pos+1] == 'r' || chars[pos+1] == 'f' || chars[pos+1] == '\'' || chars[pos+1] == '\\') {
								txt = txt + ch + chars[pos+1];
								pos+=2;
								posInLine+=2;
							}
							else if(chars[pos+1] == 'n') {
								txt = txt + ch + chars[pos+1];
								pos+=2;
								line++;
								posInLine = 0;
							}
							else {
								txt = txt + ch + chars[pos+1];
								pos+=2;
								posInLine+=2;
								errorflag = true;
							}
							
						}
						case '\'' -> {
							txt = txt + ch;
							if(txt.equals("\'\'")) {
								result.add(new Token(Token.Kind.ERROR, startPos, pos - startPos, startLine, startPosInLine, txt));
								result.current = result.current.next;
								result.current.errorMessage = "Empty string literal (\'\')";
								txt = "";
								pos++;
								posInLine++;
								state = State.START;
							}
							else if(errorflag == true) {
								result.add(new Token(Token.Kind.ERROR, startPos, pos - startPos, startLine, startPosInLine, txt));
								result.current = result.current.next;
								result.current.errorMessage = "Invalid string literal including an invalid escape sequence";
								txt = "";
								pos++;
								posInLine++;
								state = State.START;
							}
							else if(txt.charAt(0) == '\'')  {
								result.add(new Token(Token.Kind.STRING_LITERAL, startPos, pos - startPos, startLine, startPosInLine, txt));
								result.current = result.current.next;
								txt = "";
								pos++;
								posInLine++;
								state = State.START;
							}
							else {
								pos++;
								posInLine++;
							}
						}
						case '\"' -> {
							txt = txt + ch;
							if(txt.equals("\"\"")) {
								result.add(new Token(Token.Kind.ERROR, startPos, pos - startPos, startLine, startPosInLine, txt));
								result.current = result.current.next;
								result.current.errorMessage = "Empty string literal (\"\")";
								txt = "";
								pos++;
								posInLine++;
								state = State.START;
							}
							else if(errorflag == true) {
								result.add(new Token(Token.Kind.ERROR, startPos, pos - startPos, startLine, startPosInLine, txt));
								result.current = result.current.next;
								result.current.errorMessage = "Invalid string literal including an invalid escape sequence";
								txt = "";
								pos++;
								posInLine++;
								state = State.START;
							}
							else if(txt.charAt(0) == '\"')  {
								result.add(new Token(Token.Kind.STRING_LITERAL, startPos, pos - startPos, startLine, startPosInLine, txt));
								result.current = result.current.next;
								txt = "";
								pos++;
								posInLine++;
								state = State.START;
							}
							else {
								pos++;
								posInLine++;
							}
							
						}
						default -> {
							txt = txt + ch;
							pos++;
							posInLine++;
						}
					}
				}
				
				case AND-> {
					int andPos = pos;
					switch (ch) {
						case '&' -> {
							txt = "&&";
							result.add(new Token(Token.Kind.AND, andPos, 2, startLine, startPosInLine, txt));
							result.current = result.current.next;
							pos++;
							posInLine++;
							txt = "";
							state = State.START;
						}
						default -> {
							txt = "" + ch;
							result.add(new Token(Token.Kind.ERROR, andPos, 1, startLine, startPosInLine, txt));
							result.current = result.current.next;
							result.current.errorMessage = ch + " is an unrecognized character for this langauge";
							txt = "";
							state = State.START;
							
						}
					}
				}
				
				case OR-> {
					int orPos = pos;
					switch (ch) {
						case '|' -> {
							txt = "||";
							result.add(new Token(Token.Kind.OR, orPos, 2, startLine, startPosInLine, txt));
							result.current = result.current.next;
							pos++;
							posInLine++;
							txt = "";
							state = State.START;
						}
						default -> {
							txt = "" + ch;
							result.add(new Token(Token.Kind.ERROR, orPos, 1, startLine, startPosInLine, txt));
							result.current = result.current.next;
							result.current.errorMessage = ch + " is an unrecognized character for this langauge";
							txt = "";
							state = State.START;
							
						}
					}
				}

				case DIGITS-> {
					switch (ch) {
						case '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> {
							txt = txt + ch;
							pos++;
							posInLine++;
						}
						default -> {
							try {
							    Integer.parseInt(txt);
							    result.add(new Token(Token.Kind.INT_LITERAL, startPos, pos - startPos, startLine, startPosInLine, txt));
								result.current = result.current.next;
								txt = "";
								state = State.START; 
							}
							catch (NumberFormatException e) {
								result.add(new Token(Token.Kind.ERROR, startPos, pos - startPos, startLine, startPosInLine, txt));
								txt = "";
								result.current = result.current.next;
								result.current.errorMessage = ch + " is an unrecognized character for this langauge";
								state = State.START;
							}
							 		
						}
					}
				}
					
				case IDENT_PART-> {
					switch (ch) {
					case 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '$', '_' -> {
						txt = txt + ch;
						if(txt.equals("VAR")) {
							result.add(new Token(Token.Kind.KW_VAR, startPos, pos - startPos, startLine, startPosInLine, txt));
							result.current = result.current.next;
							txt = "";
							state = State.START; 
						}
						else if(txt.equals("VAL")) {
							result.add(new Token(Token.Kind.KW_VAL, startPos, pos - startPos, startLine, startPosInLine, txt));
							result.current = result.current.next;
							txt = "";
							state = State.START; 
						}
						else if(txt.equals("FUN")) {
							result.add(new Token(Token.Kind.KW_FUN, startPos, pos - startPos, startLine, startPosInLine, txt));
							result.current = result.current.next;
							txt = "";
							state = State.START; 
						}
						else if(txt.equals("DO")) {
							result.add(new Token(Token.Kind.KW_DO, startPos, pos - startPos, startLine, startPosInLine, txt));
							result.current = result.current.next;
							txt = "";
							state = State.START; 
						}
						else if(txt.equals("END")) {
							result.add(new Token(Token.Kind.KW_END, startPos, pos - startPos, startLine, startPosInLine, txt));
							result.current = result.current.next;
							txt = "";
							state = State.START; 
						}
						else if(txt.equals("LET")) {
							result.add(new Token(Token.Kind.KW_LET, startPos, pos - startPos, startLine, startPosInLine, txt));
							result.current = result.current.next;
							txt = "";
							state = State.START; 
						}
						else if(txt.equals("SWITCH")) {
							result.add(new Token(Token.Kind.KW_SWITCH, startPos, pos - startPos, startLine, startPosInLine, txt));
							result.current = result.current.next;
							txt = "";
							state = State.START; 
						}
						else if(txt.equals("CASE")) {
							result.add(new Token(Token.Kind.KW_CASE, startPos, pos - startPos, startLine, startPosInLine, txt));
							result.current = result.current.next;
							txt = "";
							state = State.START; 
						}
						else if(txt.equals("DEFAULT")) {
							result.add(new Token(Token.Kind.KW_DEFAULT, startPos, pos - startPos, startLine, startPosInLine, txt));
							result.current = result.current.next;
							txt = "";
							state = State.START; 
						}
						else if(txt.equals("IF")) {
							result.add(new Token(Token.Kind.KW_IF, startPos, pos - startPos, startLine, startPosInLine, txt));
							result.current = result.current.next;
							txt = "";
							state = State.START; 
						}
						else if(txt.equals("ELSE")) {
							result.add(new Token(Token.Kind.KW_ELSE, startPos, pos - startPos, startLine, startPosInLine, txt));
							result.current = result.current.next;
							txt = "";
							state = State.START; 
						}
						else if(txt.equals("WHILE")) {
							result.add(new Token(Token.Kind.KW_WHILE, startPos, pos - startPos, startLine, startPosInLine, txt));
							result.current = result.current.next;
							txt = "";
							state = State.START; 
						}
						else if(txt.equals("WHILE")) {
							result.add(new Token(Token.Kind.KW_WHILE, startPos, pos - startPos, startLine, startPosInLine, txt));
							result.current = result.current.next;
							txt = "";
							state = State.START; 
						}
						else if(txt.equals("RETURN")) {
							result.add(new Token(Token.Kind.KW_RETURN, startPos, pos - startPos, startLine, startPosInLine, txt));
							result.current = result.current.next;
							txt = "";
							state = State.START; 
						}
						else if(txt.equals("NIL")) {
							result.add(new Token(Token.Kind.KW_NIL, startPos, pos - startPos, startLine, startPosInLine, txt));
							result.current = result.current.next;
							txt = "";
							state = State.START; 
						}
						else if(txt.equals("TRUE")) {
							result.add(new Token(Token.Kind.KW_TRUE, startPos, pos - startPos, startLine, startPosInLine, txt));
							result.current = result.current.next;
							txt = "";
							state = State.START; 
						}
						else if(txt.equals("FALSE")) {
							result.add(new Token(Token.Kind.KW_FALSE, startPos, pos - startPos, startLine, startPosInLine, txt));
							result.current = result.current.next;
							txt = "";
							state = State.START; 
						}
						else if(txt.equals("INT")) {
							result.add(new Token(Token.Kind.KW_INT, startPos, pos - startPos, startLine, startPosInLine, txt));
							result.current = result.current.next;
							txt = "";
							state = State.START; 
						}
						else if(txt.equals("STRING")) {
							result.add(new Token(Token.Kind.KW_STRING, startPos, pos - startPos, startLine, startPosInLine, txt));
							result.current = result.current.next;
							txt = "";
							state = State.START; 
						}
						else if(txt.equals("BOOLEAN")) {
							result.add(new Token(Token.Kind.KW_BOOLEAN, startPos, pos - startPos, startLine, startPosInLine, txt));
							result.current = result.current.next;
							txt = "";
							state = State.START; 
						}
						else if(txt.equals("LIST")) {
							result.add(new Token(Token.Kind.KW_LIST, startPos, pos - startPos, startLine, startPosInLine, txt));
							result.current = result.current.next;
							txt = "";
							state = State.START; 
						}

						pos++;
						posInLine++;

					}
					
					default -> {
						result.add(new Token(Token.Kind.IDENTIFIER, startPos, pos - startPos, startLine, startPosInLine, txt));
						result.current = result.current.next;
						txt = "";
						state = State.START; 
						//DO NOT INCREMENT pos  
						
					}
				}
				}
			}
		}
		
		//add EOF token
		result.add(new Token(Token.Kind.EOF, pos, 0, startLine, startPosInLine, ""));
		result.current = result.head;
		
		
		return result;

	}





}