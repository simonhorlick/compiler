package parser;

import common.Either;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

  static class ParseState<T> {
    private final String remaining;
    private final T state;

    ParseState(String remaining, T state) {
      this.remaining = remaining;
      this.state = state;
    }

    public String getRemaining() {
      return remaining;
    }

    public T getState() {
      return state;
    }
  }

  // parseString returns a parser that consumes the string str, or returns a parse error.
  public static <ParserResult>
      Function<String, Either<ParseState<ParserResult>, ParseError>> parseString(String str) {
    return (String input) -> {
      if (input.startsWith(str)) {
        return Either.left(new ParseState(input.substring(str.length()), null));
      } else {
        return Either.right(new ParseError("expected '" + str + "'; got " + input));
      }
    };
  }

  // parseRegex returns a parser that consumes the regex if it matches, or returns a parse error.
  public static <ParserResult>
      Function<String, Either<ParseState<ParserResult>, ParseError>> parseRegex(String regex) {
    Pattern p = Pattern.compile("^" + regex);
    return (String input) -> {
      Matcher m = p.matcher(input);
      if (m.matches()) {
        return Either.left(new ParseState(input.substring(m.end()), null));
      } else {
        return Either.right(new ParseError("expected match for '" + regex + "'; got " + input));
      }
    };
  }

  public static <ParserResult>
      Function<String, Either<ParseState<ParserResult>, ParseError>> parseExpression() {
    return (String input) -> {
      Function<String, Either<ParseState<ParserResult>, ParseError>> lambda = parseString("λ");
      Function<String, Either<ParseState<ParserResult>, ParseError>> binder =
          parseRegex("[a-zA-Z]+");
      Function<String, Either<ParseState<ParserResult>, ParseError>> dot = parseString(".");

      // FIXME: Use Either monad here.
      Either<ParseState<ParserResult>, ParseError> r1 = lambda.apply(input);
      if (r1.isLeft()) {
        Either<ParseState<ParserResult>, ParseError> r2 = binder.apply(r1.left().remaining);
        if (r2.isLeft()) {
          return dot.apply(r2.left().remaining);
        } else {
          return r2;
        }
      } else {
        return r1;
      }
    };
  }
}
