package common;

public class Either<A, B> {
  private final A lhs;
  private final B rhs;

  public static <A, B> Either<A, B> left(A l) {
    return new Either<>(l, null);
  }

  public static <A, B> Either<A, B> right(B r) {
    return new Either<>(null, r);
  }

  public boolean isLeft() {
    return lhs != null;
  }

  public boolean isRight() {
    return rhs != null;
  }

  public A left() {
    return lhs;
  }

  public B right() {
    return rhs;
  }

  private Either(A lhs, B rhs) {
    assert (lhs == null || rhs == null);
    this.lhs = lhs;
    this.rhs = rhs;
  }
}
