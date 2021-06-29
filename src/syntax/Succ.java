package syntax;

// Succ is a function that when applied to a number evaluates to its successor, i.e. n+1.
class Succ implements Term {

  private final Term chain;

  Succ(Term chain) {
    this.chain = chain;
  }

  @Override
  public boolean isNumericVal() {
    return chain.isNumericVal();
  }

  @Override
  public boolean isVal() {
    return isNumericVal();
  }

  @Override
  public Term evaluateSingleStep() {
    return new Succ(chain.evaluateSingleStep());
  }

  @Override
  public String toString() {
    return "succ(" + chain + ")";
  }
}
