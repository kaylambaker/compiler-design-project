class Name extends Token {
  String id;
  Expr expr;

  public Name(String id) {
    this.id = id;
    expr = null;
  }

  public String getID() { return this.id; }

  public Name(String id, Expr expr) {
    this.id = id;
    this.expr = expr;
  }

  public String toString(int t) {
    return this.expr == null
        ? getTabs(t) + this.id
        : getTabs(t) + this.id + "[" + this.expr.toString(0) + "]";
  }

  public Type typeCheck() throws TypeCheckException {
    if (this.isArray() && !this.expr.typeCheck().equals("int"))
      throw new IndexException(this.toString(0));
    return new Type(symbolTable.get(this.id).datatype());
  }

  public boolean isArray() throws TypeCheckException {
    SymbolTable.Entry t = symbolTable.get(this.id);
    return t.isArray() && expr != null;
  }

  public boolean isNonDereferenced() throws TypeCheckException {
    SymbolTable.Entry t = symbolTable.get(this.id);
    return t.isArray() && expr == null;
  }

  public boolean isFunction() throws TypeCheckException {
    return symbolTable.get(this.id).isFunction();
  }

  public boolean isFinal() throws TypeCheckException {
    return symbolTable.get(this.id).isFinal();
  }
}
