package game;

/**
 *
 * @author iuri
 */
public class OthelloPlayer extends AbstractPlayer {

    public OthelloPlayer(int depth) {
        super(depth);
        // TODO Auto-generated constructor stub
    }

    @Override
    public BoardSquare play(int[][] tab) {
        BoardSquare boardPlace = new BoardSquare();
        Boolean validBoardPlace = false;
        loop:
        for (int i = 0; i < getGame().size; i++) {
            for (int j = 0; j < getGame().size; j++) {

                if (getGame().validate_moviment(tab, new BoardSquare(i, j), this) == 0) {
                    if (i < getGame().size - 1 && tab[i + 1][j] != 0 && tab[i + 1][j] != getBoardMark()) {
                        validBoardPlace = true;
                    } else if (i < getGame().size - 1 && j < getGame().size - 1 && tab[i + 1][j + 1] != 0 && tab[i + 1][j + 1] != getBoardMark()) {
                        validBoardPlace = true;
                    } else if (j < getGame().size - 1 && tab[i][j + 1] != 0 && tab[i][j + 1] != getBoardMark()) {
                        validBoardPlace = true;
                    } else if (i != 0 && tab[i - 1][j] != 0 && tab[i - 1][j] != getBoardMark()) {
                        validBoardPlace = true;
                    } else if (i != 0 && j != 0 && tab[i - 1][j - 1] != 0 && tab[i - 1][j - 1] != getBoardMark()) {
                        validBoardPlace = true;
                    } else if (j != 0 && tab[i][j - 1] != 0 && tab[i][j - 1] != getBoardMark()) {
                        validBoardPlace = true;
                    }
                }

                if (tab[i][j] == 0 && validBoardPlace) {
                    boardPlace.getRow(i);
                    boardPlace.setCol(j);
                    break loop;
                }
            }
        }
        return boardPlace;
    }

    public Boolean checkRowCol(int[][] board, int row, int cow) {
        Boolean valid = false;
        for (int i = 0; i < getGame().size; i++) {
            if (board[row][i] == getBoardMark()
                    || board[i][cow] == getBoardMark()) {
                valid = true;
                break;
            }
            if (row - i >= 0 && cow - i >= 0) {
                if (board[row - i][cow - i] == getBoardMark()) {
                    valid = true;
                    break;
                }
            }
            if (row + i < getGame().size && cow + i < getGame().size) {
                if (board[row + i][cow + i] == getBoardMark()) {
                    valid = true;
                    break;
                }
            }
            if (row + i < getGame().size && cow - i >= 0) {
                if (board[row + i][cow - i] == getBoardMark()) {
                    valid = true;
                    break;
                }
            }
            if (row - i >= 0 && cow + i < getGame().size) {
                if (board[row - i][cow + i] == getBoardMark()) {
                    valid = true;
                    break;
                }
            }
        }

        return valid;
    }
}
