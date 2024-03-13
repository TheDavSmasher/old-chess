import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import static ui.EscapeSequences.*;
public class Repl {

    private final ChessClient client;

    public Repl() {
        client = new ChessClient();
    }

    public void run() {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.println("Intro");
        client.help(out);

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt(out);
            String line = scanner.nextLine();

            try {
                result = client.evaluate(line, out);
            } catch (Throwable e) {
                out.print(e);
            }
        }
        out.println();
    }

    private void printPrompt(PrintStream out) {
        out.print("\n" + UNSET_TEXT_COLOR + ">>> ");
    }
}
