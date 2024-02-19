public class Fielddecl extends Token {
  enum DeclType { ARRAY, OPTIONS }

  String dataType, id;
  Expr optionalexpr;
  boolean optionalfinal;
  int intlit;
  DeclType fielddeclType;

  public Fielddecl(boolean optionalfinal, String dataType, String id,
                   Expr optionalexpr) {
    this.optionalfinal = optionalfinal;
    this.dataType = dataType;
    this.id = id;
    this.optionalexpr = optionalexpr;
    this.fielddeclType = DeclType.OPTIONS;
  }

  public Fielddecl(String dataType, String id, int intlit) {
    this.dataType = dataType;
    this.id = id;
    this.intlit = intlit;
    this.fielddeclType = DeclType.ARRAY;
  }

  public String toString(int t) {
    String s = getTabs(t);
    switch (this.fielddeclType) {
    case OPTIONS:
      if (this.optionalfinal)
        s += "final ";
      s += this.dataType + " " + this.id;
      if (optionalexpr != null)
        s += " = " + this.optionalexpr.toString(0);
      s += ";";
      break;
    case ARRAY:
      s += this.dataType + " " + this.id + "[" + this.intlit + "];";
      break;
    }
    return s;
  }

  public Type typeCheck() throws TypeCheckException {
    SymbolTable.Entry t;
    switch (this.fielddeclType) {
    case ARRAY:
      t = symbolTable.new Entry(this.dataType, false, true);
      symbolTable.addVar(this.id, t);
      break;
    case OPTIONS:
      t = symbolTable.new Entry(this.dataType, this.optionalfinal, false);
      if (this.optionalexpr != null) {
        Type s = this.optionalexpr.typeCheck();
        // if (!Type.canBeCoerced(s.type(), this.dataType)) // rhs coerced into
        // lhs type
        if (!s.canBeCoerced(this.dataType))
          throw new MismatchedException(this.dataType, s.type(), this.toString(0));
      }
      symbolTable.addVar(this.id, t);
      break;
    }
    return new Type();
  }
}
