package syntax;

// Pred is a function that when applied to a number evaluates to its predecessor.
class Pred implements Term {

  private final Term chain;

  Pred(Term chain) {
    this.chain = chain;
  }

  @Override
  public boolean isNumericVal() {
    return false;
  }

  @Override
  public boolean isVal() {
    return isNumericVal();
  }

  @Override
  public Term evaluateSingleStep() {
    if (chain instanceof Zero) {
      return new Zero();
    } else if (chain instanceof Succ && chain.isNumericVal()) {
      // pred(succ(x)) == x
      return chain;
    } else {
      return new Pred(chain.evaluateSingleStep());
    }
  }

  @Override
  public String toString() {
    return "pred(" + chain + ")";
  }
}
