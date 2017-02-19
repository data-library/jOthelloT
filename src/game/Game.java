package game;

import display.Display;
import players.HumanPlayer;
import players.RandomPlayer;

import java.util.List;

import javax.swing.JFrame;

/**
 *
 * @author Iuri
 */
public class Game {

    static int X = 1, O = -1, validate;
    public static Display displayOtello;
    public static String winner;

    public static void print(String string) {
        if (!Display.ENABLE && Tournament.GAME == 2) {
            System.out.println(string);
        }
    }

    public static void main(String[] args) throws Exception {
        BoardSquare boardPlace = null;
        AbstractGame game = null;
        AbstractPlayer player = null;
        AbstractPlayer player2 = null;
        List<Move> moveList;

        if (Display.ENABLE && Tournament.GAME == 2) {
            displayOtello = new Display();
        }
        //System.out.println("args.length " + args.length);
        switch (args.length) {
            case 0:
                game = new OthelloGame();
                //Define player 1 class
                player = new players.RandomPlayer(2);
                player.setBoardMark(X);
                player.setOpponentBoardMark(O);
                player.setGame(game);
                //Define player 2 class
                if (displayOtello != null) {
                    player2 = new players.HumanPlayerDisplay(2);
                } else {
                    player2 = new players.HumanPlayer(2);
                }
                player2.setBoardMark(O);
                player2.setOpponentBoardMark(X);
                player2.setGame(game);
                break;
            case 1:
                game = new OthelloGame();
                //Define player 1 class
                player = (AbstractPlayer) Class.forName(args[0]).getConstructor(int.class).newInstance(2);
                player.setBoardMark(X);
                player.setOpponentBoardMark(O);
                player.setGame(game);
                //Define player 2 class
                if (displayOtello != null && Tournament.GAME == 2) {
                    player2 = new players.HumanPlayerDisplay(2);
                } else {
                    player2 = new players.HumanPlayer(2);
                }
                player2.setBoardMark(O);
                player2.setOpponentBoardMark(X);
                player2.setGame(game);
                break;
            case 2:
                game = new OthelloGame();
                //Define player 1 class
                player = (AbstractPlayer) Class.forName(args[0]).getConstructor(int.class).newInstance(2);
                player.setBoardMark(X);
                player.setOpponentBoardMark(O);
                player.setGame(game);
                //Define player 2 class
                player2 = (AbstractPlayer) Class.forName(args[1]).getConstructor(int.class).newInstance(2);
                player2.setBoardMark(O);
                player2.setOpponentBoardMark(X);
                player2.setGame(game);
                break;
            default:
                break;
        }
        if (Display.ENABLE && Tournament.GAME == 2) {
            displayOtello.setPlayersName(player, player2);
        }

        OthelloBoard othelloBoard = new OthelloBoard(game);
        if (Display.ENABLE && Tournament.GAME == 2) {
            displayOtello.updateDisplay(othelloBoard.getBoard());
        }
        othelloBoard.print();

        while (true) {
            int result = 0;
//           player 1 plays
            if (Display.ENABLE && Tournament.GAME == 2) {
                displayOtello.setPlayerTurn(1);
            }
            if (Display.ENABLE && Tournament.GAME == 2) {
                displayOtello.validPlays(game.getValidMoves(othelloBoard.getBoard(), 1), player);
            }
            moveList = game.getValidMoves(othelloBoard.getBoard(), player.getMyBoardMark());
            if (moveList.size() > 0) {
                try {
                    boardPlace = player.play(othelloBoard.getBoard());
                    validate = game.validate_moviment(othelloBoard.getBoard(), boardPlace, player);
                } catch (Exception e) {
                    validate = -2;
                    winner = player2.getClass().toString();
                }
            } else {
                boardPlace = new BoardSquare(-1, -1);
                validate = game.validate_moviment(othelloBoard.getBoard(), boardPlace, player);
            }
            if (validate == 0) {               
                othelloBoard.setBoard(game.do_move(othelloBoard.getBoard(), boardPlace, player));            
                if (Display.ENABLE && Tournament.GAME == 2) {
                    displayOtello.updateDisplay(othelloBoard.getBoard(), boardPlace, player);
                }
            } else if (validate == -1 || validate == -2) {
                System.err.println(player.getClass().toString() + " WO: invalid play");
                print(player.getClass().toString() + " player1 WO: invalid play");
                break;
            }

            print("Player 1");
            othelloBoard.print();

//            check if player 1 won
            result = game.testing_end_game(othelloBoard.getBoard(), player.getBoardMark());
            if (result != 0) {
                if (result == 1) {
                    if (Display.ENABLE && Tournament.GAME == 2) {
                        displayOtello.setWinner(1);
                    }
                    winner = player.getClass().toString();
                    print("Player 1 Won");
                } else {
                    if (Display.ENABLE && Tournament.GAME == 2) {
                        displayOtello.setWinner(0);
                    }
                    winner = "draw";
                    print("Draw");
                }
                break;
            }

//           player 2
            if (Display.ENABLE && Tournament.GAME == 2) {
                displayOtello.setPlayerTurn(-1);
            }
            if (Display.ENABLE && Tournament.GAME == 2) {
                displayOtello.validPlays(game.getValidMoves(othelloBoard.getBoard(), -1), player2);
            }
            moveList = game.getValidMoves(othelloBoard.getBoard(), player2.getMyBoardMark());
            if (moveList.size() > 0) {
                try {
                    boardPlace = player2.play(othelloBoard.getBoard());
                    validate = game.validate_moviment(othelloBoard.getBoard(), boardPlace, player2);
                } catch (Exception e) {
                    validate = -2;
                    winner = player.getClass().toString();
                }
            } else {
                boardPlace = new BoardSquare(-1, -1);
                validate = game.validate_moviment(othelloBoard.getBoard(), boardPlace, player2);
            }
            if (validate == 0) {
                othelloBoard.setBoard(game.do_move(othelloBoard.getBoard(), boardPlace, player2));
                if (Display.ENABLE && Tournament.GAME == 2) {
                    displayOtello.updateDisplay(othelloBoard.getBoard(), boardPlace, player2);
                }
            } else if (validate == -1 || validate == -2) {
                print("player2 WO: invalid play");
                System.err.println(player2.getClass().toString() + " WO: invalid play");
                break;
            }
            print("Player2");
            othelloBoard.print();

//            check if player 2 won
            result = game.testing_end_game(othelloBoard.getBoard(), player2.getBoardMark());
            if (result != 0) {
                if (result == 1) {
                    if (Display.ENABLE && Tournament.GAME == 2) {
                        displayOtello.setWinner(-1);
                    }
                    winner = player2.getClass().toString();
                    print("Player 2 won");
                } else {
                    if (Display.ENABLE && Tournament.GAME == 2) {
                        displayOtello.setWinner(0);
                    }
                    winner = "draw";
                    print("Draw");
                }
                break;
            }

        }
        displayOtello.validateRepaint();

    }

    public static JFrame getGameRecordFrame() {
        return displayOtello.getGameRecordFrame();
    }

    public static void showGameRecord() {
        displayOtello.showGameRecord();
    }

    public static void closeDisplay() {
        displayOtello.closeDisplay();
    }
}
