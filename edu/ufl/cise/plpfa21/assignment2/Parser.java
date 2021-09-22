package edu.ufl.cise.plpfa21.assignment2;

import edu.ufl.cise.plpfa21.assignment1.IPLPLexer;
import edu.ufl.cise.plpfa21.assignment1.IPLPToken;
import edu.ufl.cise.plpfa21.assignment1.LexicalException;

//Step 1: compute predict sets for each production
//Step 2: rewrite grammar so that each non-terminal is on the left side of only 1 production

public class Parser implements IPLPParser {
	IPLPLexer inputLexer;
	int line;
	int charPositionInLine;
	String errorMessage;
	IPLPToken t; // next token
	
	public Parser (IPLPLexer input) throws LexicalException{
		this.inputLexer = input;
		t = inputLexer.nextToken();
		line = t.getLine();
		charPositionInLine = t.getCharPositionInLine();
		errorMessage = t.getText();
	}
	
	boolean isKind (IPLPToken t, IPLPToken.Kind kind) {
		if(t.getKind().equals(kind))
			return true;
		else
			return false;
	}
	
	boolean isExpStart() throws LexicalException {
		switch(t.getKind()) {
			case KW_NIL -> {
				return true;
			}
			case KW_TRUE -> {
				return true;
			}
			case KW_FALSE -> {
				return true;
			}
			case INT_LITERAL -> {
				return true;
			}
			case STRING_LITERAL -> {
				return true;
			}
			case LPAREN -> {
				return true;
			}
			case IDENTIFIER -> {
				return true;
			}
			default -> {
				return false;
			}
			
		}
	}
	
	void consume() throws LexicalException {
		t = inputLexer.nextToken();
	}
	
	void match(IPLPToken.Kind kind) throws LexicalException, SyntaxException {
	       if(isKind(t, kind)){
			   consume();
	       }
	       else {
	    	   throw new SyntaxException(t.getText(), t.getLine(), t.getCharPositionInLine());
	       }  
	}
	
	void program() throws LexicalException, SyntaxException {
		//ADD LOOPING STRUCTURES FOR DECLARATIONS
		while (isKind(t,IPLPToken.Kind.KW_FUN) || isKind(t,IPLPToken.Kind.KW_VAL) || isKind(t,IPLPToken.Kind.KW_VAR)) {
			declaration();
		}
	}
	
	void declaration() throws LexicalException, SyntaxException {
		switch(t.getKind()) {
			case KW_FUN -> {
				function();
			}
			case KW_VAR -> {
				consume();
				namedef();
				if(isKind(t, IPLPToken.Kind.ASSIGN)) {
					consume();
					expression();
				}
				match(IPLPToken.Kind.SEMI);
			}
			case KW_VAL -> {
				consume();
				namedef();
				match(IPLPToken.Kind.ASSIGN);
				expression();
				match(IPLPToken.Kind.SEMI);
			}
		
			default -> {
					throw new IllegalArgumentException("Unexpected value: " + t.getKind());
			}
		}
	}
	
	void function() throws LexicalException, SyntaxException {
		match(IPLPToken.Kind.KW_FUN);
		match(IPLPToken.Kind.IDENTIFIER);
		match(IPLPToken.Kind.LPAREN);
		if(isKind(t, IPLPToken.Kind.RPAREN)) {
			consume();
			if(isKind(t, IPLPToken.Kind.COLON)) {
				consume();
				type();
			}
			match(IPLPToken.Kind.KW_DO);
			block();
			match(IPLPToken.Kind.KW_END);
		}
		else {
			namedef();
			while(isKind(t, IPLPToken.Kind.COMMA)) {
				consume();
				namedef();
			}
			if(isKind(t, IPLPToken.Kind.RPAREN)) {
				consume();
				if(isKind(t, IPLPToken.Kind.COLON)) {
					consume();
					type();
				}
				match(IPLPToken.Kind.KW_DO);
				block();
				match(IPLPToken.Kind.KW_END);
			}
			else {
				throw new IllegalArgumentException("Unexpected value: " + t.getKind());
			}
		}
				
		
	}
	
	void block() throws LexicalException, SyntaxException {
		 //FIX LOOP STRUCTURE FOR BLOCKS TO INCLUDE EXPRESSION STATEMENTS
		while (isKind(t,IPLPToken.Kind.KW_LET) || isKind(t,IPLPToken.Kind.KW_SWITCH) || isKind(t,IPLPToken.Kind.KW_IF) || isKind(t,IPLPToken.Kind.KW_WHILE) || isKind(t,IPLPToken.Kind.KW_RETURN) || isExpStart() == true) {
			statement();
		}
	}
	
	void namedef() throws LexicalException, SyntaxException {
		if(isKind(t, IPLPToken.Kind.IDENTIFIER)) {
			consume();
			if(isKind(t, IPLPToken.Kind.COLON)) {
				consume();
				type();
			}
		}
		else {
			throw new IllegalArgumentException("Unexpected value: " + t.getKind());
		}
	}
	
	void statement() throws LexicalException, SyntaxException {
		switch(t.getKind()) {
			case KW_LET -> {
				consume();
				namedef();
				if(isKind(t, IPLPToken.Kind.ASSIGN)){
					  consume();
					  expression();
			    }
				match(IPLPToken.Kind.SEMI);
			}
			case KW_SWITCH -> {
				consume();
				expression();
				while (isKind(t,IPLPToken.Kind.KW_CASE) || isKind(t,IPLPToken.Kind.COLON)) { //FIX KLEENE STAR!!!
					consume();
					expression();
					match(IPLPToken.Kind.COLON);
					block();
				}
				match(IPLPToken.Kind.KW_DEFAULT);
				block();
				match(IPLPToken.Kind.KW_END);
			}
			case KW_IF -> {
				consume();
				expression();
				match(IPLPToken.Kind.KW_DO);
				block();
				match(IPLPToken.Kind.KW_END);
			}
			case KW_WHILE -> {
				consume();
				expression();
				match(IPLPToken.Kind.KW_DO);
				block();
				match(IPLPToken.Kind.KW_END);
			}
			case KW_RETURN -> {
				consume();
				expression();
				match(IPLPToken.Kind.SEMI);
			}
			default -> {
				expression();
				if(isKind(t, IPLPToken.Kind.ASSIGN)) {
					consume();
					expression();
					match(IPLPToken.Kind.SEMI);
				}
				else if(isKind(t, IPLPToken.Kind.SEMI)) {
					consume();
				}
				else {
					throw new IllegalArgumentException("Unexpected value: " + t.getKind());
				}
					
			}
		}
	}
	
	void expression() throws LexicalException, SyntaxException {
		logicalExpression();
	}
	
	void logicalExpression() throws LexicalException, SyntaxException {
		comparisonExpression();
		while (isKind(t,IPLPToken.Kind.AND) || isKind(t,IPLPToken.Kind.OR)) {
			consume();
			comparisonExpression();
		}
	}
	
	void comparisonExpression() throws LexicalException, SyntaxException {
		additiveExpression();
		while (isKind(t,IPLPToken.Kind.GT) || isKind(t,IPLPToken.Kind.LT) || isKind(t,IPLPToken.Kind.EQUALS) || isKind(t,IPLPToken.Kind.NOT_EQUALS)) {
			consume();
			additiveExpression();
		}
	}
	
	void additiveExpression() throws LexicalException, SyntaxException {
		multExpression();
		while (isKind(t,IPLPToken.Kind.PLUS) || isKind(t,IPLPToken.Kind.MINUS)) {
			consume();
			multExpression();
		}
	}
	
	void multExpression() throws LexicalException, SyntaxException {
		unaryExpression();
		while (isKind(t,IPLPToken.Kind.TIMES) || isKind(t,IPLPToken.Kind.DIV)) {
			consume();
			unaryExpression();
		}
	}
	
	void unaryExpression() throws LexicalException, SyntaxException {
		switch(t.getKind()) {
			case BANG -> {
				consume();
				primaryExpression();
			}
			case MINUS -> {
				consume();
				primaryExpression();
			}
			default -> primaryExpression();
		}
	}
	
	void primaryExpression() throws LexicalException, SyntaxException {
		switch(t.getKind()) {
			case KW_NIL -> {
				consume();
			}
			case KW_TRUE -> {
				consume();
			}
			case KW_FALSE -> {
				consume();
			}
			case INT_LITERAL -> {
				consume();
			}
			case STRING_LITERAL -> {
				consume();
			}
			case LPAREN -> {
				consume();
				expression();
				match(IPLPToken.Kind.RPAREN);
			}
			case IDENTIFIER -> {
				consume();
				switch(t.getKind()) {
					case LPAREN -> {
						consume();
						expression();
						while(isKind(t, IPLPToken.Kind.COMMA)) {
							match(IPLPToken.Kind.COMMA);
							expression();
						}
						match(IPLPToken.Kind.RPAREN);
					}
					case LSQUARE -> {
						consume();
						expression();
						match(IPLPToken.Kind.RSQUARE);
					}
					default -> {
						break;
					}
				}
			}
			default -> throw new IllegalArgumentException("Unexpected value: " + t.getKind());
			
		}
	}
	
	void type() throws LexicalException, SyntaxException {
		switch(t.getKind()) {
			case KW_INT -> {
				consume();
			}
			case KW_STRING -> {
				consume();				
			}
			case KW_BOOLEAN -> {
				consume();				
			}
			case KW_LIST -> {
				consume();
				switch(t.getKind()) {
					case LSQUARE -> {
						consume();
						if(isKind(t, IPLPToken.Kind.RSQUARE)) {
							consume();
						}
						else {
							type();
							match(IPLPToken.Kind.RSQUARE);
						}
						
						
					}
					default -> throw new IllegalArgumentException("Unexpected value: " + t.getKind());
				}
			}
			default -> throw new IllegalArgumentException("Unexpected value: " + t.getKind());
		}
	}

	@Override
	public void parse() throws Exception {
		program();
		return;
	}

}
