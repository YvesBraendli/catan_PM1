package ch.zhaw.catan;

import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;
import org.beryx.textio.TextTerminal;

public class InputParser {
    TextIO textIO;
    TextTerminal<?> textTerminal;

    public InputParser() {
        textIO = TextIoFactory.getTextIO();
        textTerminal = textIO.getTextTerminal();
    }
    
}
