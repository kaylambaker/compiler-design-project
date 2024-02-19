import java.util.ArrayList;

public class Args extends Token {
  ArrayList<Expr> args;

  public Args(Expr expr) {
    this.args = new ArrayList<>() {
      { add(expr); }
    };
  }

  public Args() { this.args = new ArrayList<>(); }

  public Args prepend(Expr expr) {
    this.args.add(0, expr);
    return this;
  }

  // returns comma separated list
  public String toString(int t) {
    String s = "";
    for (int i = 0; i < this.args.size(); i++) {
      if (i == this.args.size() - 1) // if last
        s += this.args.get(i).toString(0);
      else
        s += this.args.get(i).toString(0) + ", ";
    }
    return s;
  }

  public ArrayList<Expr> list() { return this.args; }

  public int size() { return this.args.size(); }

  public Expr get(int i) { return this.args.get(i); }

  public Type typeCheck() throws TypeCheckException {
    for (Expr e : this.args)
      e.typeCheck();
    return new Type();
  }
}
