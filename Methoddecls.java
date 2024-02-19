import java.util.ArrayList;

public class Methoddecls extends Token {
  ArrayList<Methoddecl> methoddecls;

  public Methoddecls(Methoddecl methoddecl) {
    this.methoddecls = new ArrayList<>() {
      { add(methoddecl); }
    };
  }

  public Methoddecls() { this.methoddecls = new ArrayList<>(); }

  public Methoddecls prepend(Methoddecl methoddecl) {
    this.methoddecls.add(0, methoddecl);
    return this;
  }

  public String toString(int t) {
    String s = "";
    for (Methoddecl methoddecl : this.methoddecls)
      s += methoddecl.toString(t) + "\n";
    return s;
  }

  public Type typeCheck() throws TypeCheckException{
    for (Methoddecl m : this.methoddecls)
      m.typeCheck();
    return new Type();
  }
}
