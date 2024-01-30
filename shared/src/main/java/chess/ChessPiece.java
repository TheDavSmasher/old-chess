package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private final ChessGame.TeamColor color;
    private final PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, PieceType type) {
        color = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return color;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        switch (type) {
            case BISHOP:
                return getDiagonals(board, myPosition);
            case ROOK:
                return getCross(board, myPosition);
            case QUEEN:
                Collection<ChessMove> queenMoves = getDiagonals(board, myPosition);
                queenMoves.addAll(getCross(board, myPosition));
                return queenMoves;
            case KING:
                return getKing(board, myPosition);
            case KNIGHT:
                return getKnight(board, myPosition);
            case PAWN:
                return getPawn(board, myPosition);
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return color == that.color && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, type);
    }

    public String toString() {
        String s = "";
        switch (type) {
            case BISHOP -> s = "b";
            case KNIGHT -> s = "n";
            case ROOK -> s = "r";
            case KING -> s = "k";
            case PAWN -> s = "p";
            case QUEEN -> s = "q";
        }
        if (color == ChessGame.TeamColor.WHITE) s = s.toUpperCase();
        return s;
    }

    private Collection<ChessMove> getAllPromotionMoves(ChessPosition start, ChessPosition end) {
        Collection<ChessMove> allMoves = new ArrayList<>();
        allMoves.add(new ChessMove(start, end, PieceType.QUEEN));
        allMoves.add(new ChessMove(start, end, PieceType.ROOK));
        allMoves.add(new ChessMove(start, end, PieceType.BISHOP));
        allMoves.add(new ChessMove(start, end, PieceType.KNIGHT));
        return allMoves;
    }

    private Collection<ChessMove> getPawn(ChessBoard board, ChessPosition start) { //Fail: 10, 11
        Collection<ChessMove> endMoves = new ArrayList<ChessMove>();
        int pieceDirection = (color == ChessGame.TeamColor.BLACK ? -1 : 1);
        int promotionRow = (color == ChessGame.TeamColor.BLACK ? 1 : 8);
        ChessPiece atTemp = null;
        //Move Forward
        ChessPosition temp = new ChessPosition(start.getRow() + pieceDirection, start.getColumn());
        if (!temp.outOfBounds()) {
            atTemp = board.getPiece(temp);
            if (atTemp == null) {
                if (temp.getRow() == promotionRow) {
                    endMoves.addAll(getAllPromotionMoves(start, temp));
                } else {
                    endMoves.add(new ChessMove(start, temp, null));
                }

                //Special Case: Initial Move up to 2 forward
                temp = new ChessPosition(start.getRow() + (2 * pieceDirection), start.getColumn());
                if (start.getRow() == (color == ChessGame.TeamColor.BLACK ? 7 : 2)) {
                    atTemp = board.getPiece(temp);
                    if (atTemp == null) {
                        endMoves.add(new ChessMove(start, temp, null));
                    }
                }
            }
        }
        //Eat Left
        temp = new ChessPosition(start.getRow() + pieceDirection, start.getColumn() - 1);
        if (!temp.outOfBounds()) {
            atTemp = board.getPiece(temp);
            if (atTemp != null && atTemp.color != color) {
                if (temp.getRow() == promotionRow) {
                    endMoves.addAll(getAllPromotionMoves(start, temp));
                } else {
                    endMoves.add(new ChessMove(start, temp, null));
                }
            }
        }
        //Eat Right
        temp = new ChessPosition(start.getRow() + pieceDirection, start.getColumn() + 1);
        if (!temp.outOfBounds()) {
            atTemp = board.getPiece(temp);
            if (atTemp != null && atTemp.color != color) {
                if (temp.getRow() == promotionRow) {
                    endMoves.addAll(getAllPromotionMoves(start, temp));
                } else {
                    endMoves.add(new ChessMove(start, temp, null));
                }
            }
        }
        return endMoves;
    }

    private Collection<ChessMove> getCross(ChessBoard board, ChessPosition start) {
        Collection<ChessMove> endMoves = new ArrayList<ChessMove>();
        ChessPosition temp = null;
        ChessPiece atTemp = null;
        //Right
        int moveLimit = 8 - start.getColumn();
        for (int i = 1; i <= moveLimit; i++) {
            temp = new ChessPosition(start.getRow(), start.getColumn() + i);
            if (!temp.outOfBounds()) {
                atTemp = board.getPiece(temp);
                if (atTemp == null || (atTemp.color != color)) {
                    endMoves.add(new ChessMove(start, temp, null));
                }
                if (atTemp != null) { break; }
            }
        }
        //Left
        moveLimit = start.getColumn() - 1;
        for (int i = 1; i <= moveLimit; i++) {
            temp = new ChessPosition(start.getRow(), start.getColumn() - i);
            if (!temp.outOfBounds()) {
                atTemp = board.getPiece(temp);
                if (atTemp == null || (atTemp.color != color)) {
                    endMoves.add(new ChessMove(start, temp, null));
                }
                if (atTemp != null) { break; }
            }
        }
        //Down
        moveLimit = 8 - start.getRow();
        for (int i = 1; i <= moveLimit; i++) {
            temp = new ChessPosition(start.getRow() + i, start.getColumn());
            if (!temp.outOfBounds()) {
                atTemp = board.getPiece(temp);
                if (atTemp == null || (atTemp.color != color)) {
                    endMoves.add(new ChessMove(start, temp, null));
                }
                if (atTemp != null) { break; }
            }
        }
        //Up
        moveLimit = start.getRow() - 1;
        for (int i = 1; i <= moveLimit; i++) {
            temp = new ChessPosition(start.getRow() - i, start.getColumn());
            if (!temp.outOfBounds()) {
                atTemp = board.getPiece(temp);
                if (atTemp == null || (atTemp.color != color)) {
                    endMoves.add(new ChessMove(start, temp, null));
                }
                if (atTemp != null) { break; }
            }
        }
        return endMoves;
    }

    private Collection<ChessMove> getDiagonals(ChessBoard board, ChessPosition start) {
        Collection<ChessMove> endMoves = new ArrayList<ChessMove>();
        ChessPosition temp = null;
        ChessPiece atTemp = null;
        //Down Right
        int moveLimit = 8 - Math.max(start.getRow(), start.getColumn());
        for (int i = 1; i <= moveLimit; i++) {
            temp = new ChessPosition(start.getRow() + i, start.getColumn() + i);
            if (!temp.outOfBounds()) {
                atTemp = board.getPiece(temp);
                if (atTemp == null || (atTemp.color != color)) {
                    endMoves.add(new ChessMove(start, temp, null));
                }
                if (atTemp != null) { break; }
            }
        }
        //Down Left
        moveLimit = 8 - Math.max(start.getRow(), 9 - start.getColumn());
        for (int i = 1; i <= moveLimit; i++) {
            temp = new ChessPosition(start.getRow() + i, start.getColumn() - i);
            if (!temp.outOfBounds()) {
                atTemp = board.getPiece(temp);
                if (atTemp == null || (atTemp.color != color)) {
                    endMoves.add(new ChessMove(start, temp, null));
                }
                if (atTemp != null) { break; }
            }
        }
        //Up Right
        moveLimit = 8 - Math.max(9 - start.getRow(), start.getColumn());
        for (int i = 1; i <= moveLimit; i++) {
            temp = new ChessPosition(start.getRow() - i, start.getColumn() + i);
            if (!temp.outOfBounds()) {
                atTemp = board.getPiece(temp);
                if (atTemp == null || (atTemp.color != color)) {
                    endMoves.add(new ChessMove(start, temp, null));
                }
                if (atTemp != null) { break; }
            }
        }
        //Up Left
        moveLimit = 8 - Math.max(9 - start.getRow(), 9 - start.getColumn());
        for (int i = 1; i <= moveLimit; i++) {
            temp = new ChessPosition(start.getRow() - i, start.getColumn() - i);
            if (!temp.outOfBounds()) {
                atTemp = board.getPiece(temp);
                if (atTemp == null || (atTemp.color != color)) {
                    endMoves.add(new ChessMove(start, temp, null));
                }
                if (atTemp != null) { break; }
            }
        }
        return endMoves;
    }

    private Collection<ChessMove> getKing(ChessBoard board, ChessPosition start) {
        Collection<ChessMove> endMoves = new ArrayList<ChessMove>();
        ChessPiece atTemp = null;
        //Down Right
        ChessPosition temp = new ChessPosition(start.getRow() + 1, start.getColumn() + 1);
        if (!temp.outOfBounds()) {
            atTemp = board.getPiece(temp);
            if (atTemp == null || (atTemp.color != color)) {
                endMoves.add(new ChessMove(start, temp, null));
            }
        }
        //Right
        temp = new ChessPosition(start.getRow(), start.getColumn() + 1);
        if (!temp.outOfBounds()) {
            atTemp = board.getPiece(temp);
            if (atTemp == null || (atTemp.color != color)) {
                endMoves.add(new ChessMove(start, temp, null));
            }
        }
        //Up Right
        temp = new ChessPosition(start.getRow() - 1, start.getColumn() + 1);
        if (!temp.outOfBounds()) {
            atTemp = board.getPiece(temp);
            if (atTemp == null || (atTemp.color != color)) {
                endMoves.add(new ChessMove(start, temp, null));
            }
        }
        //Up
        temp = new ChessPosition(start.getRow() - 1, start.getColumn());
        if (!temp.outOfBounds()) {
            atTemp = board.getPiece(temp);
            if (atTemp == null || (atTemp.color != color)) {
                endMoves.add(new ChessMove(start, temp, null));
            }
        }
        //Up Left
        temp = new ChessPosition(start.getRow() - 1, start.getColumn() - 1);
        if (!temp.outOfBounds()) {
            atTemp = board.getPiece(temp);
            if (atTemp == null || (atTemp.color != color)) {
                endMoves.add(new ChessMove(start, temp, null));
            }
        }
        //Left
        temp = new ChessPosition(start.getRow(), start.getColumn() - 1);
        if (!temp.outOfBounds()) {
            atTemp = board.getPiece(temp);
            if (atTemp == null || (atTemp.color != color)) {
                endMoves.add(new ChessMove(start, temp, null));
            }
        }
        //Down Left
        temp = new ChessPosition(start.getRow() + 1, start.getColumn() - 1);
        if (!temp.outOfBounds()) {
            atTemp = board.getPiece(temp);
            if (atTemp == null || (atTemp.color != color)) {
                endMoves.add(new ChessMove(start, temp, null));
            }
        }
        //Down
        temp = new ChessPosition(start.getRow() + 1, start.getColumn());
        if (!temp.outOfBounds()) {
            atTemp = board.getPiece(temp);
            if (atTemp == null || (atTemp.color != color)) {
                endMoves.add(new ChessMove(start, temp, null));
            }
        }
        return endMoves;
    }

    private Collection<ChessMove> getKnight(ChessBoard board, ChessPosition start) {
        Collection<ChessMove> endMoves = new ArrayList<ChessMove>();
        ChessPiece atTemp = null;
        //Down Bottom Right
        ChessPosition temp = new ChessPosition(start.getRow() + 2, start.getColumn() + 1);
        if (!temp.outOfBounds()) {
            atTemp = board.getPiece(temp);
            if (atTemp == null || (atTemp.color != color)) {
                endMoves.add(new ChessMove(start, temp, null));
            }
        }
        //Down Middle Right
        temp = new ChessPosition(start.getRow() + 1, start.getColumn() + 2);
        if (!temp.outOfBounds()) {
            atTemp = board.getPiece(temp);
            if (atTemp == null || (atTemp.color != color)) {
                endMoves.add(new ChessMove(start, temp, null));
            }
        }
        //Up Middle Right
        temp = new ChessPosition(start.getRow() - 1, start.getColumn() + 2);
        if (!temp.outOfBounds()) {
            atTemp = board.getPiece(temp);
            if (atTemp == null || (atTemp.color != color)) {
                endMoves.add(new ChessMove(start, temp, null));
            }
        }
        //Up Top Right
        temp = new ChessPosition(start.getRow() - 2, start.getColumn() + 1);
        if (!temp.outOfBounds()) {
            atTemp = board.getPiece(temp);
            if (atTemp == null || (atTemp.color != color)) {
                endMoves.add(new ChessMove(start, temp, null));
            }
        }
        //Up Top Left
        temp = new ChessPosition(start.getRow() - 2, start.getColumn() - 1);
        if (!temp.outOfBounds()) {
            atTemp = board.getPiece(temp);
            if (atTemp == null || (atTemp.color != color)) {
                endMoves.add(new ChessMove(start, temp, null));
            }
        }
        //Up Middle Left
        temp = new ChessPosition(start.getRow() - 1, start.getColumn() - 2);
        if (!temp.outOfBounds()) {
            atTemp = board.getPiece(temp);
            if (atTemp == null || (atTemp.color != color)) {
                endMoves.add(new ChessMove(start, temp, null));
            }
        }
        //Down Middle Left
        temp = new ChessPosition(start.getRow() + 1, start.getColumn() - 2);
        if (!temp.outOfBounds()) {
            atTemp = board.getPiece(temp);
            if (atTemp == null || (atTemp.color != color)) {
                endMoves.add(new ChessMove(start, temp, null));
            }
        }
        //Down Bottom Left
        temp = new ChessPosition(start.getRow() + 2, start.getColumn() - 1);
        if (!temp.outOfBounds()) {
            atTemp = board.getPiece(temp);
            if (atTemp == null || (atTemp.color != color)) {
                endMoves.add(new ChessMove(start, temp, null));
            }
        }
        return endMoves;
    }
}
