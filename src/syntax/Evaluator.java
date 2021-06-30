package syntax;

public class Evaluator {

  public Term step(Term t) {
    return t.evaluateSingleStep();
  }

  // evaluate reduces a term to normal form.
  public Term evaluate(Term t) {
    // FIXME this is gross.
    try {
      while (true) {
        System.out.println(t);
        t = t.evaluateSingleStep();
      }
    } catch (Exception e) {
      // ignored
    }
    return t;
  }
}
