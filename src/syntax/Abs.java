package syntax;

import java.util.Objects;

// Abs is a lambda abstraction.
public class Abs implements Term {

  // The name of this abstraction.
  private final String name;

  // The body of the abstraction.
  private final Term body;

  public Abs(String name, Term body) {
    this.name = name;
    this.body = body;
  }

  public String getName() {
    return name;
  }

  public Term getBody() {
    return body;
  }

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
  public String toString() {
    return "Abs(" + name + "," + body + ")";
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Abs abs = (Abs) o;
    return name.equals(abs.name) && body.equals(abs.body);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, body);
  }
}
