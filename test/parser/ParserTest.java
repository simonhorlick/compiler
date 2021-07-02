package parser;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.fail;
import static parser.Common.choice;
import static parser.Common.string;
import static parser.SyntaxParser.application;
import static parser.SyntaxParser.lambda;
import static parser.SyntaxParser.term;

import common.Pair;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.Test;
import parser.Parser.ParseResult;
import syntax.Abs;
import syntax.App;
import syntax.Evaluator;
import syntax.Term;
import syntax.Var;

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

  @Test
  public void shouldParseVariable() {
    String source = "x";
    Parser<Term> parser = term(new Context(Arrays.asList("x"), 0));
    assertEquals(new Var("x", 0, 1), parser.parse(source).parsed());
  }

  @Test
  public void shouldParseAbstraction() {
    String source = "λx.t";
    Parser<Term> parser = term(new Context(Arrays.asList("t"), 0));
    assertEquals(new Abs("x", new Var("t", 1, 2)), parser.parse(source).parsed());
  }

  @Test
  public void shouldParseApplication() {
    String source = "t t";
    Parser<Term> parser = term(new Context(Arrays.asList("t"), 0));
    assertEquals(new App(new Var("t", 0, 1), new Var("t", 0, 1)), parser.parse(source).parsed());
  }

  @Test
  public void shouldParseParen() {
    String source = "(x)";
    Parser<Term> parser = term(new Context(Arrays.asList("x"), 0));
    assertEquals(new Var("x", 0, 1), parser.parse(source).parsed());
  }

  @Test
  public void shouldParseParenAroundLambda() {
    String source = "(λx.t)";
    Parser<Term> parser = term(new Context(Arrays.asList("t"), 0));
    assertEquals(new Abs("x", new Var("t", 1, 2)), parser.parse(source).parsed());
  }

  @Test
  public void shouldParseChurchZero() {
    String source = "λs. λz. z";
    Parser<Term> parser = term(new Context(new ArrayList<>(), 0));
    assertEquals(new Abs("s", new Abs("z", new Var("z", 0, 1))), parser.parse(source).parsed());
  }

  @Test
  public void shouldParseChurchTwo() {
    String source = "λs. λz. s (s z)";
    Parser<Term> parser = term(new Context(new ArrayList<>(), 0));
    // λ. λ. 1 (1 0)
    Term expected =
        new Abs(
            "s",
            new Abs(
                "z", new App(new Var("s", 1, 2), new App(new Var("s", 1, 2), new Var("z", 0, 2)))));
    assertEquals(expected, parser.parse(source).parsed());
  }

  @Test
  public void shouldParseChurchPlus() {
    String source = "λm. λn. λs. λz. (m s) ((n z) s)";
    Parser<Term> parser = term(new Context(new ArrayList<>(), 0));
    // λ. λ. λ. λ. 3 1 (2 0 1)
    Term expected =
        new Abs(
            "m",
            new Abs(
                "n",
                new Abs(
                    "s",
                    new Abs(
                        "z",
                        new App(
                            new App(new Var("m", 3, 4), new Var("s", 1, 4)),
                            new App(
                                new App(new Var("n", 2, 4), new Var("z", 0, 4)),
                                new Var("s", 1, 4)))))));
    assertEquals(expected, parser.parse(source).parsed());
  }

  @Test
  public void shouldParseFix() {
    String source = "λf. ((λx. f (λy. (x x) y)) (λx. f (λy. (x x) y)))";
    Parser<Term> parser = term(new Context(new ArrayList<>(), 0));
    // λ. ((λ. 1 (λ. (1 1) 0)) (λ. 1(λ. (1 1) 0)))

    // λy. (x x) y
    Term ly =
        new Abs("y", new App(new App(new Var("x", 1, 2), new Var("x", 1, 2)), new Var("y", 0, 2)));

    // λx. f (λy. (x x) y))
    Term lx = new Abs("x", new App(new Var("f", 1, 2), ly));

    Term expected = new Abs("f", new App(lx, lx));
    assertEquals(expected, parser.parse(source).parsed());
  }

  @Test
  public void shouldParseParenInsideLambda() {
    String source = "λx.(t)";
    Parser<Term> parser = term(new Context(Arrays.asList("t"), 0));
    assertEquals(new Abs("x", new Var("t", 1, 2)), parser.parse(source).parsed());
  }

  @Test
  public void shouldParseIdentityFunction() {
    String source = "λx.x";
    Parser<Term> parser = lambda(new Context(new ArrayList<>(), 0));
    assertEquals(new Abs("x", new Var("x", 0, 1)), parser.parse(source).parsed());
  }

  @Test
  public void shouldUseCorrectDeBruijinIndices() {
    String source = "λw. y w";
    Parser<Term> parser = lambda(new Context(Arrays.asList("x", "y", "z", "a", "b"), 0));
    // expect: λ. 4 0
    assertEquals(
        new Abs("w", new App(new Var("y", 4, 5), new Var("w", 0, 5))),
        parser.parse(source).parsed());
  }

  @Test
  public void shouldParseIdentity() {
    String source = "(λx.x) y";
    Parser<Term> parser = application(new Context(new ArrayList<>(), 0));
    ParseResult<Term> parsed = parser.parse(source);
    System.out.println();
    assertEquals(new App(new Abs("x", new Var("x", 0, 1)), new Var("y", 0, 0)), parsed.parsed());
  }

  @Test
  public void shouldEvaluateIdentity() {
    String source = "(λx.x) (λt.t)";
    Parser<Term> parser = term(new Context(new ArrayList<>(), 0));
    ParseResult<Term> parsed = parser.parse(source);
    Evaluator eval = new Evaluator();
    assertEquals(new Abs("t", new Var("t", 0, 1)), eval.evaluate(parsed.parsed()));
  }

  @Test
  public void shouldEvaluateChurchBooleanAnd() {
    String truSource = "λt. λf. t";
    String flsSource = "λt. λf. t";
    String andSource = "λb. (λc. ((b c) (λt. λf. t)))";

    ParseResult<Term> tru = term(new Context(new ArrayList<>(), 0)).parse(truSource);
    ParseResult<Term> fls = term(new Context(new ArrayList<>(), 0)).parse(flsSource);
    ParseResult<Term> and = term(new Context(new ArrayList<>(), 0)).parse(andSource);

    Term andTruTru = new App(new App(and.parsed(), tru.parsed()), tru.parsed());
    Evaluator eval = new Evaluator();
    assertEquals(tru.parsed(), eval.evaluate(andTruTru));
  }

  @Test
  public void shouldEnsureRedexUsesUpBoundVariable() {
    // (λ. 1 0 2) (λ. 0)
    // → 0 (λ.0) 1
    String source = "(λx. (y x) z) (λx. x)";

    Term expected =
        new App(new App(new Var("y", 0, 1), new Abs("x", new Var("x", 0, 1))), new Var("z", 1, 1));

    ParseResult<Term> parsed = term(new Context(Arrays.asList("z", "y"), 2)).parse(source);

    Evaluator eval = new Evaluator();
    assertEquals(expected, eval.evaluate(parsed.parsed()));
  }
}
