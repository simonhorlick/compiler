package syntax;

// True is the boolean constant value true.
class True implements Term {

  @Override
  public boolean isNumericVal() {
    return false;
  }

  @Override
  public boolean isVal() {
    return true;
  }

  @Override
  public Term evaluateSingleStep() {
    throw new RuntimeException("no rule applies");
  }

  @Override
  public boolean equals(Object other) {
    return other instanceof True;
  }

  @Override
  public String toString() {
    return "true";
  }
}
