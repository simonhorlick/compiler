package parser;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

import common.Either;
import java.util.function.Function;
import org.junit.Test;
import parser.Parser1.ParseState;

public class Parser1Test {
  // Create a parser that expects the exact string "token".
  Function<String, Either<ParseState<Void>, ParseError>> parseStringToken =
      Parser1.parseString("token");

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

  @Test
  public void shouldParseAnIdentifier() {
    Function<String, Either<ParseState<Void>, ParseError>> binder = Parser1.parseRegex("[a-zA-Z]+");

    Either<ParseState<Void>, ParseError> result = binder.apply("One.remaining");
    assertTrue(result.isLeft());
    assertEquals("remaining", result.left().getRemaining());
  }
}
