package syntax;

// Var is a variable.
class Var implements Term {

  // The de Bruijn index of this node. The number index stands for “the variable bound by the
  // index’th enclosing λ.”
  private final int index;

  // The length of the current context, as a consistency check.
  private final int contextLength;

  public Var(int index, int contextLength) {
    this.index = index;
    this.contextLength = contextLength;
  }

  @Override
  public boolean isNumericVal() {
    return false;
  }

  @Override
  public boolean isVal() {
    return false;
  }

  @Override
  public Term evaluateSingleStep() {
    throw new RuntimeException("no rule applies");
  }

  @Override
  public String toString() {
    return Integer.toString(index);
  }

  public int getIndex() {
    return index;
  }

  public int getContextLength() {
    return contextLength;
  }

  @Override
  public boolean equals(Object o) {
    return toString().equals(o.toString());
  }
}
