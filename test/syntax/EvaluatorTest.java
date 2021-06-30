package syntax;

import static junit.framework.TestCase.assertEquals;

import org.junit.Test;

public class EvaluatorTest {

  Term zero = new Zero();
  Term one = new Succ(zero);
  Term two = new Succ(one);
  Term three = new Succ(two);

  Evaluator evaluator = new Evaluator();

  @Test
  public void shouldEvaluateIsZeroOfZero() {
    Term term = new IsZero(zero);
    assertEquals(new True(), term.evaluateSingleStep());
  }

  @Test
  public void shouldEvaluateIsZeroOfOne() {
    Term term = new IsZero(one);
    assertEquals(new False(), term.evaluateSingleStep());
  }

  @Test
  public void shouldEvaluateIfStatement() {
    // if iszero(1) then 2 else 3
    Term term = new If(new IsZero(one), two, three);
    assertEquals(three, evaluator.evaluate(term));
  }

  @Test
  public void shouldEvaluateChurchBooleans() {
    // FIXME: This can't possibly work as there's no way to encode free variables in the current
    //  implementation.

    // tru = λt. λf. t
    // fls = λt. λf. f
    Term tru = new Abs("t", new Abs("f", new Var(1, 2)));
    Term fls = new Abs("t", new Abs("f", new Var(0, 2)));

    // and = λb. λc. b c fls
    Term and = new Abs("b", new Abs("c", new App(new Var(1, 2), new App(new Var(0, 2), fls))));

    // and tru fls -> fls
    assertEquals(fls, evaluator.evaluate(new App(new App(and, tru), fls)));
  }

  @Test
  public void shouldEvaluateLambdaTerms() {
    // The ordinary term λx.x corresponds to the nameless term λ.0, while
    // λx.λy. x (y x) corresponds to λ.λ. 1 (0 1).

    // The number zero in Church numeral form. c0 = λs. λz. z;
    Term c0 = new Abs("s", new Abs("z", new Var(0, 2)));

    // c1 = λs. λz.sz;
    Term c1 = new Abs("s", new Abs("z", new App(new Var(1, 2), new Var(0, 2))));

    // plus = λm. λn. λs. λz.ms(nzs);
    Term plus =
        new Abs(
            "m",
            new Abs(
                "n",
                new Abs(
                    "s",
                    new Abs(
                        "z",
                        new App(
                            new App(new Var(3, 4), new Var(1, 4)),
                            new App(new App(new Var(2, 4), new Var(0, 4)), new Var(1, 4)))))));

    assertEquals(c1, evaluator.evaluate(new App(new App(plus, c0), c1)));

    // c2 = λs. λz.s(sz);
    Term c2 =
        new Abs("s", new Abs("z", new App(new Var(1, 2), new App(new Var(1, 2), new Var(0, 2)))));

    // c2 = λs. λz.s(s(sz));
    Term c3 =
        new Abs(
            "s",
            new Abs(
                "z",
                new App(
                    new Var(1, 2), new App(new Var(1, 2), new App(new Var(1, 2), new Var(0, 2))))));

    assertEquals(c3, evaluator.evaluate(new App(new App(plus, c1), c2)));
  }
}
