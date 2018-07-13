# IndentManager
Manages size of indentation based on levels

## Construction
IndentManager getInstance() - Returns an instance of IndentManager with levels set at the default of two spaces

IndentManager getInstance(int spacesPerLevel) - Returns instance with levels set to specified number of spaces

## Indentation Level
void increment() - Increments the indentation one level

void increment(int levels) - Increments the indentation the specified number of levels

void decrement() - Decrement the indentation one level

void decrement(int levels) - Decrement the indentation the specified number of levels

bool isAtRootLevel() - There is currently NO indentation

String format(String line) - Return 'line' prepended with the current indentation