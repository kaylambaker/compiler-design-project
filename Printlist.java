import java.util.ArrayList;

public class Printlist extends Token {
  ArrayList<Expr> printlist;

  public Printlist(Expr expr) {
    this.printlist = new ArrayList<>() {
      { add(expr); }
    };
  }

  public Printlist() { this.printlist = new ArrayList<>(); }

  public Printlist prepend(Expr expr) {
    this.printlist.add(0, expr);
    return this;
  }

  public String toString(int t) {
    String s = "";
    for (int i = 0; i < this.printlist.size(); i++) {
      if (i == this.printlist.size() - 1)
        s += this.printlist.get(i).toString(0);
      else
        s += this.printlist.get(i).toString(0) + ", ";
    }
    return s;
  }

  public Type typeCheck() throws TypeCheckException {
    for (Expr e : this.printlist) {
      Type s = e.typeCheck();
      if (s.equals("void") && e.isFunction())
        throw new PrintlistException(e.toString(0), "void");
      if (e.isVar()) {
        SymbolTable.Entry t = symbolTable.get(e.name.getID());
        if (!e.isArray() && t.isArray())
          throw new PrintlistException(e.toString(0), "non-dereferenced array");
      }
    }
    return new Type();
  }
}
