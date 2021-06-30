package syntax;

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
    return "λ" + name + ". " + body;
  }

  @Override
  public boolean equals(Object o) {
    return toString().equals(o.toString());
  }
}
