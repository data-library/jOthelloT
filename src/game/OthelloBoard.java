package game;

import display.Display;

/**
 *
 * @author Iuri
 */
public class OthelloBoard {

    private char[] conversion = {'o', ' ', 'x'};
    private int[][] board;
    private AbstractGame game;
    private String divider;

    public OthelloBoard(AbstractGame game) {
        this.game = game;
        board = game.start_board();
        divider = generateDivisor();
    }

    public void print() {
        if (!Display.ENABLE || Tournament.GAME == 1) {
            for (int i = 0; i < getGame().size; i++) {
                for (int j = 0; j < getGame().size; j++) {
                    System.out.printf(" %c %c", getConversion()[getBoard()[i][j] + 1], (j == (getGame().size - 1)) ? ' ' : '|');
                }
                if (i != getGame().size - 1) {
                    System.out.println(getDivider());
                }
            }
            System.out.println("\r\n");
        }
    }

    public String generateDivisor() {
        String d = new String("\r\n");
        for (int i = 0; i < (getGame().size - 1); i++) {
            d += "---+";
        }
        d += "---";
        return d;
    }

    public char[] getConversion() {
        return conversion;
    }

    public void setConversion(char[] conversion) {
        this.conversion = conversion;
    }

    public int[][] getBoard() {
        return board;
    }

    public void setBoard(int[][] board) {
        this.board = board;
    }

    public AbstractGame getGame() {
        return game;
    }

    public void setGame(AbstractGame game) {
        this.game = game;
    }

    public String getDivider() {
        return divider;
    }

    public void setDivider(String divider) {
        this.divider = divider;
    }
}
