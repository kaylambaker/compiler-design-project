public class Argdecl extends Token {
  String type, id;
  boolean isArray;

  public Argdecl(String type, String id, boolean isArray) {
    this.type = type;
    this.id = id;
    this.isArray = isArray;
  }

  public String toString(int t) {
    String s = getTabs(0) + this.type + " " + this.id;
    if (this.isArray)
      s += "[]";
    return s;
  }

  public String getType(){
    return this.type;
  }

  public Type typeCheck() throws TypeCheckException {
    SymbolTable.Entry t = symbolTable.new Entry(this.type, false, this.isArray);
    symbolTable.addVar(this.id, t);
    return new Type();
  }
}
