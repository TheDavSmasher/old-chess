import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.*;
public class Repl {

    public String evaluate() {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        ChessClient.run(out);

        return "";
    }

    private void printPrompt(PrintStream out) {
        out.print("\n" + UNSET_TEXT_COLOR + ">> " + SET_TEXT_COLOR_GREEN);
    }
}
