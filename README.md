# Nonogram Auto Solver
Nonogram Auto-Solver is a Java toolkit that can solve the picture logic puzzle automatically with step-by-step visualization in three styles.

```
[Usage] nonogram <number_file_name> [output]
    output: 0 --- in plain text (default)
            1 --- in plain text with ANSI color escape sequence
            2 --- draw to screen
```

1. In Plain Text: Print every step and final result to the console with [AsciiTable](https://github.com/vdmeer/asciitable).
2. In Plain Text with ANSI color escape sequence: Print every step and final result to the console with ANSI color escape sequence with [JAnsi](https://github.com/fusesource/jansi).
3. Draw to Screen: Create a window and draw every step plus final result with [Processing](https://github.com/processing/processing).

The Number file must be formatted as following with no comments:

```
line1  >>>      15    // Column size. Must be 15 lines followed e.g.
line2  >>>      1 2 1 // Every column's clue from left to right by each line.
line3  >>>      3 4 5 // Numbers are splitted by one space from top to bottom in a line. 
line4  >>>      12
...    >>>      ...
...    >>>      ...
line17 >>>      10    // Row size. Must be 10 lines followed e.g.
line18 >>>      13 1  // Every row's clue from top to bottom by each line.  
line19 >>>      1 2 1 // Numbers are splitted by one space from left to right in a line. 
...    >>>      ...
...    >>>      ...
```