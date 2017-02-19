package game;

public class Move {

    private int[][] board;
    private BoardSquare boardPlace;

    public Move(int[][] b, BoardSquare bp) {
        board = b;
        boardPlace = bp;
    }

    public int[][] getBoard() {
        return board;
    }

    public BoardSquare getBardPlace() {
        return boardPlace;
    }

}
