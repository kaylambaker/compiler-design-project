class TypeCheckException extends Exception {
  protected String err, methoddeclstr, methoddecltype;

  public TypeCheckException() {
  }

  public TypeCheckException(String s) {
    this.err = s;
  }

  public String toString() {
    return this.err;
  }
}

class FinalException extends TypeCheckException {
  String stmt;

  public FinalException(String stmt) {
    super("cannot change value of final: %s");
    this.stmt = stmt;
  }

  public String toString() {
    return String.format(this.err, this.stmt);
  }
}

class MismatchedException extends TypeCheckException {
  String type1, type2, stmt;

  public MismatchedException(String type1, String type2, String stmt) {
    super("assignment must be of same type, cannot assign %s to %s: %s");
    this.type1 = type1;
    this.type2 = type2;
    this.stmt = stmt;
  }

  public String toString() {
    return String.format(this.err, this.type1, this.type2, this.stmt);
  }
}

class ReadException extends TypeCheckException {
  String errType, arg, stmt;

  public ReadException(String errType, String arg, String stmt) {
    super("cannot read %s %s: %s");
    this.errType = errType;
    this.arg = arg;
    this.stmt = stmt;
  }

  public String toString() {
    return String.format(this.err, this.errType, this.arg, this.stmt);
  }
}

class PrintException extends TypeCheckException {
  String errType, stmt, arg;

  public PrintException(String errType, String arg, String stmt) {
    super("cannot print %s %s: %s");
    this.errType = errType;
    this.stmt = stmt;
    this.arg = arg;
  }

  public String toString() {
    return String.format(this.err, this.errType, this.arg, this.stmt);
  }
}

class IndexException extends TypeCheckException {
  String expr;

  public IndexException(String expr) {
    super("array index must be type int: %s");
    this.expr = expr;
  }

  public String toString() {
    return String.format(this.err, this.expr);
  }
}

class MathException extends TypeCheckException {
  String type1, type2, expr;

  public MathException(String type1, String type2, String expr) {
    super("cannot perform arithmetic on types %s and %s: %s");
    this.type1 = type1;
    this.type2 = type2;
    this.expr = expr;
  }

  public MathException(String type1, String expr) {
    super("cannot perform arithmetic on type %s: %s");
    this.type1 = type1;
    this.expr = expr;
  }

  public String toString() {
    return this.type2 == null
        ? String.format(this.err, this.type1, this.expr)
        : String.format(this.err, this.type1, this.type2, this.expr);
  }
}

class NotFuncException extends TypeCheckException {
  String var, expr;

  public NotFuncException(String var, String expr) {
    super("%s is not a function: %s");
    this.var = var;
    this.expr = expr;
  }

  public String toString() {
    return String.format(this.err, this.var, this.expr);
  }
}

class WrongReturnException extends TypeCheckException {
  String expectedRet, actualRet, func;

  public WrongReturnException(String expectedRet, String actualRet, String func) {
    super("function %s expected return type %s, got %s");
    this.expectedRet = expectedRet;
    this.func = func;
    this.actualRet = actualRet;
  }

  public String toString() {
    return String.format(this.err, this.func,
        this.expectedRet, this.actualRet);
  }
}

class DiffReturnException extends TypeCheckException {
  String type1, type2;

  public DiffReturnException(String type1, String type2) {
    this.type1 = type1;
    this.type2 = type2;
  }
}

class PrintlistException extends TypeCheckException {
  String arg, type;

  public PrintlistException(String arg, String type) {
    this.arg = arg;
    this.type = type;
  }
}

class ReadlistException extends TypeCheckException {
  String arg, type;

  public ReadlistException(String arg, String type) {
    this.arg = arg;
    this.type = type;
  }
}

class WrongArgCountException extends TypeCheckException {
  int expected, actual;
  String stmt;

  public WrongArgCountException(int expected, int actual, String stmt) {
    super("wrong number of arguments to function, expected %d got %d: %s");
    this.expected = expected;
    this.actual = actual;
    this.stmt = stmt;
  }

  public String toString() {
    return String.format(this.err, this.expected, this.actual, this.stmt);
  }
}

class RedeclaredException extends TypeCheckException {
  String var;

  public RedeclaredException(String var) {
    super("redeclared variable: %s");
    this.var = var;
  }

  public String toString() {
    return String.format(this.err, this.var);
  }
}

class UndeclaredException extends TypeCheckException {
  String var;

  public UndeclaredException(String var) {
    super("undeclared variable: %s");
    this.var = var;
  }

  public String toString() {
    return String.format(this.err, this.var);
  }
}

class CannotCompareException extends TypeCheckException {
  String type1, type2, expr, op;

  public CannotCompareException(String type1, String type2, String op, String expr) {
    super("cannot compare types %s and %s using %s operator: %s");
    this.type1 = type1;
    this.type2 = type2;
    this.expr = expr;
    this.op = op;
  }

  public String toString() {
    return String.format(this.err, this.type1, this.type2, this.op, this.expr);
  }
}

class BoolException extends TypeCheckException {
  String type1, type2, expr, op;

  public BoolException(String type1, String type2, String op, String expr) {
    super(
        "cannot perform boolean operation types %s and %s using %s operator: %s");
    this.type1 = type1;
    this.type2 = type2;
    this.expr = expr;
    this.op = op;
  }

  public String toString() {
    return String.format(this.err, this.type1, this.type2, this.op, this.expr);
  }
}

class UopException extends TypeCheckException {
  String op, expr, type;

  public UopException(String op, String type, String expr) {
    super("cannot use operator %s on type %s: %s");
    this.op = op;
    this.expr = expr;
    this.type = type;
  }

  public String toString() {
    return String.format(this.err, this.op, this.type, this.expr);
  }
}

class TernaryBoolException extends TypeCheckException {
  String expr, type;

  public TernaryBoolException(String type, String expr) {
    super(
        "first expression in ternary expression must be coercable to bool, %s is not coercable to bool: %s");
    this.type = type;
    this.expr = expr;
  }

  public String toString() {
    return String.format(this.err, this.type, this.expr);
  }
}

class TernaryEqException extends TypeCheckException {
  String type1, type2, expr;

  public TernaryEqException(String type1, String type2, String expr) {
    super(
        "second and third expressions in ternary expression must be the same, %s and %s are not the same type: %s");
    this.type1 = type1;
    this.type2 = type2;
    this.expr = expr;
  }

  public String toString() {
    return String.format(this.err, this.type1, this.type2, this.expr);
  }
}

class StmtCondException extends TypeCheckException {
  String stmtType, exprType, expr;

  public StmtCondException(String stmtType, String exprType, String expr) {
    super(
        "condition in %s statement must be coercable to bool, type %s is not coercable to bool: %s");
    this.stmtType = stmtType;
    this.exprType = exprType;
    this.expr = expr;
  }

  public String toString() {
    return String.format(this.err, this.stmtType, this.exprType, this.expr);
  }
}

class FuncArgTypeException extends TypeCheckException {
  String expected, actual, func, stmt;

  public FuncArgTypeException(String expected, String actual, String func,
      String stmt) {
    super("function %s expected argument of type %s got %s: %s");
    this.expected = expected;
    this.actual = actual;
    this.func = func;
    this.stmt = stmt;
  }

  public String toString() {
    return String.format(this.err, this.func, this.expected, this.actual,
        this.stmt);
  }
}

class StrArrException extends TypeCheckException {
  String expr;

  public StrArrException(String expr) {
    super("cannot concatenate string with non-dereferenced array: %s");
    this.expr = expr;
  }

  public String toString() {
    return String.format(this.err, this.expr);
  }
}
