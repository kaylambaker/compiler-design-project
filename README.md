# Overview

This project implements a lexer, parser, and type checking for the grammar below using Java, JFlex, and CUP Parser Generator.

# Grammar

Program $\rightarrow$ class id { Memberdecls }

Memberdecls $\rightarrow$ Fielddecls Methoddecls

Fielddecls $\rightarrow$ Fielddecl Fielddecls | $\lambda$

Methoddecls $\rightarrow$ Methoddecl Methoddecls | $\lambda$

Fielddecl $\rightarrow$ Optionalfinal Type id Optionalexpr;

&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;| Type id [ intlit ]

Optionalfinal $\rightarrow$ final | $\lambda$

Optionalexpr $\rightarrow$ = Expr | $\lambda$

Methoddecl $\rightarrow$ Returntype id ( Argdecls ) { Fielddecls Stmts } Optionalsemi

Optionalsemi $\rightarrow$ ; | $\lambda$

Returntype $\rightarrow$ Type | void

Type $\rightarrow$ int | char | bool | float

Argdecls $\rightarrow$ ArgdeclList | $\lambda$

ArgdeclList $\rightarrow$ Argdecl, ArgdeclList | Argdecl

Argdecl $\rightarrow$ Type id | Type id []

Stmts $\rightarrow$ Stmt Stmts | $\lambda$

Stmt $\rightarrow$ if ( Expr ) Stmt OptionalElse | while ( Expr ) Stmt | Name = Expr

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; | read ( Readlist ); | print ( Printlist ); | printline ( Printlinelist );

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; | id (); | id ( Args ); | return ; | retrn Expr ; | Name++; | Name--;

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; | { Fielddecls Stmts } Optionalsemi

OptionalElse $\rightarrow$ else Stmt | $\lambda$

Name $\rightarrow$ id | id [ Expr ]

Args $\rightarrow$ Expr, Args | Expr

Readlist $\rightarrow$ Name, Readlist | Name

Printlist $\rightarrow$ Expr, Printlist | Expr

Printlinelist $\rightarrow$ Printlist | $\lambda$

Expr $\rightarrow$ Name | id () | id ( Args ) | intlit | charlit | strlit | floatlit | true | false 

&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;| ( Expr ) | ~ Expr | - Expr | + Expr | ( Type ) Expr

&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;| Expr Binaryop Expr | ( Expr ? Expr : Expr )

Binaryop $\rightarrow$ * | / | + | - | < | > | <= | >= | == | <> | \|\| | &&

# Build and run

This project was developed and tested on Linux. 

To build and run the test files, use the ```make run```  command or just ```make```. ```make run``` will run the test files in the directory p3tests. The output files will be put in the p3tests directory and will be called ```<test name>-output.txt```

To just build the project use the command ```make all```. To run your own input files after building, use the command ```java -cp .:./java-cup-11b.jar TypeCheckingTest <input file>```. The program will write output to System.err and System.out.
