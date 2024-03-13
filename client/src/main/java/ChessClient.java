import chess.*;

import java.io.PrintStream;
import java.util.Arrays;

import static java.lang.Character.isUpperCase;
import static ui.EscapeSequences.*;

public class ChessClient {

    private final int BOARD_SIZE = 8;
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

        //TODO talk to server
        authToken = "tempToken";
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

        //TODO talk to server
        authToken = "tempToken";
        help(out);

        return "Welcome back";
    }

    private String listGames(PrintStream out) {
        return "Here's the games";
    }

    private String createGame(PrintStream out, String[] params) {
        if (params.length < 1) {
            out.print("Please provide a game ID.\\nFormat: 2 gameName");
            return "Retry";
        }
        //TODO talk to server
        return "Created new game";
    }

    private String joinGame(PrintStream out, String[] params) {
        if (params.length < 2) {
            out.print("Please provide a game ID and color.\nFormat: 3 gameID 1/2");
            return "Retry";
        }
        //TODO talk to server
        ChessGame testGame = new ChessGame();
        try {
            testGame.makeMove(new ChessMove(new ChessPosition(2,1), new ChessPosition(3,1), null));
        } catch (InvalidMoveException ignored) {}
        String[][] board = getChessBoardAsArray(testGame.getBoard());

        printChessBoard(out, board, true);
        return "You joined";
    }

    private String observeGame(PrintStream out, String[] params) {
        if (params.length < 1) {
            out.print("Please provide a game ID.\nFormat: 4 gameID");
            return "Retry";
        }
        //TODO talk to server
        ChessGame testGame = new ChessGame();
        String[][] board = getChessBoardAsArray(testGame.getBoard());
        printChessBoard(out, board, false);

        return "You're now watching";
    }

    private String logout(PrintStream out) {
        // TODO talk to server
        authToken = null;
        help(out);

        return "See you later!";
    }

    private boolean loggedInCheck() {
        return authToken != null;
    }

    private void printChessBoard(PrintStream out, String[][] board, boolean whiteBottom) {
        printTopHeader(out, whiteBottom);

        boolean firstIsWhite = true;
        for (int i = 0; i < BOARD_SIZE; i++) {
            int boardRow = whiteBottom ? (BOARD_SIZE - i - 1) : i;
            drawChessRow(out, i, board[boardRow], firstIsWhite, whiteBottom);
            firstIsWhite = !firstIsWhite;
        }

        printTopHeader(out, whiteBottom);
    }

    private void printTopHeader(PrintStream out, boolean whiteBottom) {
        String[] columns = { " a ", " b ", " c ", " d ", " e ", " f ", " g ", " h "};
        setGreyBG(out);
        out.print("   ");
        for (int i = 0; i < BOARD_SIZE; i++) {
            int index = whiteBottom ? i : BOARD_SIZE - i - 1;
            out.print(columns[index]);
        }
        out.print("   ");
        resetBGColor(out);
        out.println();
    }

    private void drawSideHeader(PrintStream out, int col, boolean whiteBottom) {
        setGreyBG(out);
        int actual = whiteBottom ? (BOARD_SIZE - col) : (col + 1);
        out.print(" " + actual + " ");
    }

    private void drawChessRow(PrintStream out, int col, String[] boardRow, boolean firstIsWhite, boolean whiteBottom) {
        drawSideHeader(out, col, whiteBottom);
        boolean isWhite = firstIsWhite;
        for (int i = 0; i < BOARD_SIZE; i++) {
            int boardCol = whiteBottom ? i : (BOARD_SIZE - i - 1);
            String pieceString = boardRow[boardCol];
            drawChessSquare(out, pieceString, isWhite);
            isWhite = !isWhite;
        }
        drawSideHeader(out, col, whiteBottom);
        resetBGColor(out);
        out.println();
    }

    private void drawChessSquare(PrintStream out, String pieceString, boolean isWhite) {
        if (isWhite) {
            setWhiteBG(out);
        } else {
            setBlackBG(out);
        }
        if (pieceString != null) {
            boolean pieceIsWhite = isUpperCase(pieceString.charAt(0));
            if (pieceIsWhite) {
                setRedText(out);
            } else {
                setBlueText(out);
            }
            out.print(" "+pieceString.toUpperCase()+" ");
        } else {
            out.print("   ");
        }

    }

    private String[][] getChessBoardAsArray(ChessBoard chessBoard) {
        String[][] stringBoard = new String[8][8];

        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                ChessPiece tempPiece = chessBoard.getPiece(new ChessPosition(i+1, j+1));
                if (tempPiece != null) {
                    stringBoard[i][j] = tempPiece.toString();
                }
            }
        }

        return stringBoard;
    }

    private void setBlackBG(PrintStream out) {
        out.print(SET_BG_COLOR_BLACK);
    }

    private void setRedText(PrintStream out) {
        out.print(SET_TEXT_COLOR_RED);
    }

    private void setBlueText(PrintStream out) {
        out.print(SET_TEXT_COLOR_BLUE);
    }

    private void setWhiteBG(PrintStream out) {
        out.print(SET_BG_COLOR_WHITE);
    }

    private void setGreyBG(PrintStream out) {
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_BLACK);
    }

    private void resetBGColor(PrintStream out) {
        out.print(UNSET_BG_COLOR);
        out.print(UNSET_TEXT_COLOR);
    }
}
