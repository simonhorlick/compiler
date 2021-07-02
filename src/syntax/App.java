package syntax;

import java.util.Objects;

// App is an application of two terms.
public class App implements Term {

  private final Term left;
  private final Term right;

  public App(Term left, Term right) {
    this.left = left;
    this.right = right;
  }

  public Term getLeft() {
    return left;
  }

  public Term getRight() {
    return right;
  }

  @Override
  public boolean isNumericVal() {
    return false;
  }

  @Override
  public boolean isVal() {
    return false;
  }

  // The shifting function below takes a “cutoff” parameter c that controls which variables should
  // be shifted.
  private Term walk(int d, int c, Term t) {
    if (t instanceof Var) {
      int n = ((Var) t).getContextLength();
      int x = ((Var) t).getIndex();
      if (x >= c) {
        return new Var(((Var) t).name(), x + d, n + d);
      } else {
        return new Var(((Var) t).name(), x, n + d);
      }
    } else if (t instanceof Abs) {
      String x = ((Abs) t).getName();
      Term t1 = ((Abs) t).getBody();
      return new Abs(x, walk(d, c + 1, t1));
    } else if (t instanceof App) {
      Term t1 = ((App) t).getLeft();
      Term t2 = ((App) t).getRight();
      return new App(walk(d, c, t1), walk(d, c, t2));
    } else {
      throw new RuntimeException("not implemented");
    }
  }

  // shifting (6.2.1)
  private Term termShift(int d, Term t) {
    return walk(d, 0, t);
  }

  private Term termSubstWalk(int j, Term s, int c, Term t) {
    if (t instanceof Var) {
      int n = ((Var) t).getContextLength();
      int x = ((Var) t).getIndex();
      if (x == j + c) {
        return termShift(c, s);
      } else {
        return new Var(((Var) t).name(), x, n);
      }
    } else if (t instanceof Abs) {
      String x = ((Abs) t).getName();
      Term t1 = ((Abs) t).getBody();
      return new Abs(x, termSubstWalk(j, s, c + 1, t1));
    } else if (t instanceof App) {
      Term t1 = ((App) t).getLeft();
      Term t2 = ((App) t).getRight();
      return new App(termSubstWalk(j, s, c, t1), termSubstWalk(j, s, c, t2));
    } else {
      throw new RuntimeException("not implemented");
    }
  }

  // The substitution [j -> s]t of term s for the variable numbered j in term t.
  private Term termSubst(int j, Term s, Term t) {
    return termSubstWalk(j, s, 0, t);
  }

  // The term being substituted for the bound variable is first shifted up by one, then the
  // substitution is made, and then the whole result is shifted down by one to account for the
  // fact that the bound variable has been used up.
  private Term termSubstTop(Term s, Term t) {
    return termShift(-1, termSubst(0, termShift(1, s), t));
  }

  @Override
  public Term evaluateSingleStep() {
    if (left instanceof Abs && right.isVal()) {
      // termSubstTop v2 t12
      Term t12 = ((Abs) left).getBody();
      Term v2 = right;
      return termSubstTop(v2, t12);
    } else if (left.isVal()) {
      // let t2’ = eval1 ctx t2 in
      //   TmApp(fi, v1, t2’)
      return new App(left, right.evaluateSingleStep());
    } else {
      // let t1’ = eval1 ctx t1 in
      //  TmApp(fi, t1’, t2)
      return new App(left.evaluateSingleStep(), right);
    }
  }

  @Override
  public String toString() {
    return "App(" + left + "," + right + ")";
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    App app = (App) o;
    return left.equals(app.left) && right.equals(app.right);
  }

  @Override
  public int hashCode() {
    return Objects.hash(left, right);
  }
}
