class Unaryop extends Token {
  String op;

  public Unaryop(String op) { this.op = op; }

  public boolean isMath() { return this.op.equals("+") || this.op.equals("-"); }

  public String getOp() { return this.op; }

  public String toString(int t) { return getTabs(t) + this.op; }

  public Type typeCheck() { return new Type(); }
}
