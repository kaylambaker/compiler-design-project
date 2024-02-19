public class Methoddecl extends Token {
  String returntype, id;
  Argdecllist argdecls;
  Stmts stmts;
  Fielddecls fielddecls;
  boolean optionalsemi;

  public Methoddecl(String returntype, String id, Argdecllist argdecls,
                    Fielddecls fielddecls, Stmts stmts, boolean optionalsemi) {
    this.returntype = returntype;
    this.id = id;
    this.argdecls = argdecls;
    this.fielddecls = fielddecls;
    this.stmts = stmts;
    this.optionalsemi = optionalsemi;
  }

  public String toString(int t) {
    String s = "";
    s += getTabs(t) + this.returntype + " " + this.id + "(";
    s += this.argdecls.toString(0);
    s += ")\n" + getTabs(t) + "{\n";
    s += this.fielddecls.toString(t + 1);
    s += this.stmts.toString(t + 1);
    s += getTabs(t) + "}";
    if (this.optionalsemi)
      s += ";";
    return s;
  }

  public Type typeCheck() throws TypeCheckException {
    try {
      SymbolTable.Entry t =
          symbolTable.new Entry(this.returntype, this.argdecls);
      symbolTable.addVar(this.id, t);
      symbolTable.startScope(); // argdecl scope
      this.argdecls.typeCheck();
      symbolTable.startScope(); // fielddecl scope
      this.fielddecls.typeCheck();
      Type returnType = this.stmts.typeCheck();
      if (returnType.equals("none") && this.returntype.equals("void")) {
        symbolTable.endScope();
        symbolTable.endScope();
        return new Type();
      }
      // if (!this.returntype.equals(returnType.type())) {
      if (!returnType.canBeCoerced(this.returntype)) {
        if (returnType.equals("none"))
          throw new WrongReturnException(this.returntype, "void", this.id);
        throw new WrongReturnException(this.returntype, returnType.type(), this.id);
      }
      if (returnType.isConditional())
        throw new WrongReturnException(this.returntype, "void", this.id);
      symbolTable.endScope(); // fielddecls scope
      symbolTable.endScope(); // argdecl scope
      return new Type();
    } catch (DiffReturnException d) {
      if (!d.type1.equals(this.returntype)) {
        if (d.type1.equals("none"))
          throw new WrongReturnException(this.returntype, "void", this.id);
        throw new WrongReturnException(this.returntype, d.type1, this.id);
      } else {
        if (d.type2.equals("none"))
          throw new WrongReturnException(this.returntype, "void", this.id);
        throw new WrongReturnException(this.returntype, d.type2, this.id);
      }
    } catch (TypeCheckException egg) {
      egg.methoddeclstr = this.id;
      egg.methoddecltype = this.returntype;
      throw egg;
    }
  }
}
