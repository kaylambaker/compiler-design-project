public class Stmt extends Token {
  enum StmtType {
    IF,
    WHILE,
    RETEXPR,
    ID,
    UNARY,
    READ,
    PRINT,
    ASSIGN,
    CURLY,
  }

  Expr expr;
  Name name;
  Stmt stmt;
  Stmts stmts;
  Fielddecls fielddecls;
  IfEnd ifend;
  String id, func;
  Args args;
  Printlist printlist;
  StmtType type;
  Unaryop uop;
  Readlist readlist;
  boolean optionalsemi;

  public Stmt(Expr expr, Stmt stmt, IfEnd ifend) {
    this.expr = expr;
    this.stmt = stmt;
    this.ifend = ifend;
    this.type = StmtType.IF;
  }

  public Stmt(String id, Args args) {
    this.id = id;
    this.args = args;
    this.type = StmtType.ID;
  }

  public Stmt() { this.type = StmtType.RETEXPR; }

  public Stmt(Expr expr) {
    this.expr = expr;
    this.type = StmtType.RETEXPR;
  }

  public Stmt(Name name, Unaryop uop) {
    this.name = name;
    this.uop = uop;
    this.type = StmtType.UNARY;
  }

  public Stmt(Readlist readlist) {
    this.readlist = readlist;
    this.type = StmtType.READ;
  }

  public Stmt(Fielddecls fielddecls, Stmts stmts, boolean optionalsemi) {
    this.stmts = stmts;
    this.optionalsemi = optionalsemi;
    this.fielddecls = fielddecls;
    this.type = StmtType.CURLY;
  }

  public Stmt(Printlist printlist, String func) {
    this.printlist = printlist;
    this.func = func;
    this.type = StmtType.PRINT;
  }

  public Stmt(Name name, Expr expr) {
    this.name = name;
    this.expr = expr;
    this.type = StmtType.ASSIGN;
  }

  public Stmt(Expr expr, Stmt stmt) {
    this.expr = expr;
    this.stmt = stmt;
    this.type = StmtType.WHILE;
  }

  public String toString(int t) {
    String s = "";
    switch (this.type) {
    case ID:
      s = getTabs(t) + this.id + "(";
      s += this.args.toString(0);
      s += ");";
      break;
    case RETEXPR:
      s = getTabs(t) + "return ";
      if (this.expr != null)
        s += this.expr.toString(0);
      s += ";";
      break;
    case UNARY:
      s = getTabs(t) + this.name.toString(0) + this.uop.toString(0) + ";";
      break;
    case READ:
      s = getTabs(t) + "read(";
      s += this.readlist.toString(0);
      s += ");";
      break;
    case PRINT:
      s = getTabs(t) + this.func + "(";
      s += this.printlist.toString(0);
      s += ");";
      break;
    case ASSIGN:
      s += getTabs(t) + this.name.toString(0) + " = " + this.expr.toString(0) +
           ";";
      break;
    case WHILE:
      s += getTabs(t) + "while(" + this.expr.toString(0) + ") \n" +
           this.stmt.toString(t + 1);
      break;
    case CURLY:
      s += getTabs(t - 1) + "{\n";
      s += this.fielddecls.toString(t);
      s += this.stmts.toString(t);
      s += getTabs(t - 1) + "}";
      if (this.optionalsemi)
        s += ";";
      break;
    case IF:
      s += getTabs(t) + "if (" + this.expr.toString(0) + ") \n" +
           this.stmt.toString(t + 1);
      if (this.ifend != null)
        s += "\n" + this.ifend.toString(t);
      break;
    }
    return s;
  }

  public Type typeCheck() throws TypeCheckException {
    Type exprType;
    SymbolTable.Entry nameType;
    switch (this.type) {
    case IF:
    case WHILE:
      exprType = this.expr.typeCheck();
      if (!exprType.canBeCoerced("bool")) {
        if (this.type == StmtType.IF)
          throw new StmtCondException("if", exprType.type(), this.toString(0));
        else
          throw new StmtCondException("while", exprType.type(), this.toString(0));
      }
      Type stmtType = this.stmt.typeCheck();
      if (this.ifend != null) {
        Type ifendType = this.ifend.typeCheck();
        if (!ifendType.equals(stmtType)) {
          if (ifendType.equals("none") && stmtType.equals("none"))
            return new Type("none", false);
          if (ifendType.equals("none") && !stmtType.equals("none"))
            return new Type(stmtType.type(), true);
          if (!ifendType.equals("none") && stmtType.equals("none"))
            return new Type(stmtType.type(), true);
        } else { // if if and else statements are same type
          return new Type(stmtType.type(), false);
        }
        // if there is no else
        return new Type(stmtType.type(), true);
      }
      return new Type(stmtType.type(), true);
    case RETEXPR:
      return this.expr == null ? new Type("void") : this.expr.typeCheck();
    case ID: // NOTE: same code as in expr, if need change, also need change
             // expr
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
      return new Type();
    case UNARY:
      nameType = symbolTable.get(this.name.getID());
      if (nameType.isFinal())
        throw new FinalException(this.toString(0));
      if (!nameType.eqDataType("int") && !nameType.eqDataType("float"))
        throw new MathException(nameType.datatype(), this.toString(0));
      return new Type();
    case READ:
      try {
        return this.readlist.typeCheck();
      } catch (ReadlistException r) {
        throw new ReadException(r.type, r.arg, this.toString(0));
      }
    case PRINT:
      try {
        return this.printlist.typeCheck();
      } catch (PrintlistException p) {
        throw new PrintException(p.type, p.arg, this.toString(0));
      }
    case ASSIGN:
      nameType = symbolTable.get(this.name.getID());
      exprType = this.expr.typeCheck();
      if (nameType.isFinal())
        throw new FinalException(this.toString(0));
      if (!exprType.canBeCoerced(nameType.datatype()))
        throw new MismatchedException(nameType.datatype(), exprType.type(),
                                this.toString(0));
      return new Type();
    case CURLY:
      symbolTable.startScope();
      this.fielddecls.typeCheck();
      Type type = this.stmts.typeCheck();
      symbolTable.endScope();
      return type;
    }
    return new Type();
  }
}
