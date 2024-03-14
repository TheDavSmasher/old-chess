import chess.*;
import model.GameData;
import ui.ChessUI;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;

public class ChessClient {
    private String authToken;

    public ChessClient() {
        authToken = null;
    }

    public String evaluate(String input, PrintStream out) {
        String[] tokens = input.toLowerCase().split(" ");
        int command = (tokens.length > 0) ? Integer.parseInt(tokens[0]) : 0;
        String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
        if (loggedInCheck()) {
            return switch (command) {
                case 1 -> listGames(out);
                case 2 -> createGame(out, params);
                case 3 -> joinGame(out, params);
                case 4 -> observeGame(out, params);
                case 5 -> logout(out);
                default -> help(out);
            };
        }
        return switch (command) {
            case 1 -> register(out, params);
            case 2 -> signIn(out, params);
            case 3 -> "quit";
            default -> help(out);
        };
    }

    public String help(PrintStream out) {
        if (!loggedInCheck()) {
            out.print( """
                   1 - Register
                   2 - Login
                   3 - Quit
                   
                   0 - Help""");
        } else {
            out.print("""
               1 - List Games
               2 - Create Game
               3 - Join Game
               4 - Observe Game
               5 - Logout
               
               0 - Help""");
        }
        return "";
    }

    private String register(PrintStream out, String[] params) {
        if (params.length < 3) {
            out.print("Please provide a username, password, and email.\nFormat: 1 username password email");
            return "Retry";
        }
        String username = params[0];
        String password = params[1];
        String email = params[2];

        authToken = ServerFacade.register(username, password, email).authToken();
        help(out);

        return "Welcome new user";
    }

    private String signIn(PrintStream out, String[] params) {
        if (params.length < 2) {
            out.print("Please provide a username and password.\nFormat: 2 username password");
            return "Retry";
        }
        String username = params[0];
        String password = params[1];

        authToken = ServerFacade.login(username, password).authToken();
        help(out);

        return "Welcome back";
    }

    private String listGames(PrintStream out) {
        ArrayList<GameData> allGames = ServerFacade.listGames(authToken);
        out.print("Games:");
        int i = 0;
        for (GameData data : allGames) {
            String white = (data.whiteUsername() != null) ? data.whiteUsername() : "No one";
            String black = (data.blackUsername() != null) ? data.blackUsername() : "No one";
            out.print("\n  " + (++i) + ". " + data.gameName() + ": " + white + " vs " + black);
        }
        return "Here's the games";
    }

    private String createGame(PrintStream out, String[] params) {
        if (params.length < 1) {
            out.print("Please provide a game ID.\\nFormat: 2 gameName");
            return "Retry";
        }
        ServerFacade.createGame(authToken, params[1]);
        return "Created new game";
    }

    private String joinGame(PrintStream out, String[] params) {
        if (params.length < 2) {
            out.print("Please provide a game ID and color.\nFormat: 3 gameID 1/2");
            return "Retry";
        }
        GameData gameData = ServerFacade.joinGame(authToken, params[0], Integer.parseInt(params[1]));
        //TODO check if null
        ChessGame testGame = gameData.game();
        String[][] board = ChessUI.getChessBoardAsArray(testGame.getBoard());

        ChessUI.printChessBoard(out, board, true);
        return "You joined";
    }

    private String observeGame(PrintStream out, String[] params) {
        if (params.length < 1) {
            out.print("Please provide a game ID.\nFormat: 4 gameID");
            return "Retry";
        }
        GameData gameData = ServerFacade.joinGame(authToken, params[0], Integer.parseInt(params[1]));
        //TODO check if null
        ChessGame testGame = gameData.game();
        String[][] board = ChessUI.getChessBoardAsArray(testGame.getBoard());
        ChessUI.printChessBoard(out, board, false);

        return "You're now watching";
    }

    private String logout(PrintStream out) {
        ServerFacade.logout(authToken);
        authToken = null;
        help(out);

        return "See you later!";
    }

    private boolean loggedInCheck() {
        return authToken != null;
    }
}
