package syntax;

// False is the constant boolean value false.
class False implements Term {

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
    return other instanceof False;
  }

  @Override
  public String toString() {
    return "false";
  }
}
