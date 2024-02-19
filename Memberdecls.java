public class Memberdecls extends Token {
  Fielddecls fielddecls;
  Methoddecls methoddecls;

  public Memberdecls(Fielddecls fielddecls, Methoddecls methoddecls) {
    this.fielddecls = fielddecls;
    this.methoddecls = methoddecls;
  }

  public String toString(int t) {
    String s = "";
    s += this.fielddecls.toString(t);
    s += this.methoddecls.toString(t);
    return s;
  }

  public Type typeCheck() throws TypeCheckException {
    this.fielddecls.typeCheck();
    this.methoddecls.typeCheck();
    return new Type();
  }
}
