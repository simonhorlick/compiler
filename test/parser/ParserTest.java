package parser;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

import common.Either;
import java.util.function.Function;
import org.junit.Test;
import parser.Parser.ParseState;

public class ParserTest {
  // Create a parser that expects the exact string "token".
  Function<String, Either<ParseState<Void>, ParseError>> parseStringToken =
      Parser.parseString("token");

  @Test
  public void shouldReturnAParseErrorWhenNoInputIsGiven() {
    Either<ParseState<Void>, ParseError> result = parseStringToken.apply("");
    assertTrue(result.isRight());
  }

  @Test
  public void shouldParseAStringThatMatchesTheWholeInput() {
    Either<ParseState<Void>, ParseError> result = parseStringToken.apply("token");
    assertTrue(result.isLeft());
    assertEquals("", result.left().getRemaining());
  }

  @Test
  public void shouldParseAStringThatMatchesAPrefixOfTheInput() {
    Either<ParseState<Void>, ParseError> result = parseStringToken.apply("tokens");
    assertTrue(result.isLeft());
    assertEquals("s", result.left().getRemaining());
  }
}
