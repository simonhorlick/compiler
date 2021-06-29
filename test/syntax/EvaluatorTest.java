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
}
