import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SymbolTable {
  public class Entry {
    String dataType;
    boolean isFinal, isArray, isFunction;
    Argdecllist argdecls;
    int arrSize;

    public Entry() {}

    public Entry(String dataType) { this.dataType = dataType; }

    public Entry(String dataType, Argdecllist argdecls) {
      this.dataType = dataType;
      this.argdecls = argdecls;
      this.isFunction = true;
    }

    public Entry(String dataType, boolean isFinal, boolean isArray) {
      this.dataType = dataType;
      this.isFinal = isFinal;
      this.isArray = isArray;
    }

    public Entry(String dataType, String id, boolean isArray, int arrSize) {
      this.dataType = dataType;
      this.isArray = isArray;
      this.arrSize = arrSize;
    }

    public final boolean equals(Entry t) { return this.dataType == t.dataType; }

    public final boolean eqDataType(String s) {
      return this.dataType.equals(s);
    }

    public final boolean isFinal() { return this.isFinal; }

    public final boolean isArray() { return this.isArray; }

    public final boolean isFunction() { return this.isFunction; }

    public final String datatype() { return this.dataType; }

    public final Argdecllist argdecls() { return this.argdecls; }
  }

  // (id, type)
  ArrayList<HashMap<String, Entry>> table;

  public SymbolTable() { this.table = new ArrayList<>(); }

  public void startScope() { this.table.add(new HashMap<String, Entry>()); }

  public void endScope() { this.table.remove(this.table.size() - 1); }

  public void addVar(String id, Entry type) throws TypeCheckException {
    int scopeIndex = this.table.size() - 1;
    HashMap<String, Entry> scopeTable = this.table.get(scopeIndex);
    for (Map.Entry<String, Entry> e : scopeTable.entrySet()) {
      if (e.getKey().equals(id))
        throw new RedeclaredException(id);
    }
    scopeTable.put(id, type);
  }

  public Entry get(String s) throws TypeCheckException {
    for (int i = this.table.size() - 1; i >= 0; --i) {

      for (Map.Entry<String, Entry> p : this.table.get(i).entrySet()) {
        if (p.getKey().equals(s))
          return p.getValue();
      }
    }
    throw new UndeclaredException(s);
  }
}
