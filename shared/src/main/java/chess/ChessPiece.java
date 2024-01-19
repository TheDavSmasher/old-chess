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
            case ROOK:
                return movesFromEndPositions(myPosition, getCross(board, myPosition));
            case KNIGHT:
                return movesFromEndPositions(myPosition, getKnight(board, myPosition));
            case KING:
                return movesFromEndPositions(myPosition, getKing(board, myPosition));
            case PAWN:
                return getPawnMoves(board, myPosition);
            case QUEEN:
                Collection<ChessPosition> fullList = getCross(board, myPosition);
                fullList.addAll(getDiagonals(board, myPosition));
                return  movesFromEndPositions(myPosition, fullList);
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

    private boolean validEndPosition(ChessPiece atEnd) {
        return atEnd == null || (atEnd.color != this.color && atEnd.type != PieceType.KING);
    }

    private Collection<ChessMove> movesFromEndPositions(ChessPosition start, Collection<ChessPosition> ends) {
        Collection<ChessMove> allMoves = new ArrayList<>();
        for (ChessPosition end : ends) {
            allMoves.add(new ChessMove(start, end, null));  //FIXME accurately represent promotionPiece
        }
        return allMoves;
    }

    private Collection<ChessMove> getAllPromotionMoves(ChessMove promotionMove) {
        Collection<ChessMove> promotionMoves = new ArrayList<>();
        promotionMoves.add(new ChessMove(promotionMove.getStartPosition(), promotionMove.getEndPosition(), PieceType.QUEEN));
        promotionMoves.add(new ChessMove(promotionMove.getStartPosition(), promotionMove.getEndPosition(), PieceType.ROOK));
        promotionMoves.add(new ChessMove(promotionMove.getStartPosition(), promotionMove.getEndPosition(), PieceType.BISHOP));
        promotionMoves.add(new ChessMove(promotionMove.getStartPosition(), promotionMove.getEndPosition(), PieceType.KNIGHT));
        return promotionMoves;
    }

    private Collection<ChessMove> getPawnMoves(ChessBoard board, ChessPosition start) {
        Collection<ChessMove> endMoves = new ArrayList<>();
        int boardDirection = (color == ChessGame.TeamColor.BLACK) ? -1 : 1 ; //Down is positive, Up is negative
        ChessPiece atTemp = null;
        //Front
        ChessPosition temp = new ChessPosition(start.getRow() + boardDirection, start.getColumn());
        if (!temp.outOfBounds()) {
            atTemp = board.getPiece(temp);
            if (atTemp == null) {
                if (((color == ChessGame.TeamColor.WHITE && temp.getRow() == 8) || temp.getRow() == 1)) {
                    endMoves.addAll(getAllPromotionMoves(new ChessMove(start, temp, null)));
                } else {
                    endMoves.add(new ChessMove(start, temp, null));
                }

                //Special Case: front has to be clear up to 2 tiles front and be in initial position
                temp = new ChessPosition(start.getRow() + 2 * boardDirection, start.getColumn());
                if (!temp.outOfBounds()) {
                    atTemp = board.getPiece(temp);
                    if (atTemp == null && ((color == ChessGame.TeamColor.WHITE && start.getRow() == 2) || start.getRow() == 7)) {
                        endMoves.add(new ChessMove(start, temp, null));
                    }
                }
            }
        }
        //Eat Left
        temp = new ChessPosition(start.getRow() + boardDirection, start.getColumn() - 1);
        if (!temp.outOfBounds()) {
            atTemp = board.getPiece(temp);
            if (atTemp != null && (atTemp.color != this.color && atTemp.type != PieceType.KING)) {
                if (((color == ChessGame.TeamColor.WHITE && temp.getRow() == 8) || temp.getRow() == 1)) {
                    endMoves.addAll(getAllPromotionMoves(new ChessMove(start, temp, null)));
                } else {
                    endMoves.add(new ChessMove(start, temp, null));
                }
            }
        }
        //Eat Left
        temp = new ChessPosition(start.getRow() + boardDirection, start.getColumn() + 1);
        if (!temp.outOfBounds()) {
            atTemp = board.getPiece(temp);
            if (atTemp != null && (atTemp.color != this.color && atTemp.type != PieceType.KING)) {
                if (((color == ChessGame.TeamColor.WHITE && temp.getRow() == 8) || temp.getRow() == 1)) {
                    endMoves.addAll(getAllPromotionMoves(new ChessMove(start, temp, null)));
                } else {
                    endMoves.add(new ChessMove(start, temp, null));
                }
            }
        }
        return endMoves;
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
        ChessPosition temp = null;
        ChessPiece atTemp = null;
        //Down Right
        int moveCount = 8 - Math.max(start.getRow(), start.getColumn());
        for (int i = 1; i <= moveCount; i++) {     // Starts at 1 so to not include start position inside the list
            temp = new ChessPosition(start.getRow() + i, start.getColumn() + i);
            atTemp = board.getPiece(temp);
            if (validEndPosition(atTemp)) { endPositionList.add(temp); }
            if (atTemp != null) { break; }
        }
        //Down Left
        moveCount = 8 - Math.max(start.getRow(), 9 - start.getColumn());
        for (int i = 1; i <= moveCount; i++) {
            temp = new ChessPosition(start.getRow() + i, start.getColumn() - i);
            atTemp = board.getPiece(temp);
            if (validEndPosition(atTemp)) { endPositionList.add(temp); }
            if (atTemp != null) { break; }
        }
        //Up Right
        moveCount = 8 - Math.max(9 - start.getRow(), start.getColumn());
        for (int i = 1; i <= moveCount; i++) {
            temp = new ChessPosition(start.getRow() - i, start.getColumn() + i);
            atTemp = board.getPiece(temp);
            if (validEndPosition(atTemp)) { endPositionList.add(temp); }
            if (atTemp != null) { break; }
        }
        //Up Left
        moveCount = 8 - Math.max(9 - start.getRow(), 9 - start.getColumn());
        for (int i = 1; i <= moveCount; i++) {
            temp = new ChessPosition(start.getRow() - i, start.getColumn() - i);
            atTemp = board.getPiece(temp);
            if (validEndPosition(atTemp)) { endPositionList.add(temp); }
            if (atTemp != null) { break; }
        }
        return endPositionList;
    }

    private Collection<ChessPosition> getCross(ChessBoard board, ChessPosition start) {
        Collection<ChessPosition> endPositionList = new ArrayList<>();
        ChessPosition temp = null;
        ChessPiece atTemp = null;
        //Right
        int moveCount = 8 - start.getColumn();
        for (int i = 1; i <= moveCount; i++) {
            temp = new ChessPosition(start.getRow(), start.getColumn() + i);
            atTemp = board.getPiece(temp);
            if (validEndPosition(atTemp)) { endPositionList.add(temp); }
            if (atTemp != null) { break; }
        }
        //Left
        moveCount = start.getColumn() - 1;
        for (int i = 1; i <= moveCount; i++) {
            temp = new ChessPosition(start.getRow(), start.getColumn() - i);
            atTemp = board.getPiece(temp);
            if (validEndPosition(atTemp)) { endPositionList.add(temp); }
            if (atTemp != null) { break; }
        }
        //Down
        moveCount = 8 - start.getRow();
        for (int i = 1; i <= moveCount; i++) {
            temp = new ChessPosition(start.getRow() + i, start.getColumn());
            atTemp = board.getPiece(temp);
            if (validEndPosition(atTemp)) { endPositionList.add(temp); }
            if (atTemp != null) { break; }
        }
        //Up
        moveCount = start.getRow() - 1;
        for (int i = 1; i <= moveCount; i++) {
            temp = new ChessPosition(start.getRow() - i, start.getColumn());
            atTemp = board.getPiece(temp);
            if (validEndPosition(atTemp)) { endPositionList.add(temp); }
            if (atTemp != null) { break; }
        }
        return endPositionList;
    }

    private Collection<ChessPosition> getKing(ChessBoard board, ChessPosition start) {
        Collection<ChessPosition> endPositionList = new ArrayList<>();
        ChessPiece atTemp = null;
        //Bottom Right
        ChessPosition temp = new ChessPosition(start.getRow() + 1, start.getColumn() + 1);
        if (!temp.outOfBounds()) {
            atTemp = board.getPiece(temp);
            if (validEndPosition(atTemp)) { endPositionList.add(temp); }
        }
        //Right
        temp = new ChessPosition(start.getRow(), start.getColumn() + 1);
        if (!temp.outOfBounds()) {
            atTemp = board.getPiece(temp);
            if (validEndPosition(atTemp)) { endPositionList.add(temp); }
        }
        //Top Right
        temp = new ChessPosition(start.getRow() - 1, start.getColumn() + 1);
        if (!temp.outOfBounds()) {
            atTemp = board.getPiece(temp);
            if (validEndPosition(atTemp)) { endPositionList.add(temp); }
        }
        //Top
        temp = new ChessPosition(start.getRow() - 1, start.getColumn());
        if (!temp.outOfBounds()) {
            atTemp = board.getPiece(temp);
            if (validEndPosition(atTemp)) { endPositionList.add(temp); }
        }
        //Top Left
        temp = new ChessPosition(start.getRow() - 1, start.getColumn() - 1);
        if (!temp.outOfBounds()) {
            atTemp = board.getPiece(temp);
            if (validEndPosition(atTemp)) { endPositionList.add(temp); }
        }
        //Left
        temp = new ChessPosition(start.getRow(), start.getColumn() - 1);
        if (!temp.outOfBounds()) {
            atTemp = board.getPiece(temp);
            if (validEndPosition(atTemp)) { endPositionList.add(temp); }
        }
        //Bottom Left
        temp = new ChessPosition(start.getRow() + 1, start.getColumn() - 1);
        if (!temp.outOfBounds()) {
            atTemp = board.getPiece(temp);
            if (validEndPosition(atTemp)) { endPositionList.add(temp); }
        }
        //Down
        temp = new ChessPosition(start.getRow() + 1, start.getColumn());
        if (!temp.outOfBounds()) {
            atTemp = board.getPiece(temp);
            if (validEndPosition(atTemp)) { endPositionList.add(temp); }
        }
        return endPositionList;
    }


    private Collection<ChessPosition> getKnight(ChessBoard board, ChessPosition start) {
        Collection<ChessPosition> endPositionList = new ArrayList<>();
        ChessPiece atTemp = null;
        //Bottom Right
        ChessPosition temp = new ChessPosition(start.getRow() + 2, start.getColumn() + 1);
        if (!temp.outOfBounds()) {
            atTemp = board.getPiece(temp);
            if (validEndPosition(atTemp)) { endPositionList.add(temp); }
        }
        //Bottom Middle Right
        temp = new ChessPosition(start.getRow() + 1, start.getColumn() + 2);
        if (!temp.outOfBounds()) {
            atTemp = board.getPiece(temp);
            if (validEndPosition(atTemp)) { endPositionList.add(temp); }
        }
        //Top Middle Right
        temp = new ChessPosition(start.getRow() - 1, start.getColumn() + 2);
        if (!temp.outOfBounds()) {
            atTemp = board.getPiece(temp);
            if (validEndPosition(atTemp)) { endPositionList.add(temp); }
        }
        //Top Right
        temp = new ChessPosition(start.getRow() - 2, start.getColumn() + 1);
        if (!temp.outOfBounds()) {
            atTemp = board.getPiece(temp);
            if (validEndPosition(atTemp)) { endPositionList.add(temp); }
        }
        //Bottom Left
        temp = new ChessPosition(start.getRow() + 2, start.getColumn() - 1);
        if (!temp.outOfBounds()) {
            atTemp = board.getPiece(temp);
            if (validEndPosition(atTemp)) { endPositionList.add(temp); }
        }
        //Bottom Middle Left
        temp = new ChessPosition(start.getRow() + 1, start.getColumn() - 2);
        if (!temp.outOfBounds()) {
            atTemp = board.getPiece(temp);
            if (validEndPosition(atTemp)) { endPositionList.add(temp); }
        }
        //Top Middle Left
        temp = new ChessPosition(start.getRow() - 1, start.getColumn() - 2);
        if (!temp.outOfBounds()) {
            atTemp = board.getPiece(temp);
            if (validEndPosition(atTemp)) { endPositionList.add(temp); }
        }
        //Top Left
        temp = new ChessPosition(start.getRow() - 2, start.getColumn() - 1);
        if (!temp.outOfBounds()) {
            atTemp = board.getPiece(temp);
            if (validEndPosition(atTemp)) { endPositionList.add(temp); }
        }
        return endPositionList;
    }
}