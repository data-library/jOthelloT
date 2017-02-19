package game;

/**
 *
 * @author iuri
 */
public class BoardSquare {

    private int row;
    private int col;

    public BoardSquare() {
    }

    public BoardSquare(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public void getRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    @Override
    public String toString() {
        return "{" + "row=" + row + ", col=" + col + "}";
    }
    
}
