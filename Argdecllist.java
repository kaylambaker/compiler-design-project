import java.util.ArrayList;

public class Argdecllist extends Token{
  ArrayList<Argdecl> argdecllist;

  public Argdecllist(Argdecl argdecl) {
    this.argdecllist = new ArrayList<>() {
      { add(argdecl); }
    };
  }

  public Argdecllist() { this.argdecllist = new ArrayList<>(); }

  public Argdecllist prepend(Argdecl argdecl) {
    this.argdecllist.add(0, argdecl);
    return this;
  }

  public String toString(int t) {
    String s = "";
    for (int i = 0; i < this.argdecllist.size(); i++) {
      if (i == this.argdecllist.size() - 1)
        s += this.argdecllist.get(i).toString(0);
      else
        s += this.argdecllist.get(i).toString(0) + ", ";
    }
    return s;
  }

  public Type typeCheck() throws TypeCheckException {
    for (Argdecl a : this.argdecllist)
      a.typeCheck();
    return new Type();
  }

  public int size() { return this.argdecllist.size(); }

  public Argdecl get(int i) { return this.argdecllist.get(i); }
}
