package parser;

import java.util.Arrays;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

// Common general-purpose parsers.
public class Common {

  // string returns a Parser that matches the exact regular expression in the input.
  public static Parser<MatchResult> regex(Pattern pattern) {
    return new RegexParser(pattern);
  }

  // string returns a Parser that matches the exact string in the input.
  public static Parser<String> string(String string) {
    return regex(Pattern.compile(Pattern.quote(string))).map(MatchResult::group);
  }

  // choice returns the first parser that matches the input.
  @SafeVarargs
  public static <B> Parser<B> choice(Parser<B> parser, Parser<B>... parsers) {
    return Arrays.stream(parsers).reduce(parser, Parser::or);
  }
}
