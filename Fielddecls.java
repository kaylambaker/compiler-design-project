import java.util.ArrayList;

public class Fielddecls extends Token {
  ArrayList<Fielddecl> fielddecls;

  public Fielddecls(ArrayList<Fielddecl> fielddecls) {
    this.fielddecls = fielddecls;
  }

  public Fielddecls(Fielddecl fielddecl) {
    this.fielddecls = new ArrayList<>() {
      { add(fielddecl); }
    };
  }

  public Fielddecls() { this.fielddecls = new ArrayList<>(); }

  public Fielddecls append(Fielddecl fielddecl) {
    this.fielddecls.add(fielddecl);
    return this;
  }

  public String toString(int t) {
    String s = "";
    for (Fielddecl fielddecl : this.fielddecls)
      s += fielddecl.toString(t) + "\n";
    return s;
  }

  public Type typeCheck() throws TypeCheckException {
    for (Fielddecl f : this.fielddecls)
      f.typeCheck();
    return new Type();
  }
}
