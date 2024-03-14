import chess.*;
import server.ChessServer;

public class Main {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Server: " + piece);
        ChessServer server = new ChessServer();
        server.run(8080);
    }
}