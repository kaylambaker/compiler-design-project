/*-***
 *
 * This file defines a stand-alone lexical analyzer for a subset of the Pascal
 * programming language.  This is the same lexer that will later be integrated
 * with a CUP-based parser.  Here the lexer is driven by the simple Java test
 * program in ./PascalLexerTest.java, q.v.  See 330 Lecture Notes 2 and the
 * Assignment 2 writeup for further discussion.
 *
 */


import java_cup.runtime.*;


%%
/*-*
 * LEXICAL FUNCTIONS:
 */

%cup
%line
%column
%unicode
%class Lexer

%{

/**
 * Return a new Symbol with the given token id, and with the current line and
 * column numbers.
 */
Symbol newSym(int tokenId) {
    return new Symbol(tokenId, yyline, yycolumn);
}

/**
 * Return a new Symbol with the given token id, the current line and column
 * numbers, and the given token value.  The value is used for tokens such as
 * identifiers and numbers.
 */
Symbol newSym(int tokenId, Object value) {
    return new Symbol(tokenId, yyline, yycolumn, value);
}

%}


/*-*
 * PATTERN DEFINITIONS:
 */


/**
 * Implement patterns as regex here
 */
intlit=[:digit:]+
id=[:letter:]([:letter:]|[:digit:])*
legalcharchar=([^\n\r\t'\\]|(\\r|\\t|\\n|\\'|\\\\))
legalstrchar=([^\n\r\t\"\\]|(\\r|\\t|\\n|\\\"|\\\\))
strlit=\"{legalstrchar}*\"
whitespace=[ \n\t\r]
charlit=\'{legalcharchar}\'
floatlit=[:digit:]+\.[:digit:]+
comment=(\\\*)~(\*\\)|\\\\(.*)

%%
/**
 * LEXICAL RULES:
 */
/**
 * Implement terminals here, ORDER MATTERS!
 */
{comment} {}
class {return newSym(sym.CLASS, "class");}
final {return newSym(sym.FINAL, "final");}
void {return newSym(sym.VOID, "void");}
int {return newSym(sym.INT, "int");}
char {return newSym(sym.CHAR, "char");}
bool {return newSym(sym.BOOL, "bool");}
float {return newSym(sym.FLOAT, "float");}
if {return newSym(sym.IF, "if");}
else {return newSym(sym.ELSE, "else");}
while {return newSym(sym.WHILE, "while");}
read {return newSym(sym.READ, "read");}
print {return newSym(sym.PRINT, "print");}
printline {return newSym(sym.PRINTLINE, "printline");}
return {return newSym(sym.RETURN, "return");}
true {return newSym(sym.TRUE, "true");}
false {return newSym(sym.FALSE, "false");}
"/" {return newSym(sym.DIVIDE, "/");}
"(" {return newSym(sym.LPAREN, "(");}
")" {return newSym(sym.RPAREN, ")");}
"[" {return newSym(sym.LBRACKET, "[");}
"]" {return newSym(sym.RBRACKET, "]");}
";" {return newSym(sym.SEMI, ";");}
"~" {return newSym(sym.TILDE, "~");}
"--" {return newSym(sym.MINUSMINUS, "--");}
"-" {return newSym(sym.MINUS, "-");}
"++" {return newSym(sym.PLUSPLUS, "++");}
"+" {return newSym(sym.PLUS, "+");}
"{" {return newSym(sym.LCURLY, "{");}
"}" {return newSym(sym.RCURLY, "}");}
"?" {return newSym(sym.TERNARY, "?");}
":" {return newSym(sym.COLON, ":");}
"*" {return newSym(sym.TIMES, "*");}
"==" {return newSym(sym.EQEQ, "==");}
"<=" {return newSym(sym.LTEQ, "<=");}
">=" {return newSym(sym.MTEQ, ">=");}
"<" {return newSym(sym.LT, "<");}
">" {return newSym(sym.MT, ">");}
"=" {return newSym(sym.EQ, "=");}
"<>" {return newSym(sym.NOTEQ, "<>");}
"||" {return newSym(sym.OR, "||");}
"&&" {return newSym(sym.AND, "&&");}
"," {return newSym(sym.COMMA, ",");}
{id} {return newSym(sym.ID, yytext());}
{intlit} {return newSym(sym.INTLIT, new Integer(yytext()));}
{floatlit} {return newSym(sym.FLOATLIT, yytext());}
{charlit} {return newSym(sym.CHARLIT, yytext());}
{strlit} {return newSym(sym.STRLIT, yytext());}
{whitespace}    { /* Ignore whitespace. */ }
.               { System.out.println("Illegal char, '" + yytext() +
                    "' line: " + yyline + ", column: " + yychar); } 
