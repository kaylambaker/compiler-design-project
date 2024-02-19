JAVA=java
JAVAC=javac
JFLEX=$(JAVA) -jar jflex-full-1.8.2.jar
CUPJAR=./java-cup-11b.jar
CUP=$(JAVA) -jar $(CUPJAR)
CP=.:$(CUPJAR)

uefault: run

.SUFFIXES: $(SUFFIXES) .class .java

.java.class:
		$(JAVAC) -cp $(CP) $*.java

# all: Lexer.java parser.java parserD.java

FILE=    Lexer.java      parser.java    sym.java \
    LexerTest.java  Binaryop.java ScannerTest.java Token.java \
		Name.java Expr.java Unaryop.java Stmt.java IfEnd.java \
	  Fielddecl.java Methoddecl.java Memberdecls.java \
		Fielddecls.java Methoddecls.java Readlist.java \
		Printlist.java Argdecllist.java Type.java TypeCheckException.java \
		TypeCheckingTest.java 

TEST= p3tests/badDec p3tests/badInc p3tests/badNegation \
			p3tests/badString p3tests/badTernaryCond \
			p3tests/badTernaryTypes p3tests/boolToFloat \
			p3tests/boolToInt p3tests/callNonExistFunc \
			p3tests/charToFloat p3tests/charToInt \
			p3tests/floatToInt p3tests/fullValidProgram \
			p3tests/incompatBinary p3tests/intArrayToBoolArray \
			p3tests/noReturn p3tests/reassignFinal \
			p3tests/redefMethod p3tests/redefVar \
			p3tests/redefVarAsMethod p3tests/returnTypeBad \

run: all
		for file in $(TEST) ; do \
			$(JAVA) -cp $(CP) TypeCheckingTest $$file.as > $$file-output.txt ;\
    done


# run: $(TEST).as

all: Lexer.java parser.java $(FILE:java=class)

$(TEST).as: all

clean:
		rm -f *.class *~ *.bak Lexer.java parser.java sym.java

Lexer.java: tokens.jflex
		$(JFLEX) tokens.jflex

parser.java: grammar.cup
		$(CUP) -interface < grammar.cup

parserD.java: grammar.cup
		$(CUP) -interface -dump < grammar.cup

