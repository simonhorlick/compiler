package syntax;

// IsZero is a term that when applied to the numeric constant zero evaluates to true.
class IsZero implements Term {

  private final Term term;

  IsZero(Term term) {
    this.term = term;
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
    if (term instanceof Zero) {
      return new True();
    } else if (term instanceof Succ && term.isNumericVal()) {
      return new False();
    } else {
      return new IsZero(term.evaluateSingleStep());
    }
  }

  @Override
  public String toString() {
    return "iszero(" + term + ")";
  }
}
