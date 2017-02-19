package players;

/**
 *
 * @author Marcelo Paglione
 */
import java.util.List;
import java.util.Random;

import game.AbstractPlayer;
import game.BoardSquare;
import game.Move;
import game.OthelloGame;

public class RandomPlayer extends AbstractPlayer {

    public RandomPlayer(int depth) {
        super(depth);
    }

    @Override
    public BoardSquare play(int[][] tab) {
        OthelloGame jogo = new OthelloGame();
        Random r = new Random();
        List<Move> jogadas = jogo.getValidMoves(tab, getMyBoardMark());
        if (jogadas.size() > 0) {
            return jogadas.get(r.nextInt(jogadas.size())).getBardPlace();
        } else {
            return new BoardSquare(-1, -1);
        }
    }

}
