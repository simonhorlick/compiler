package parser;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.fail;
import static parser.Common.choice;
import static parser.Common.string;

import common.Pair;
import org.junit.Test;
import parser.Parser.ParseResult;

public class ParserTest {
  @Test
  public void shouldParseAStringThatMatchesTheWholeInput() {
    Parser<String> fooParser = string("foo");
    assertEquals("foo", fooParser.parse("foo").parsed());
  }

  @Test
  public void shouldFailToParseNonMatchingString() {
    Parser<String> fooParser = string("foo");
    try {
      fooParser.parse("bar");
      fail("expected exception");
    } catch (Exception ex) {
      // ignored
    }
  }

  @Test
  public void shouldParseAStringThatMatchesAPrefixOfTheInput() {
    // Create a parser that expects the exact string "token".
    Parser<String> tokenParser = string("token");
    ParseResult<String> result = tokenParser.parse("tokens");
    assertEquals("token", result.parsed());
    assertEquals("s", result.remaining());
  }

  @Test
  public void shouldParseTwoStringsInSequence() {
    Parser<String> fooParser = string("foo");
    Parser<String> barParser = string("bar");
    Parser<Pair<String, String>> foobarParser = fooParser.then(barParser);
    assertEquals(new Pair<>("foo", "bar"), foobarParser.parse("foobar").parsed());
  }

  @Test
  public void shouldParseOr() {
    Parser<String> fooOrBarParser = choice(string("foo"), string("bar"));
    assertEquals("foo", fooOrBarParser.parse("foo").parsed());
    assertEquals("bar", fooOrBarParser.parse("bar").parsed());
    try {
      fooOrBarParser.parse("quux");
      fail("expected exception");
    } catch (Exception ex) {
      // ignored
    }
  }
}
