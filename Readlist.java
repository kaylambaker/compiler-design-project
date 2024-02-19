import java.util.ArrayList;

public class Readlist extends Token {
  ArrayList<Name> readlist;

  public Readlist(Name name) {
    this.readlist = new ArrayList<>() {
      { add(name); }
    };
  }

  public Readlist prepend(Name name) {
    this.readlist.add(0, name);
    return this;
  }

  // returns comma separated list
  public String toString(int t) {
    String s = "";
    for (int i = 0; i < this.readlist.size(); i++) {
      if (i == this.readlist.size() - 1)
        s += this.readlist.get(i).toString(0);
      else
        s += this.readlist.get(i).toString(0) + ", ";
    }
    return s;
  }

  public Type typeCheck() throws TypeCheckException {
    for (int i = 0; i < this.readlist.size(); i++) {
      Name n = this.readlist.get(i);
      SymbolTable.Entry t = symbolTable.get(n.getID());
      n.typeCheck();
      if (t.isFinal())
        throw new ReadlistException(this.toString(0),"final");
      if (t.isFunction())
        throw new ReadlistException(this.toString(0),"function");
      if (t.isArray() && !n.isArray()) // non dereferenced array
        throw new ReadlistException(this.toString(0),"non-dereferenced array");
    }
    return new Type();
  }
}
