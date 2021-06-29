package syntax;

// Zero is the numeric constant value zero.
class Zero implements Term {

  @Override
  public boolean isNumericVal() {
    return true;
  }

  @Override
  public boolean isVal() {
    return isNumericVal();
  }

  @Override
  public Term evaluateSingleStep() {
    throw new RuntimeException("no rule applies");
  }

  @Override
  public String toString() {
    return "zero";
  }
}
