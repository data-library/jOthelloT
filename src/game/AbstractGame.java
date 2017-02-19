package game;

import java.util.List;

/**
 *
 * @author iuri
 */
abstract public class AbstractGame {

    public int size = 0;

    //0 = not over, 1 = win, 2 = draw
    abstract public int testing_end_game(int[][] tab, int marcaTabuleiro);

    // 0 = ok, -1 = filled move, -2 = invalid moviment
    abstract public int validate_moviment(int[][] boardOriginal, BoardSquare moviment, AbstractPlayer abstractPlayer);

    abstract public int[][] do_move(int[][] boardOriginal, BoardSquare moviment, AbstractPlayer abstractPlayer);

    abstract public int[][] start_board();

    //abstract public List<Move> getAvailableMoviments(int[][] boardOriginal, int boardMark);
    abstract public List<Move> getValidMoves(int[][] boardOriginal, int boardMark);

}
