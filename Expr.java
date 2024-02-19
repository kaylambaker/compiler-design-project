class Expr extends Token {
  public enum ExprType {
    BINARY,
    UNARY,
    TERNARY,
    PARENS,
    CAST,
    ID,
    CHAR,
    STR,
    INT,
    FLOAT,
    BOOL,
    NAME,
  }

  Name name;
  String id, charlit;
  Integer intlit;
  Float floatlit;
  String strlit;
  Boolean bool;
  Expr e1, e2, e3;
  Binaryop bop;
  Unaryop uop;
  String castType;
  Args args;
  ExprType type;

  public Expr(Name name) {
    this.name = name;
    this.type = ExprType.NAME;
  }

  public Expr(String id, Args args) {
    this.id = id;
    this.args = args;
    this.type = ExprType.ID;
  }

  public Expr(boolean b) {
    this.bool = b;
    this.type = ExprType.BOOL;
  }

  // assign string or char
  public Expr(ExprType type, String s) {
    if (type == ExprType.STR)
      this.strlit = s;
    else if (type == ExprType.CHAR)
      this.charlit = s;
    this.type = type;
  }

  public Expr(float f) {
    this.floatlit = f;
    this.type = ExprType.FLOAT;
  }

  public Expr(int i) {
    this.intlit = i;
    this.type = ExprType.INT;
  }

  public Expr(Expr e1, Binaryop op, Expr e2) {
    this.bop = op;
    this.e1 = e1;
    this.e2 = e2;
    this.type = ExprType.BINARY;
  }

  public Expr(Expr e1) {
    this.e1 = e1;
    this.type = ExprType.PARENS;
  }

  public Expr(Expr e1, Unaryop uop) {
    this.e1 = e1;
    this.uop = uop;
    this.type = ExprType.UNARY;
  }

  public Expr(Expr e1, String castType) {
    this.e1 = e1;
    this.castType = castType;
    this.type = ExprType.CAST;
  }

  public Expr(Expr e1, Expr e2, Expr e3) {
    this.e1 = e1;
    this.e2 = e2;
    this.e3 = e3;
    this.type = ExprType.TERNARY;
  }

  public String toString(int t) {
    String s = "";
    switch (this.type) {
    case BINARY:
      s = getTabs(t) + this.e1.toString(0) + " " + this.bop.toString(0) + " " +
          this.e2.toString(0);
      break;
    case UNARY:
      s = getTabs(t) + this.uop.toString(0) + this.e1.toString(0);
      break;
    case TERNARY:
      s = getTabs(t) + "( " + this.e1.toString(0) + " ? " +
          this.e2.toString(0) + " : " + this.e3.toString(0) + " ) ";
      break;
    case PARENS:
      s = getTabs(t) + "(" + this.e1.toString(0) + ")";
      break;
    case CAST:
      s = getTabs(t) + "(" + this.castType + ")" + this.e1.toString(0);
      break;
    case ID:
      s = getTabs(t) + this.id + "(";
      s += this.args.toString(0);
      s += ")";
      break;
    case CHAR:
      s = getTabs(t) + this.charlit;
      break;
    case STR:
      s = getTabs(t) + this.strlit;
      break;
    case INT:
      if (this.intlit != null)
        s = getTabs(t) + this.intlit;
      break;
    case FLOAT:
      s = getTabs(t) + this.floatlit;
      break;
    case BOOL:
      s = getTabs(t) + this.bool;
      break;
    case NAME:
      s = getTabs(t) + this.name.toString(0);
      break;
    }
    return s;
  }

  public Type typeCheck() throws TypeCheckException {
    Type e1Type, e2Type, e3Type;
    switch (this.type) {
    case BINARY:
      e1Type = this.e1.typeCheck();
      e2Type = this.e2.typeCheck();
      if (this.bop.isMath()) {
        if (e1Type.equals("int") && e2Type.equals("int"))
          return new Type("int");
        else if (e1Type.equals("float") && e2Type.equals("int"))
          return new Type("float");
        else if (e1Type.equals("int") && e2Type.equals("float"))
          return new Type("float");
        else if (e1Type.equals("float") && e2Type.equals("float"))
          return new Type("float");
        else if (this.bop.getOp().equals("+")) {
          if (e1Type.equals("string") || e2Type.equals("string")) {
            if(e1.isNonDereferenced() || e2.isNonDereferenced())
            throw new TypeCheckException("no");
            return new Type("string");
          } else
            throw new MathException(e1Type.type(), e2Type.type(), this.toString(0));
        } else {
          throw new MathException(e1Type.type(), e2Type.type(), this.toString(0));
        }
      } else if (this.bop.compareNum()) {
        if (e1Type.equals("int") && e2Type.equals("int") ||
            e1Type.equals("int") && e2Type.equals("float") ||
            e1Type.equals("float") && e2Type.equals("int") ||
            e1Type.equals("float") && e2Type.equals("float"))
          return new Type("bool");
        else
          throw new CannotCompareException(e1Type.type(), e2Type.type(),
                                     this.bop.getOp(), this.toString(0));
      } else { // compare bool
        if (e1Type.canBeCoerced("bool") && e2Type.canBeCoerced("bool"))
          return new Type("bool");
        else
          throw new BoolException(e1Type.type(), e2Type.type(), this.bop.getOp(),
                            this.toString(0));
      }
    case UNARY:
      e1Type = e1.typeCheck();
      if (this.uop.isMath()) {
        if (e1Type.equals("int"))
          return new Type("int");
        else if (e1Type.equals("float"))
          return new Type("float");
        else
          throw new UopException(this.uop.getOp(), e1Type.type(), this.toString(0));
      } else { // boolean op
        if (e1Type.equals("bool"))
          return new Type("bool");
        else
          throw new UopException(this.uop.getOp(), e1Type.type(), this.toString(0));
      }
    case ID: // NOTE: same code as in stmt, if need change, also need change
             // stmt
      SymbolTable.Entry func = symbolTable.get(this.id);
      if (!func.isFunction())
        throw new NotFuncException(this.id, this.toString(0));
      if (func.argdecls().size() != this.args.size())
        throw new WrongArgCountException(func.argdecls().size(), this.args.size(),
                                   this.toString(0));
      for (int i = 0; i < func.argdecls().size(); i++) {
        Argdecl a = func.argdecls().get(i);
        Expr e = this.args.get(i);
        if (!e.typeCheck().canBeCoerced(a.getType()))
          throw new FuncArgTypeException(a.getType(), e.typeCheck().type(), this.id,
                                   this.toString(0));
      }
      return new Type(func.datatype());
    case TERNARY:
      e1Type = e1.typeCheck();
      e2Type = e2.typeCheck();
      e3Type = e3.typeCheck();
      if (!e1Type.canBeCoerced("bool"))
        throw new TernaryBoolException(e1Type.type(), this.toString(0));
      if (!e2Type.equals(e3Type))
        throw new TernaryEqException(e2Type.type(), e3Type.type(), this.toString(0));
      return e2Type;
    case PARENS:
      return e1.typeCheck();
    case CAST:
      return new Type(this.castType);
    case NAME:
      return this.name.typeCheck();
    case INT:
      return new Type("int");
    case CHAR:
      return new Type("char");
    case STR:
      return new Type("string");
    case BOOL:
      return new Type("bool");
    case FLOAT:
      return new Type("float");
    }
    return new Type();
  }

  public boolean isArray() throws TypeCheckException {
    return this.name != null && this.name.isArray();
  }

  public boolean isNonDereferenced() throws TypeCheckException {
    return this.name != null && this.name.isNonDereferenced();
  }

  public boolean isFunction() throws TypeCheckException {
    if (this.id == null)
      return false;
    SymbolTable.Entry t = symbolTable.get(this.id);
    return this.id != null && t.isFunction();
  }

  public boolean isFinal() throws TypeCheckException {
    return this.name != null && this.name.isFinal();
  }

  public boolean isVar() throws TypeCheckException { return this.name != null; }
}
