package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private ChessBoard gameBoard = new ChessBoard();
    private TeamColor currentTurn;

    public ChessGame() {
        gameBoard.resetBoard();
        currentTurn = TeamColor.WHITE;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return currentTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        currentTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece temp = gameBoard.getPiece(startPosition);
        if (temp == null) {
            return null;
        }
        return temp.pieceMoves(gameBoard, startPosition);
        //FIXME check for King in check and if the piece is King or not.
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        if (!validMoves(move.getStartPosition()).contains(move)) {
            throw new InvalidMoveException("Move chosen is Illegal");
        }
        if (move.getPromotionPiece() == null) {
            gameBoard.addPiece(move.getEndPosition(), gameBoard.getPiece(move.getStartPosition()));
        } else {
            gameBoard.addPiece(move.getEndPosition(), new ChessPiece(currentTurn, move.getPromotionPiece()));
        }
        gameBoard.addPiece(move.getStartPosition(), null);
    }

    private TeamColor getOtherTeam(TeamColor teamColor) {
        if (teamColor == TeamColor.WHITE) {
            return TeamColor.BLACK;
        } else {
            return TeamColor.WHITE;
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        Collection<ChessMove> allOpposingMoves = allPossibleTeamMoves(getOtherTeam(teamColor));
        for (ChessMove move : allOpposingMoves) {
            ChessPiece temp = gameBoard.getPiece(move.getEndPosition());
            if (temp != null && temp.getTeamColor() == teamColor && temp.getPieceType() == ChessPiece.PieceType.KING) {
                return true;
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        return isInCheck(teamColor) && allPossibleTeamMoves(teamColor).isEmpty();
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        return !isInCheck(teamColor) && allPossibleTeamMoves(teamColor).isEmpty();
    }

    private Collection<ChessMove> allPossibleTeamMoves(TeamColor team) {
        Collection<ChessMove> allMoves = new ArrayList<>();
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition temp = new ChessPosition(i,j);
                ChessPiece atTemp = gameBoard.getPiece(temp);
                if (atTemp != null && atTemp.getTeamColor() == team) {
                    allMoves.addAll(atTemp.pieceMoves(gameBoard, temp));
                }
            }
        }
        return allMoves;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        gameBoard = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return gameBoard;
    }
}
