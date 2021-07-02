package parser;

import static parser.Common.regex;
import static parser.Common.string;

import common.Pair;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import parser.Parser.ParseResult;
import syntax.Abs;
import syntax.App;
import syntax.Term;
import syntax.Var;

public class SyntaxParser {

  public static Parser<MatchResult> name() {
    return regex(Pattern.compile("[a-zA-Z]+"));
  }

  public static Parser<Term> variable(Context context) {
    return name()
        .map(n -> new Var(n.group(), context.getDeBruijinIndex(n.group()), context.names().size()));
  }

  public static Parser<Term> application(Context context) {
    // examples of valid syntax: "t t" or "(t)t" or "(t) (t)"
    return (variable(context).then(string(" ")).map(x -> x.fst()))
        .or(() -> paren(context))
        .then(regex(Pattern.compile("[ ]*")))
        .map(x -> x.fst())
        .then(variable(context).or(() -> paren(context)))
        .map(n -> new App(n.fst(), n.snd()));
  }

  public static Parser<Term> paren(Context context) {
    return string("(").then(term(context)).then(string(")")).map(x -> x.fst().snd());
  }

  public static Parser<Term> term(Context context) {
    return application(context)
        .or(() -> paren(context))
        .or(() -> lambda(context))
        .or(() -> variable(context));
  }

  public static Parser<Term> lambda(Context context) {
    return in -> {
      // First parse the binder name up to the dot.
      Parser<Pair<Pair<String, MatchResult>, MatchResult>> lam =
          string("λ").then(name()).then(regex(Pattern.compile("\\.[ ]*")));

      ParseResult<Pair<Pair<String, MatchResult>, MatchResult>> nme = lam.parse(in);

      String absName = nme.parsed().fst().snd().group();

      // Then parse the rest of the expression.
      Parser<Term> body = term(context.addBinder(absName)).map(x -> new Abs(absName, x));

      return body.parse(nme.remaining());
    };
  }
}
