import chess.*;
import client.Repl;

public class ClientMain {

    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);

        Repl repl = new Repl();
        repl.run();
    }
}