package client;

import chess.*;
import model.dataAccess.GameData;
import ui.ChessUI;

import java.io.IOException;
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
        int command;
        try {
            command = (tokens.length > 0) ? Integer.parseInt(tokens[0]) : 0;
        } catch (NumberFormatException e) {
            help(out, false);
            return "Wrong option";
        }
        String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
        if (loggedInCheck()) {
            return switch (command) {
                case 1 -> listGames(out);
                case 2 -> createGame(out, params);
                case 3 -> joinGame(out, params);
                case 4 -> observeGame(out, params);
                case 5 -> logout(out);
                default -> help(out, false);
            };
        }
        return switch (command) {
            case 1 -> register(out, params);
            case 2 -> signIn(out, params);
            case 3 -> "quit";
            default -> help(out, false);
        };
    }

    public String help(PrintStream out, boolean simple) {
        if (simple) {
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
        } else {
            if (!loggedInCheck()) {
                out.print( """
                   1 - Register: creates a new user in the database. Username must be unique.
                       Format: 1 username password email
                   2 - Login: logs in to the server with a pre-registered username with its corresponding password.
                       Format: 2 username password
                   3 - Quit: exit out of the client.
                   
                   0 - Help: print this menu again. Also prints out if input is beyond what's accepted.""");
            } else {
                out.print("""
               1 - List Games: show all games that are currently being hosted in the server.
               2 - Create Game: create a new game in the database with a name. The game's name can include spaces.
                   Format: 2 gameName
               3 - Join Game: join an existing game with as a specific player color.
                   Format: 3 white/black gameID
               4 - Observe Game: see the current state of a game without becoming a player,
                   Format: 4 gameID
               5 - Logout: leave your current session and return to login menu.
               
               0 - Help: print this menu again. Also prints out if input is beyond what's accepted.""");
            }
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

        try {
            authToken = ServerFacade.register(username, password, email).authToken();
        } catch (IOException e) {
            out.print(e.getMessage());
            return "Error Caught";
        }
        help(out, true);

        return "Welcome new user";
    }

    private String signIn(PrintStream out, String[] params) {
        if (params.length < 2) {
            out.print("Please provide a username and password.\nFormat: 2 username password");
            return "Retry";
        }
        String username = params[0];
        String password = params[1];

        try {
            authToken = ServerFacade.login(username, password).authToken();
        } catch (IOException e) {
            out.print(e.getMessage());
            return "Error Caught";
        }
        help(out, true);

        return "Welcome back";
    }

    private String listGames(PrintStream out) {
        ArrayList<GameData> allGames;
        try {
            allGames = ServerFacade.listGames(authToken);
        } catch (IOException e) {
            out.print(e.getMessage());
            return "Error Caught";
        }
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
        try {
            String fullName = stringFromParams(params);
            ServerFacade.createGame(authToken, fullName);
        } catch (IOException e) {
            out.print(e.getMessage());
            return "Error Caught";
        }
        return "Created new game";
    }

    private String joinGame(PrintStream out, String[] params) {
        if (params.length < 2) {
            out.print("Please provide a game ID and color.\nFormat: 3 WHITE/BLACK gameID");
            return "Retry";
        }
        try {
            ServerFacade.joinGame(authToken, params[0], Integer.parseInt(params[1]));
        } catch (IOException e) {
            out.print(e.getMessage());
            return "Error Caught";
        }
        ChessGame testGame = new ChessGame();
        String[][] board = ChessUI.getChessBoardAsArray(testGame.getBoard());
        boolean white = params[0].equalsIgnoreCase("white");
        ChessUI.printChessBoard(out, board, white);
        return "You joined";
    }

    private String observeGame(PrintStream out, String[] params) {
        if (params.length < 1) {
            out.print("Please provide a game ID.\nFormat: 4 gameID");
            return "Retry";
        }
        try {
            ServerFacade.observeGame(authToken, Integer.parseInt(params[0]));
        } catch (IOException e) {
            out.print(e.getMessage());
            return "Error Caught";
        }
        ChessGame testGame = new ChessGame();
        String[][] board = ChessUI.getChessBoardAsArray(testGame.getBoard());
        ChessUI.printChessBoard(out, board, true);

        return "You're now watching";
    }

    private String logout(PrintStream out) {
        try {
            ServerFacade.logout(authToken);
        } catch (IOException e) {
            out.print(e.getMessage());
            return "Error Caught";
        }
        authToken = null;
        help(out, true);

        return "See you later!";
    }

    private boolean loggedInCheck() {
        return authToken != null;
    }

    private String stringFromParams(String[] params) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < params.length; i++) {
            result.append(params[i]);
            if (i < params.length - 1) {
                result.append(" ");
            }
        }
        return result.toString();
    }
}
