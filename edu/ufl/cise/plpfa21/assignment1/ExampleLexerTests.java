package edu.ufl.cise.plpfa21.assignment1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class ExampleLexerTests implements PLPTokenKinds {

	IPLPLexer getLexer(String input) {
		return CompilerComponentFactory.getLexer(input);
	}

	@Test
	public void testsemi() throws LexicalException {
		String input = ";";
		IPLPLexer lexer = getLexer(input);
		{
			IPLPToken token = lexer.nextToken();
			Kind kind = token.getKind();
			assertEquals(kind, Kind.SEMI);
		}
	}
	@Test
	public void testneq() throws LexicalException {
		String input = "!=";
		IPLPLexer lexer = getLexer(input);
		{
			IPLPToken token = lexer.nextToken();
			Kind kind = token.getKind();
			assertEquals(kind, Kind.NOT_EQUALS);
		}
	}
	@Test
	public void testeq() throws LexicalException {
		String input = "==";
		IPLPLexer lexer = getLexer(input);
		{
			IPLPToken token = lexer.nextToken();
			Kind kind = token.getKind();
			assertEquals(kind, Kind.EQUALS);
		}
	}

	@Test
	public void testplus() throws LexicalException {
		String input = "+ ;";
		IPLPLexer lexer = getLexer(input);
		{
			IPLPToken token = lexer.nextToken();
			Kind kind = token.getKind();
			assertEquals(kind, Kind.PLUS);
			token = lexer.nextToken();
			kind = token.getKind();
			assertEquals(kind, Kind.SEMI);
		}
	}
	
	@Test
	public void testRSQUARE() throws LexicalException {
		String input = "]";
		IPLPLexer lexer = getLexer(input);
		{
			IPLPToken token = lexer.nextToken();
			Kind kind = token.getKind();
			assertEquals(kind, Kind.RSQUARE);
		}
	}
	
	@Test
	public void testand() throws LexicalException {
		String input = "&&";
		IPLPLexer lexer = getLexer(input);
		{
			IPLPToken token = lexer.nextToken();
			Kind kind = token.getKind();
			assertEquals(kind, Kind.AND);
		}
	}
	
	@Test
	public void testamp() throws LexicalException {
		String input = "&";
		IPLPLexer lexer = getLexer(input);
		assertThrows(LexicalException.class, () -> {
			@SuppressWarnings("unused")
			IPLPToken token = lexer.nextToken();
		});
	}
	
	@Test
	public void testempty() throws LexicalException {
		String input = "\"\"";
		IPLPLexer lexer = getLexer(input);
		assertThrows(LexicalException.class, () -> {
			@SuppressWarnings("unused")
			IPLPToken token = lexer.nextToken();
		});
	}
	
	@Test
	public void testor() throws LexicalException {
		String input = "||";
		IPLPLexer lexer = getLexer(input);
		{
			IPLPToken token = lexer.nextToken();
			Kind kind = token.getKind();
			assertEquals(kind, Kind.OR);
		}
	}
	
	@Test
	public void testif() throws LexicalException {
		String input = "/****/IF";
		IPLPLexer lexer = getLexer(input);
		{
			IPLPToken token = lexer.nextToken();
			Kind kind = token.getKind();
			assertEquals(kind, Kind.KW_IF);
		}
	}
	
	@Test
	public void testifBOOL() throws LexicalException {
		String input = "IF BOOLEAN";
		IPLPLexer lexer = getLexer(input);
		{
			IPLPToken token = lexer.nextToken();
			Kind kind = token.getKind();
			assertEquals(kind, Kind.KW_IF);
			token = lexer.nextToken();
			kind = token.getKind();
			assertEquals(kind, Kind.KW_BOOLEAN);
		}
	}
	
	@Test
	public void teststrlit() throws LexicalException {
		String input = "\"he\nllo\'\"";
		IPLPLexer lexer = getLexer(input);
		{
			IPLPToken token = lexer.nextToken();
			Kind kind = token.getKind();
			assertEquals(kind, Kind.STRING_LITERAL);
			int line = token.getLine();
			assertEquals(line, 1);
			String text = token.getText();
			assertEquals(text, "\"he\nllo\'\"");
			String textval = token.getStringValue();
			assertEquals(textval, "hello'");
		}
	}
	
	@Test
	public void testescapestrlit() throws LexicalException {
		String input = "\"\\r\\r\"";
		IPLPLexer lexer = getLexer(input);
		{
			IPLPToken token = lexer.nextToken();
			Kind kind = token.getKind();
			assertEquals(kind, Kind.STRING_LITERAL);
		}
	}
	
	@Test
	public void testbador() throws LexicalException {
		String input = "|";
		IPLPLexer lexer = getLexer(input);
		assertThrows(LexicalException.class, () -> {
			@SuppressWarnings("unused")
			IPLPToken token = lexer.nextToken();
		});
	}
	
	@Test
	public void test0() throws LexicalException {
		String input = """

				""";
		IPLPLexer lexer = getLexer(input);
		{
			IPLPToken token = lexer.nextToken();
			Kind kind = token.getKind();
			assertEquals(kind, Kind.EOF);
		}
	}

	@Test
	public void test1() throws LexicalException {
		String input = """
				abc
				  def
				     ghi

				""";
		IPLPLexer lexer = getLexer(input);
		{
			IPLPToken token = lexer.nextToken();
			Kind kind = token.getKind();
			assertEquals(kind, Kind.IDENTIFIER);
			int line = token.getLine();
			assertEquals(line, 1);
			int charPositionInLine = token.getCharPositionInLine();
			assertEquals(charPositionInLine, 0);
			String text = token.getText();
			assertEquals(text, "abc");
		}
		{
			IPLPToken token = lexer.nextToken();
			Kind kind = token.getKind();
			assertEquals(kind, Kind.IDENTIFIER);
			int line = token.getLine();
			assertEquals(line, 2);
			int charPositionInLine = token.getCharPositionInLine();
			assertEquals(charPositionInLine, 2);
			String text = token.getText();
			assertEquals(text, "def");
		}
		{
			IPLPToken token = lexer.nextToken();
			Kind kind = token.getKind();
			assertEquals(kind, Kind.IDENTIFIER);
			int line = token.getLine();
			assertEquals(line, 3);
			int charPositionInLine = token.getCharPositionInLine();
			assertEquals(charPositionInLine, 5);
			String text = token.getText();
			assertEquals(text, "ghi");
		}
		{
			IPLPToken token = lexer.nextToken();
			Kind kind = token.getKind();
			assertEquals(kind, Kind.EOF);
		}
	}

	@Test
	public void test2() throws LexicalException {
		String input = """
				a123 123a
				""";
		IPLPLexer lexer = getLexer(input);
		{
			IPLPToken token = lexer.nextToken();
			Kind kind = token.getKind();
			assertEquals(kind, Kind.IDENTIFIER);
			int line = token.getLine();
			assertEquals(line, 1);
			int charPositionInLine = token.getCharPositionInLine();
			assertEquals(charPositionInLine, 0);
			String text = token.getText();
			assertEquals(text, "a123");
		}
		{
			IPLPToken token = lexer.nextToken();
			Kind kind = token.getKind();
			assertEquals(kind, Kind.INT_LITERAL);
			int line = token.getLine();
			assertEquals(line, 1);
			int charPositionInLine = token.getCharPositionInLine();
			assertEquals(charPositionInLine, 5);
			String text = token.getText();
			assertEquals(text, "123");
			int val = token.getIntValue();
			assertEquals(val, 123);
		}
		{
			IPLPToken token = lexer.nextToken();
			Kind kind = token.getKind();
			assertEquals(kind, Kind.IDENTIFIER);
			int line = token.getLine();
			assertEquals(line, 1);
			int charPositionInLine = token.getCharPositionInLine();
			assertEquals(charPositionInLine, 8);
			String text = token.getText();
			assertEquals(text, "a");
		}
		{
			IPLPToken token = lexer.nextToken();
			Kind kind = token.getKind();
			assertEquals(kind, Kind.EOF);
		}
	}

	@Test
	public void test3() throws LexicalException {
		String input = """
				= == ===
				""";
		IPLPLexer lexer = getLexer(input);
		{
			IPLPToken token = lexer.nextToken();
			Kind kind = token.getKind();
			assertEquals(kind, Kind.ASSIGN);
			int line = token.getLine();
			assertEquals(line, 1);
			int charPositionInLine = token.getCharPositionInLine();
			assertEquals(charPositionInLine, 0);
			String text = token.getText();
			assertEquals(text, "=");
		}
		{
			IPLPToken token = lexer.nextToken();
			Kind kind = token.getKind();
			assertEquals(kind, Kind.EQUALS);
			int line = token.getLine();
			assertEquals(line, 1);
			int charPositionInLine = token.getCharPositionInLine();
			assertEquals(charPositionInLine, 2);
			String text = token.getText();
			assertEquals(text, "==");
		}
		{
			IPLPToken token = lexer.nextToken();
			Kind kind = token.getKind();
			assertEquals(kind, Kind.EQUALS);
			int line = token.getLine();
			assertEquals(line, 1);
			int charPositionInLine = token.getCharPositionInLine();
			assertEquals(charPositionInLine, 5);
			String text = token.getText();
			assertEquals(text, "==");
		}
		{
			IPLPToken token = lexer.nextToken();
			Kind kind = token.getKind();
			assertEquals(kind, Kind.ASSIGN);
			int line = token.getLine();
			assertEquals(line, 1);
			int charPositionInLine = token.getCharPositionInLine();
			assertEquals(charPositionInLine, 7);
			String text = token.getText();
			assertEquals(text, "=");
		}
		{
			IPLPToken token = lexer.nextToken();
			Kind kind = token.getKind();
			assertEquals(kind, Kind.EOF);
		}
	}

	@Test
	public void test4() throws LexicalException {
		String input = """
				a %
				""";
		IPLPLexer lexer = getLexer(input);
		{
			IPLPToken token = lexer.nextToken();
			Kind kind = token.getKind();
			assertEquals(kind, Kind.IDENTIFIER);
			int line = token.getLine();
			assertEquals(line, 1);
			int charPositionInLine = token.getCharPositionInLine();
			assertEquals(charPositionInLine, 0);
			String text = token.getText();
			assertEquals(text, "a");
		}
		assertThrows(LexicalException.class, () -> {
			@SuppressWarnings("unused")
			IPLPToken token = lexer.nextToken();
		});
	}
	
	@Test
	public void megatest() throws LexicalException {
		String input = """
				BOOLEAN asdf /*comment*/ 1234 "hello" "\\a" == != IF ;
				""";
		IPLPLexer lexer = getLexer(input);
		{
			IPLPToken token = lexer.nextToken();
			Kind kind = token.getKind();
			assertEquals(kind, Kind.KW_BOOLEAN);
			int line = token.getLine();
			assertEquals(line, 1);
			int charPositionInLine = token.getCharPositionInLine();
			assertEquals(charPositionInLine, 0);
			String text = token.getText();
			assertEquals(text, "BOOLEAN");
		}
		{
			IPLPToken token = lexer.nextToken();
			Kind kind = token.getKind();
			assertEquals(kind, Kind.IDENTIFIER);
			int line = token.getLine();
			assertEquals(line, 1);
			int charPositionInLine = token.getCharPositionInLine();
			assertEquals(charPositionInLine, 8);
			String text = token.getText();
			assertEquals(text, "asdf");
		}
		{
			IPLPToken token = lexer.nextToken();
			Kind kind = token.getKind();
			assertEquals(kind, Kind.INT_LITERAL);
			int line = token.getLine();
			assertEquals(line, 1);
			int charPositionInLine = token.getCharPositionInLine();
			assertEquals(charPositionInLine, 25);
			String text = token.getText();
			assertEquals(text, "1234");
		}
		{
			IPLPToken token = lexer.nextToken();
			Kind kind = token.getKind();
			assertEquals(kind, Kind.STRING_LITERAL);
			int line = token.getLine();
			assertEquals(line, 1);
			int charPositionInLine = token.getCharPositionInLine();
			assertEquals(charPositionInLine, 30);
			String text = token.getText();
			assertEquals(text, "\"hello\"");
			String textval = token.getStringValue();
			assertEquals(textval, "hello");
		}
		assertThrows(LexicalException.class, () -> {
			@SuppressWarnings("unused")
			IPLPToken token = lexer.nextToken();
		});
		{
			IPLPToken token = lexer.nextToken();
			Kind kind = token.getKind();
			assertEquals(kind, Kind.EQUALS);
			int line = token.getLine();
			assertEquals(line, 1);
			int charPositionInLine = token.getCharPositionInLine();
			assertEquals(charPositionInLine, 43);
			String text = token.getText();
			assertEquals(text, "==");
		}
		{
			IPLPToken token = lexer.nextToken();
			Kind kind = token.getKind();
			assertEquals(kind, Kind.NOT_EQUALS);
			int line = token.getLine();
			assertEquals(line, 1);
			int charPositionInLine = token.getCharPositionInLine();
			assertEquals(charPositionInLine, 46);
			String text = token.getText();
			assertEquals(text, "!=");
		}
		{
			IPLPToken token = lexer.nextToken();
			Kind kind = token.getKind();
			assertEquals(kind, Kind.KW_IF);
			int line = token.getLine();
			assertEquals(line, 1);
			int charPositionInLine = token.getCharPositionInLine();
			assertEquals(charPositionInLine, 49);
			String text = token.getText();
			assertEquals(text, "IF");
		}
		{
			IPLPToken token = lexer.nextToken();
			Kind kind = token.getKind();
			assertEquals(kind, Kind.SEMI);
			int line = token.getLine();
			assertEquals(line, 1);
			int charPositionInLine = token.getCharPositionInLine();
			assertEquals(charPositionInLine, 52);
			String text = token.getText();
			assertEquals(text, ";");
		}
		{
			IPLPToken token = lexer.nextToken();
			Kind kind = token.getKind();
			assertEquals(kind, Kind.EOF);
		}
	}

	@Test
	public void test5() throws LexicalException {
		String input = """
				99999999999999999999999999999999999999999999999999999999999999999999999
				""";
		IPLPLexer lexer = getLexer(input);
		assertThrows(LexicalException.class, () -> {
			@SuppressWarnings("unused")
			IPLPToken token = lexer.nextToken();
		});
	}

}