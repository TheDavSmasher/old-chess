package ui;

import chess.ChessBoard;
import chess.ChessPiece;
import chess.ChessPosition;

import java.io.PrintStream;

import static java.lang.Character.isUpperCase;
import static ui.EscapeSequences.*;
import static ui.EscapeSequences.UNSET_TEXT_COLOR;

public class ChessUI {

    private final int BOARD_SIZE = 8;

    public String[][] getChessBoardAsArray(ChessBoard chessBoard) {
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

    public void printChessBoard(PrintStream out, String[][] board, boolean whiteBottom) {
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
