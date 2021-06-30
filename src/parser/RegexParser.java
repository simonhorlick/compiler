package parser;

import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexParser implements Parser<MatchResult> {
  private final Pattern pattern;

  public RegexParser(Pattern pattern) {
    this.pattern = pattern;
  }

  public ParseResult<MatchResult> parse(String in) {
    Matcher matcher = pattern.matcher(in);
    if (matcher.lookingAt()) {
      return new ParseResult<>(matcher.toMatchResult(), in.substring(matcher.end()));
    } else {
      throw new RuntimeException("got '" + in + "'; expected '" + pattern.pattern() + "'");
    }
  }
}
