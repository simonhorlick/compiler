package common;

import java.util.Objects;

public class Pair<A, B> {
  private final A fst;
  private final B snd;

  public Pair(A fst, B snd) {
    this.fst = fst;
    this.snd = snd;
  }

  public A fst() {
    return fst;
  }

  public B snd() {
    return snd;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Pair<?, ?> pair = (Pair<?, ?>) o;
    return Objects.equals(fst, pair.fst) && Objects.equals(snd, pair.snd);
  }

  @Override
  public int hashCode() {
    return Objects.hash(fst, snd);
  }
}
