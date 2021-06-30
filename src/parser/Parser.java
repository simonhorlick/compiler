package parser;

import common.Pair;
import java.util.function.Function;

// Parser is the interface for all Parsers. A parser consumes an input string and, if successful,
// returns the parsed result `A`. If the parser is unsuccessful it throws.
public interface Parser<A> {

  // parse consumes from the start of the input string and returns a result.
  A parse(String in);

  // then orders parsers.
  default <U> Parser<Pair<A, U>> then(Parser<U> that) {
    return in -> new Pair<>(parse(in), that.parse(in));
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

  // map returns a parser that parses the input and then applies the given function to the parsed
  // result.
  default <U> Parser<U> map(Function<A, U> f) {
    return in -> f.apply(parse(in));
  }
}
