package parser;

import common.Pair;
import java.util.function.Function;
import java.util.function.Supplier;

// Parser is the interface for all Parsers. A parser consumes an input string and, if successful,
// returns the parsed result `A`. If the parser is unsuccessful it throws.
public interface Parser<A> {

  // ParseResult contains the result of a successful parse and the remaining substring of the input.
  class ParseResult<A> {
    private final A parsed;
    private final String remaining;

    public ParseResult(A parsed, String remaining) {
      this.parsed = parsed;
      this.remaining = remaining;
    }

    public A parsed() {
      return parsed;
    }

    public String remaining() {
      return remaining;
    }
  }

  // parse consumes from the start of the input string and returns a result.
  ParseResult<A> parse(String in);

  // then orders parsers.
  default <U> Parser<Pair<A, U>> then(Parser<U> that) {
    return in -> {
      ParseResult<A> r = parse(in);
      ParseResult<U> s = that.parse(r.remaining);
      return new ParseResult<>(new Pair<>(r.parsed, s.parsed), s.remaining);
    };
  }

  // or allows a fallback parser to be used in the case that this parser is unable to parse the
  // input.
  default Parser<A> or(Parser<A> that) {
    return in -> {
      try {
        return parse(in);
      } catch (Exception e) {
        return that.parse(in);
      }
    };
  }

  // or that also takes a creation function, i.e. or(() -> other).
  default Parser<A> or(Supplier<Parser<A>> that) {
    return in -> or(that.get()).parse(in);
  }

  // map returns a parser that parses the input and then applies the given function to the parsed
  // result.
  default <U> Parser<U> map(Function<A, U> f) {
    return in -> {
      ParseResult<A> parsed = parse(in);
      return new ParseResult<>(f.apply(parsed.parsed), parsed.remaining);
    };
  }
}
