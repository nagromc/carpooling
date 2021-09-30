package org.carpooling;

import java.util.Objects;

public class Tuple<A, B> {

  protected final A a;
  protected final B b;

  public Tuple(A a, B b) {
    this.a = a;
    this.b = b;

    if (a == null) throw new ParameterCannotBeNullException("Left hand side is null");
    if (b == null) throw new ParameterCannotBeNullException("Right hand side is null");
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Tuple<?, ?> tuple = (Tuple<?, ?>) o;
    return a.equals(tuple.a) && b.equals(tuple.b);
  }

  @Override
  public int hashCode() {
    return Objects.hash(a, b);
  }


  private static class ParameterCannotBeNullException extends RuntimeException {

    public ParameterCannotBeNullException(String message) {
      super(message);
    }

  }

}
