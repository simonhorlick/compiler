package parser;

import static parser.Common.regex;
import static parser.Common.string;

import java.util.regex.Pattern;
import syntax.Abs;
import syntax.Term;

public class SyntaxParser {

  public static Parser<Term> lambda() {
    return in -> {
      // TODO: it's either a lambda abstraction or a variable (or an app).
      Parser<Term> body = lambda();

      Parser<Term> lam =
          string("λ")
              .then(regex(Pattern.compile("[a-zA-Z]+")))
              .then(string("."))
              .then(body)
              .map(x -> new Abs(x.fst().snd(), x.snd()));

      return lam.parse(in);
    };
  }
}
