package syntax;

// If is a term that evaluates to the true branch if the statement is true and the false branch
// otherwise.
class If implements Term {

  private final Term statement;
  private final Term trueBranch;
  private final Term falseBranch;

  If(Term statement, Term trueBranch, Term falseBranch) {
    this.statement = statement;
    this.trueBranch = trueBranch;
    this.falseBranch = falseBranch;
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
    if (statement instanceof True) {
      return trueBranch;
    } else if (statement instanceof False) {
      return falseBranch;
    } else {
      // The statement needs to be broken down.
      return new If(statement.evaluateSingleStep(), trueBranch, falseBranch);
    }
  }

  @Override
  public String toString() {
    return "if " + statement + " then " + trueBranch + " else " + falseBranch;
  }
}
