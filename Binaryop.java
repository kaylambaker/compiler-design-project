class Binaryop extends Token {
  String op;

  public Binaryop(String op) { this.op = op; }

  public String toString(int t) { return getTabs(t) + this.op; }

  public String getOp() { return this.op; }

  public Type typeCheck() throws TypeCheckException { return new Type(); }

  public boolean isMath() {
    return this.op.equals("*") || this.op.equals("-") || this.op.equals("+") ||
        this.op.equals("/");
  }

  public boolean compareNum() {
    return this.op.equals("<") || this.op.equals(">") || this.op.equals("<>") ||
        this.op.equals("<=") || this.op.equals(">=") || this.op.equals("==");
  }

  public boolean compareBool() {
    return this.op.equals("&&") || this.op.equals("||");
  }
}
