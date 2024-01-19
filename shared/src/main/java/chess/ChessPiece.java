package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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
        this.color = pieceColor;
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
     * It does, however, account for enemy and friendly pieces.
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        switch (type) {
            case BISHOP:
                return movesFromEndPositions(myPosition, getDiagonals(board, myPosition));
        }
        return null;
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

    private Collection<ChessMove> movesFromEndPositions(ChessPosition start, Collection<ChessPosition> ends) {
        Collection<ChessMove> allMoves = new ArrayList<>();
        for (ChessPosition end : ends) {
            allMoves.add(new ChessMove(start, end, null));  //FIXME accurately represent promotionPiece
        }
        return allMoves;
    }

    /**
     * This function has 4 for loops, one per diagonal direction, each one adding any possible end position to the List, collecting all diagonal moves possible.
     * The for loop starts i = 1 so the starting position is not included in the end position list.
     * It loops until 8 - boardLimit, so it loops until the board edge; boardLimit is more along the lines of how many tiles
     * are available before then.
     * For Down Left and Up Right, Math.max() will have one of the two coordinates be subtracted from 9, because it is being treated as an inverse, for simpler
     * calculations inside the for loop.
     *
     * @param board - Current board state
     * @param start - Piece starting position
     * @return list of all possible end positions
     */
    private Collection<ChessPosition> getDiagonals(ChessBoard board, ChessPosition start) {
        Collection<ChessPosition> endPositionList = new ArrayList<>();
        //Down Right
        int boardLimit = Math.max(start.getRow(), start.getColumn());
        for (int i = 1; i <= 8 - boardLimit; i++) {     // Starts at 1 so to not include start position inside the list
            ChessPosition temp = new ChessPosition(start.getRow() + i, start.getColumn() + i);
            ChessPiece atTemp = board.getPiece(temp);
            if (atTemp == null || (atTemp.color != this.color && atTemp.type != PieceType.KING)) {
                endPositionList.add(temp);
            }
            if (atTemp != null) {
                break;
            }
        }
        //Down Left
        boardLimit = Math.max(start.getRow(), 9 - start.getColumn());
        for (int i = 1; i <= 8 - boardLimit; i++) {
            ChessPosition temp = new ChessPosition(start.getRow() + i, start.getColumn() - i);
            ChessPiece atTemp = board.getPiece(temp);
            if (atTemp == null || (atTemp.color != this.color && atTemp.type != PieceType.KING)) {
                endPositionList.add(temp);
            }
            if (atTemp != null) {
                break;
            }
        }
        //Up Right
        boardLimit = Math.max(9 - start.getRow(), start.getColumn());
        for (int i = 1; i <= 8 - boardLimit; i++) {
            ChessPosition temp = new ChessPosition(start.getRow() - i, start.getColumn() + i);
            ChessPiece atTemp = board.getPiece(temp);
            if (atTemp == null || (atTemp.color != this.color && atTemp.type != PieceType.KING)) {
                endPositionList.add(temp);
            }
            if (atTemp != null) {
                break;
            }
        }
        //Up Left
        boardLimit = Math.max(9 - start.getRow(), 9 - start.getColumn());
        for (int i = 1; i <= 8 - boardLimit; i++) {
            ChessPosition temp = new ChessPosition(start.getRow() - i, start.getColumn() - i);
            ChessPiece atTemp = board.getPiece(temp);
            if (atTemp == null || (atTemp.color != this.color && atTemp.type != PieceType.KING)) {
                endPositionList.add(temp);
            }
            if (atTemp != null) {
                break;
            }
        }
        return endPositionList;
    }
}