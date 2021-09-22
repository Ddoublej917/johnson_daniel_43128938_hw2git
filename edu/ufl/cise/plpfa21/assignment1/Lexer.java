package edu.ufl.cise.plpfa21.assignment1;

public class Lexer implements IPLPLexer {
	Token head;
	Token current;
	public Lexer (){
		head = null;
		current = null;
	}
	
	public void add (Token t) {
		Token newToken  = new Token(t.token_kind, t.token_pos, t.token_length, t.token_line, t.token_posInLine, t.token_text);
		if(head == null) {
			head = newToken;
			current = head;
			return;
		}
		else {
			current = head;
			while(current.next != null) {
				current = current.next;
			}
				
			current.next = newToken;
		}
		
	}

	@Override
	public IPLPToken nextToken() throws LexicalException {
		current = current.next;
		if (current.token_kind == Token.Kind.ERROR) {
			throw new LexicalException(current.errorMessage, current.token_line,current.token_posInLine);
		}
		else {
			return current;
		}
	}
}
