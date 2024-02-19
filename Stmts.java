import java.util.ArrayList;

public class Stmts extends Token {
  ArrayList<Stmt> stmts;

  public Stmts() { this.stmts = new ArrayList<>(); }

  public Stmts prepend(Stmt stmt) {
    this.stmts.add(0, stmt);
    return this;
  }

  public String toString(int t) {
    String s = "";
    for (Stmt stmt : this.stmts)
      s += stmt.toString(t) + "\n";
    return s;
  }

  public Type typeCheck() throws TypeCheckException {
    ArrayList<Type> returnTypes = new ArrayList<>();
    for (Stmt s : this.stmts) {
      Type t = s.typeCheck();
      if (!t.equals("none"))
        returnTypes.add(s.typeCheck());
    }
    if (returnTypes.size() == 0)
      return new Type();
    // check all same return type
    for (int i = 1; i < returnTypes.size(); i++) {
      if (!returnTypes.get(i - 1).equals(returnTypes.get(i)))
        throw new DiffReturnException(returnTypes.get(i - 1).type(),
                                returnTypes.get(i).type());
    }
    // check there is a gaurenteed return type
    int gaurenteed = -1;
    for (int i = 0; i < returnTypes.size(); i++) {
      Type t = returnTypes.get(i);
      if (!t.isConditional()) {
        gaurenteed = i;
        break;
      }
    }
    if (gaurenteed < 0) { // no gaurenteed return
      return returnTypes.get(0);
    } else {
      return returnTypes.get(gaurenteed);
    }
  }
}
