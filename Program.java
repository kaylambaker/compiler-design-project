class Program extends Token {
  Memberdecls memberdecls;
  String id;

  // Constructor
  public Program(String id, Memberdecls memberdecls) {
    this.id = id;
    this.memberdecls = memberdecls;
    symbolTable = new SymbolTable();
    symbolTable.startScope();
  }

  public String toString(int t) {
    String s = "Program:\nclass " + this.id + " {\n";
    s += this.memberdecls.toString(t + 1);
    s += "}";
    return s;
  }

  public Type typeCheck() throws TypeCheckException {
    return this.memberdecls.typeCheck();
  }
}
