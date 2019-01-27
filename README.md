# IJVM ISA emulator

## Intro
IJVM is an instruction set architecture created by Andrew Tanenbaum for his MIC-1 architecture. It is used to teach assembly basics in his book Structured Computer Organization. IJVM is mostly a subset of the JVM assembly language that is used in the Java platform. For the first year course "project application development" we had create a virtual machine in java to emulate the IJVM assembly.
Grade: 7.6/10

## Useful information

In the folder src/main/java/pad/ijvm/ you can find the source code of the java program.  
The tests can be found in src/test/java/pad/ijvm/

To build the project in the gradle wrapper:
```bash
./gradlew build
```

To run:
```bash
./gradlew run
```
To test the program:
```bash
./gradlew test
```

## Known bugs

1. One of the advanced test does not pass because I didn't correctly implement method invocation.

The theory: _"Before invoking a method, the caller also pushes the method arguments
to the stack. Thereafter INVOKEVIRTUAL is called with one argument, which
is a reference to a pointer in the constant pool. The pointer in the constant
pool, in turn points to the first address of the method area. The method
area first contains two shorts (2 byte numbers), the first one signifying the
number of arguments the method expects, and the second one being the
local variable area size. The fifth byte in the method area is the actual
first instruction to be executed."_

The problem is, as far as I am concerned, with one of the shorts. I don't use the local variable area size. In line 193 of the IJVM class the short is being explicitly skipped: ```text.skipShort();```

2. The "Short" class I created was an illegal override of the built-in java API class Short. This may result in unwanted behaviour
