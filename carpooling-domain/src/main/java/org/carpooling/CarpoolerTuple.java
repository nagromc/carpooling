package org.carpooling;

public class CarpoolerTuple extends Tuple<Carpooler, Carpooler> {

  public CarpoolerTuple(Carpooler debtor, Carpooler creditor) {
    super(debtor, creditor);
  }

  @Override
  public String toString() {
    return a + "->" + b;
  }

}
