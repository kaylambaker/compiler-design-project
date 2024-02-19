public class Type {
  String type;
  boolean isConditional;

  public Type() {
    this.type = "none";
    this.isConditional = false;
  }

  public Type(String type) {
    this.type = type;
    this.isConditional = false;
  }

  public Type(String type, boolean isConditional) {
    this.type = type;
    this.isConditional = isConditional;
  }

  public boolean equals(Type t) { return this.type.equals(t.type); }

  public boolean equals(String t) { return this.type.equals(t); }

  public boolean isConditional() { return this.isConditional; }

  public String type() { return this.type; }

  public boolean canBeCoerced(String t) {
    if (this.equals(t)) 
      return true;
    if (this.equals("int")) 
      return t.equals("bool") || t.equals("float");
    return t.equals("string");
  }

  public boolean canBeCoerced(Type t) { return this.canBeCoerced(t.type); }
}
