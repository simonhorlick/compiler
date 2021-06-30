package parser;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.fail;
import static parser.Common.choice;
import static parser.Common.string;

import org.junit.Test;

public class ParserTest {
  @Test
  public void shouldParseString() {
    Parser<String> fooParser = string("foo");
    assertEquals("foo", fooParser.parse("foo"));
    try {
      fooParser.parse("bar");
      fail("expected exception");
    } catch (Exception ex) {
      // ignored
    }
  }

  @Test
  public void shouldParseOr() {
    Parser<String> fooOrBarParser = choice(string("foo"), string("bar"));
    assertEquals("foo", fooOrBarParser.parse("foo"));
    assertEquals("bar", fooOrBarParser.parse("bar"));
    try {
      fooOrBarParser.parse("quux");
      fail("expected exception");
    } catch (Exception ex) {
      // ignored
    }
  }
}
