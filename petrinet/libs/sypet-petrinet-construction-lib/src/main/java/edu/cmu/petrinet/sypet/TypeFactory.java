package edu.cmu.petrinet.sypet;

public final class TypeFactory {
  public Type createVoidType() {
    return new Type() {
      @Override
      public String name() {
        return "void";
      }

      @Override
      public boolean isCastableTo(Type type) {
        return false;
      }
    };
  }
}
