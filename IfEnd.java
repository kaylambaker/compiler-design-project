public class IfEnd extends Token {
  Stmt stmt;

  public IfEnd(Stmt stmt) { this.stmt = stmt; }

  public String toString(int t) {
    String s = getTabs(t) + "else\n" + this.stmt.toString(t + 1);
    return s;
  }

  public Type typeCheck() throws TypeCheckException { return this.stmt.typeCheck(); }
}
