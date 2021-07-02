package syntax;

import java.util.Objects;

// Var is a variable.
public class Var implements Term {

  // The variable name for pretty printing.
  private final String name;

  // The de Bruijn index of this node. The number index stands for “the variable bound by the
  // index’th enclosing λ.”
  private final int index;

  // The length of the current context, as a consistency check.
  private final int contextLength;

  public Var(String name, int index, int contextLength) {
    this.name = name;
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
    return "Var(" + name + "," + index + ")";
  }

  public String name() {
    return name;
  }

  public int getIndex() {
    return index;
  }

  public int getContextLength() {
    return contextLength;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Var var = (Var) o;
    return index == var.index && name.equals(var.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, index);
  }
}
