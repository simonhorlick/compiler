package parser;

import static parser.Common.regex;
import static parser.Common.string;

import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import syntax.Abs;
import syntax.Term;

public class SyntaxParser {

  public static Parser<Term> lambda() {
    return in -> {
      Parser<String> lam = string("λ");
      Parser<MatchResult> name = regex(Pattern.compile("[a-zA-Z]+"));
      Parser<String> dot = string(".");
      Parser<Term> body =
          lambda(); // TODO: it's either a lambda abstraction or a variable (or an app).
      return lam.then(name)
          .then(dot)
          .then(body)
          .map(x -> new Abs(x.fst().snd(), x.snd()))
          .parse(in);
    };
  }
}
