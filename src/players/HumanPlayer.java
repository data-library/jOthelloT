package players;

import java.util.List;
import java.util.Random;
import java.util.Scanner;

import game.AbstractPlayer;
import game.BoardSquare;
import game.Move;
import game.OthelloGame;

/**
 *
 * @author Lucas M. Oliveira
 */
public class HumanPlayer extends AbstractPlayer {

    public HumanPlayer(int depth) {
        super(depth);
    }

    @Override
    public BoardSquare play(int[][] tab) {
        BoardSquare casa = new BoardSquare();

        OthelloGame jogo = new OthelloGame();
        Random r = new Random();
        List<Move> jogadas = jogo.getValidMoves(tab, getMyBoardMark());
//        for(Jogada j: jogadas){
//			System.out.print(j.getCasa().getLinha());
//			System.out.println(j.getCasa().getColuna());
//			System.out.println();
//		}

        Scanner leitor = new Scanner(System.in);

        int i, j;

        System.out.println("Insira a linha");
        i = leitor.nextInt();
        System.out.println("Insira a coluna");
        j = leitor.nextInt();
        if (tab[i][j] == 0) {
            casa.getRow(i);
            casa.setCol(j);
        }
        return casa;
    }

}
