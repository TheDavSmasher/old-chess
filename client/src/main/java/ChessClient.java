import chess.*;

import java.io.PrintStream;
import java.util.Arrays;

import static java.lang.Character.isUpperCase;
import static ui.EscapeSequences.*;
import static ui.EscapeSequences.UNSET_TEXT_COLOR;

public class ChessClient {

    private final int BOARD_SIZE = 8;
    private String authToken;

    public ChessClient() {
        authToken = null;
    }

    public void run(PrintStream out) {
        out.print(ERASE_SCREEN);

        ChessGame testGame = new ChessGame();
        try {
            testGame.makeMove(new ChessMove(new ChessPosition(2,1), new ChessPosition(3,1), null));
        } catch (InvalidMoveException ignored) {}

        String[][] board = getChessBoardAsArray(testGame.getBoard());

        printChessBoard(out, board, false);
        out.println();
        printChessBoard(out, board, true);
    }

    public String evaluate(String input) {
        String[] tokens = input.toLowerCase().split(" ");
        String command = (tokens.length > 0) ? tokens[0] : "help";
        String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
        return switch (command) {
            case "register" -> register(params);
            case "signin" -> signIn(params);
            case "logout" -> logout();
            case "create" -> createGame(params);
            case "games" -> listGames();
            case "join" -> joinGame(params);
            case "observe" -> observeGame(params);
            case "quit" -> "quit";
            default -> help();
        };
    }

    public String help() {
        return "Help wanted";
    }

    private String observeGame(String[] params) {
        return null;
    }

    private String joinGame(String[] params) {
        return null;
    }

    private String listGames() {
        return null;
    }

    private String createGame(String[] params) {
        return null;
    }

    private String logout() {
        return null;
    }

    private String register(String[] params) {
        return null;
    }

    private String signIn(String[] params) {
        //TODO talk to server
        return null;
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
