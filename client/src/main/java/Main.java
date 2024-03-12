import chess.*;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static java.lang.Character.isUpperCase;
import static ui.EscapeSequences.*;

public class Main {

    private static final int BOARD_SIZE = 8;

    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);

        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

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

    private static void printChessBoard(PrintStream out, String[][] board, boolean whiteBottom) {
        printTopHeader(out, whiteBottom);

        boolean firstIsWhite = true;
        for (int i = 0; i < BOARD_SIZE; i++) {
            int boardRow = whiteBottom ? (BOARD_SIZE - i - 1) : i;
            drawChessRow(out, i, board[boardRow], firstIsWhite, whiteBottom);
            firstIsWhite = !firstIsWhite;
        }

        printTopHeader(out, whiteBottom);
    }

    private static void printTopHeader(PrintStream out, boolean whiteBottom) {
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

    private static void drawSideHeader(PrintStream out, int col, boolean whiteBottom) {
        setGreyBG(out);
        int actual = whiteBottom ? (BOARD_SIZE - col) : (col + 1);
        out.print(" " + actual + " ");
    }

    private static void drawChessRow(PrintStream out, int col, String[] boardRow, boolean firstIsWhite, boolean whiteBottom) {
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

    private static void drawChessSquare(PrintStream out, String pieceString, boolean isWhite) {
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

    private static String[][] getChessBoardAsArray(ChessBoard chessBoard) {
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

    private static void setBlackBG(PrintStream out) {
        out.print(SET_BG_COLOR_BLACK);
    }

    private static void setRedText(PrintStream out) {
        out.print(SET_TEXT_COLOR_RED);
    }

    private static void setBlueText(PrintStream out) {
        out.print(SET_TEXT_COLOR_BLUE);
    }

    private static void setWhiteBG(PrintStream out) {
        out.print(SET_BG_COLOR_WHITE);
    }

    private static void setGreyBG(PrintStream out) {
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_BLACK);
    }

    private static void resetBGColor(PrintStream out) {
        out.print(UNSET_BG_COLOR);
        out.print(UNSET_TEXT_COLOR);
    }
}