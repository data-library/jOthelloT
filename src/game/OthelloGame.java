package game;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author iuri
 */
public class OthelloGame extends AbstractGame {

    public OthelloGame() {
        size = 8;
    }

    @Override
    public int testing_end_game(int[][] board, int markBoard) {
        int win = 0, loss = 0, end = 0;

        if (noSpace(board)) {
            for (int row = 0; row < size; row++) {
                for (int col = 0; col < size; col++) {
                    if (board[row][col] == markBoard) {
                        win++;
                    } else {
                        loss++;
                    }
                }
            }
            if (win > loss) {
                end = 1;
            } else if (win == loss) {
                end = 2;
            }
        }

        int markBoardOpponent = 0;
        int markBoardMine=  0;
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if (board[row][col] != markBoard && board[row][col] != 0) {
                    markBoardOpponent++;
                }
                if (board[row][col] == markBoard) {
                    markBoardMine++;
                }
            }
        }
        if (markBoardOpponent == 0) {
            end = 1;
        }

        if (getValidMoves(board, 1).isEmpty()
                && getValidMoves(board, -1).isEmpty()) {
            if (markBoardMine > markBoardOpponent) {
                end = 1;
            } else if (markBoardMine == markBoardOpponent) {
                end = 2;
            }
        }
        return end;
    }

    @Override
    public List<Move> getValidMoves(int[][] board, int markBoard) {
        List<BoardSquare> boardPlaces = new ArrayList<>();
        List<Move> boards = new ArrayList<>();
        OthelloPlayer othelloPlayer = new OthelloPlayer(-1);
        othelloPlayer.setBoardMark(markBoard);
        int sizeI = board.length;
        for (int i = 0; i < sizeI; i++) {
            for (int j = 0; j < sizeI; j++) {
                BoardSquare moviment = new BoardSquare(i, j);
                if (validate_moviment(board, moviment, othelloPlayer) == 0) {
                    boardPlaces.add(moviment);
                }
            }
        }

        for (BoardSquare boardPlace : boardPlaces) {
            int[][] b = start_board();
            for (int i = 0; i < sizeI; i++) {
                for (int j = 0; j < sizeI; j++) {
                    b[i][j] = board[i][j];
                }
            }
            boards.add(new Move(do_move(b, boardPlace, othelloPlayer), boardPlace));
        }
        return boards;
    }

    @Override
    public int validate_moviment(int[][] board, BoardSquare boardPlace, AbstractPlayer abstractPlayer) {

//        skip moviment
        if (boardPlace.getRow() == -1 && boardPlace.getCol() == -1) {
            return 0;
        }

        if (board[boardPlace.getRow()][boardPlace.getCol()] == 0) {
//            check col +
            if (boardPlace.getCol() + 1 < size && board[boardPlace.getRow()][boardPlace.getCol() + 1] != 0 && board[boardPlace.getRow()][boardPlace.getCol() + 1] != abstractPlayer.getBoardMark()) {//check right
                for (int i = boardPlace.getCol() + 1; i < size; i++) {
                    if (board[boardPlace.getRow()][i] == abstractPlayer.getBoardMark()) {
                        return 0;
                    }
                    if (board[boardPlace.getRow()][i] == 0) {
                        break;
                    }
                }
            }
//                check col -
            if (boardPlace.getCol() > 0 && board[boardPlace.getRow()][boardPlace.getCol() - 1] != 0 && board[boardPlace.getRow()][boardPlace.getCol() - 1] != abstractPlayer.getBoardMark()) {//check left
                for (int i = boardPlace.getCol() - 1; i >= 0; i--) {
                    if (board[boardPlace.getRow()][i] == abstractPlayer.getBoardMark()) {
                        return 0;
                    }
                    if (board[boardPlace.getRow()][i] == 0) {
                        break;
                    }
                }
            }
//                check row -
            if (boardPlace.getRow() > 0 && board[boardPlace.getRow() - 1][boardPlace.getCol()] != 0 && board[boardPlace.getRow() - 1][boardPlace.getCol()] != abstractPlayer.getBoardMark()) {//check up
                for (int i = boardPlace.getRow() - 1; i >= 0; i--) {
                    if (board[i][boardPlace.getCol()] == abstractPlayer.getBoardMark()) {
                        return 0;
                    }
                    if (board[i][boardPlace.getCol()] == 0) {
                        break;
                    }
                }
            }
//                check row +
            if (boardPlace.getRow() < size - 1 && board[boardPlace.getRow() + 1][boardPlace.getCol()] != 0 && board[boardPlace.getRow() + 1][boardPlace.getCol()] != abstractPlayer.getBoardMark()) {//check down
                for (int i = boardPlace.getRow() + 1; i < size; i++) {
                    if (board[i][boardPlace.getCol()] == abstractPlayer.getBoardMark()) {
                        return 0;
                    }
                    if (board[i][boardPlace.getCol()] == 0) {
                        break;
                    }
                }
            }
//                check main diagonal +
            if (boardPlace.getCol() < size - 1 && boardPlace.getRow() < size - 1 && board[boardPlace.getRow() + 1][boardPlace.getCol() + 1] != 0 && board[boardPlace.getRow() + 1][boardPlace.getCol() + 1] != abstractPlayer.getBoardMark()) {//check down right
                for (int i = boardPlace.getRow() + 1, j = boardPlace.getCol() + 1; i < size && j < size; i++, j++) {
                    if (board[i][j] == abstractPlayer.getBoardMark()) {
                        return 0;
                    }
                    if (board[i][j] == 0) {
                        break;
                    }
                }
            }
//                check main diagonal -
            if (boardPlace.getCol() > 0 && boardPlace.getRow() > 0 && board[boardPlace.getRow() - 1][boardPlace.getCol() - 1] != 0 && board[boardPlace.getRow() - 1][boardPlace.getCol() - 1] != abstractPlayer.getBoardMark()) {//check up left
                for (int i = boardPlace.getRow() - 1, j = boardPlace.getCol() - 1; i >= 0 && j >= 0; i--, j--) {
                    if (board[i][j] == abstractPlayer.getBoardMark()) {
                        return 0;
                    }
                    if (board[i][j] == 0) {
                        break;
                    }
                }
            }
//                check second diagonal +
            if (boardPlace.getCol() > 0 && boardPlace.getRow() < size - 1 && board[boardPlace.getRow() + 1][boardPlace.getCol() - 1] != 0 && board[boardPlace.getRow() + 1][boardPlace.getCol() - 1] != abstractPlayer.getBoardMark()) {//check down left
                for (int i = boardPlace.getRow() + 1, j = boardPlace.getCol() - 1; i < size && j >= 0; i++, j--) {
                    if (board[i][j] == abstractPlayer.getBoardMark()) {
                        return 0;
                    }
                    if (board[i][j] == 0) {
                        break;
                    }
                }
            }
//                check second diagonal -
            if (boardPlace.getCol() < size - 1 && boardPlace.getRow() > 0 && board[boardPlace.getRow() - 1][boardPlace.getCol() + 1] != 0 && board[boardPlace.getRow() - 1][boardPlace.getCol() + 1] != abstractPlayer.getBoardMark()) {//second up right
                for (int i = boardPlace.getRow() - 1, j = boardPlace.getCol() + 1; i >= 0 && j < size; i--, j++) {
                    if (board[i][j] == abstractPlayer.getBoardMark()) {
                        return 0;
                    }
                    if (board[i][j] == 0) {
                        break;
                    }
                }
            }
        } else {
            return -1;
        }
        return -2;
    }

    public boolean checkAroundPositionAndPlayedPosition(int row, int col, BoardSquare boardPlace) {
        if (row == boardPlace.getRow() && col == boardPlace.getCol()) {
            return true;
        }
        if (row == boardPlace.getRow() + 1 && col == boardPlace.getCol() + 1) {
            return true;
        }
        if (row == boardPlace.getRow() - 1 && col == boardPlace.getCol() - 1) {
            return true;
        }
        if (row == boardPlace.getRow() - 1 && col == boardPlace.getCol() + 1) {
            return true;
        }
        if (row == boardPlace.getRow() + 1 && col == boardPlace.getCol() - 1) {
            return true;
        }
        if (row == boardPlace.getRow() + 1 && col == boardPlace.getCol()) {
            return true;
        }
        if (row == boardPlace.getRow() - 1 && col == boardPlace.getCol()) {
            return true;
        }
        if (row == boardPlace.getRow() && col == boardPlace.getCol() + 1) {
            return true;
        }
        if (row == boardPlace.getRow() && col == boardPlace.getCol() - 1) {
            return true;
        }
        return false;
    }

    @Override
    public int[][] do_move(int[][] board, BoardSquare boardPlace, AbstractPlayer abstractPlayer) {
//        skip play
        if (boardPlace.getRow() == -1 && boardPlace.getCol() == -1) {
            return board;
        }

        board[boardPlace.getRow()][boardPlace.getCol()] = abstractPlayer.getBoardMark();
        // search own mark in backwards line
        int position = -1;
        for (int i = boardPlace.getRow() - 1; i >= 0; i--) {
            // rdr - whether find an empty spot before the player mark
            if (board[i][boardPlace.getCol()] == 0) {
                break;
            }
            if (board[i][boardPlace.getCol()] == abstractPlayer.getBoardMark()) {
                position = i;
                break;
            }
        }
        if (position != -1) {
            position++;
            for (int i = position; i < boardPlace.getRow(); i++) {
                if (board[i][boardPlace.getCol()] != 0) {
                    board[i][boardPlace.getCol()] = abstractPlayer.getBoardMark();
                }
            }
        }
        // search own mark in forward lines
        position = -1;
        for (int i = boardPlace.getRow() + 1; i < 8; i++) {
            // rdr - whether find an empty spot before the player marks
            if (board[i][boardPlace.getCol()] == 0) {
                break;
            }
            if (board[i][boardPlace.getCol()] == abstractPlayer.getBoardMark()) {
                position = i;
                break;
            }
        }
        if (position != -1) {
            position--;
            for (int i = position; i > boardPlace.getRow(); i--) {
                if (board[i][boardPlace.getCol()] != 0) {
                    board[i][boardPlace.getCol()] = abstractPlayer.getBoardMark();
                }
            }
        }
        /**
         * **************************************************************************************
         */
        //search own mark in col in backwards line
        position = -1;
        for (int i = boardPlace.getCol() - 1; i >= 0; i--) {
            //rdr - whether find an empty spot before the player marks
            if (board[boardPlace.getRow()][i] == 0) {
                break;
            }
            if (board[boardPlace.getRow()][i] == abstractPlayer.getBoardMark()) {
                position = i;
                break;
            }
        }
        if (position != -1) {
            position++;
            for (int i = position; i < boardPlace.getCol(); i++) {
                if (board[boardPlace.getRow()][i] != 0) {
                    board[boardPlace.getRow()][i] = abstractPlayer.getBoardMark();
                }
            }
        }
        // search own mark in rows in forward lines
        position = -1;
        for (int i = boardPlace.getCol() + 1; i < 8; i++) {
            // rdr - whether find an empty spot before the player marks
            if (board[boardPlace.getRow()][i] == 0) {
                break;
            }
            if (board[boardPlace.getRow()][i] == abstractPlayer.getBoardMark()) {
                position = i;
                break;
            }
        }
        if (position != -1) {
            position--;
            for (int i = position; i > boardPlace.getCol(); i--) {
                if (board[boardPlace.getRow()][i] != 0) {
                    board[boardPlace.getRow()][i] = abstractPlayer.getBoardMark();
                }
            }
        }
        /**
         * ******************************************************
         */
        // search own mark in backwards diagonals
        int row = boardPlace.getRow() - 1;
        int col = boardPlace.getCol() - 1;
        // rdr - indicates whether an empty spot was found before player mark
        boolean empty = false;

        while (row >= 0 && col >= 0) {
            if (board[row][col] == abstractPlayer.getBoardMark()) {
                break;
            }
            // rdr - exits loop if an empty spot was found
            if (board[row][col] == 0) {
                empty = true;
                break;
            }
            row--;
            col--;
        }
        if (row >= 0 && col >= 0 && !empty) { // rdr - check if it is empty
            while (row != boardPlace.getRow()
                    && col != boardPlace.getCol()) {
                if (board[row][col] != 0) {
                    board[row][col] = abstractPlayer.getBoardMark();
                }
                row++;
                col++;
            }
        }
        //search own mark in forward diagonal
        row = boardPlace.getRow() + 1;
        col = boardPlace.getCol() + 1;

        empty = false;
        while (row < 8 && col < 8) {
            if (board[row][col] == abstractPlayer.getBoardMark()) {
                break;
            }
            if (board[row][col] == 0) {
                empty = true;
                break;
            }
            row++;
            col++;
        }
        if (row < 8 && col < 8 && !empty) { 
            while (row != boardPlace.getRow()
                    && col != boardPlace.getCol()) {
                if (board[row][col] != 0) {
                    board[row][col] = abstractPlayer.getBoardMark();
                }
                row--;
                col--;
            }
        }
        /**
         * ******************************************************
         */
        //search own mark in backwards inverse diagonal
        row = boardPlace.getRow() - 1;
        col = boardPlace.getCol() + 1;
        empty = false;
        while (row >= 0 && col < 8) {
            if (board[row][col] == abstractPlayer.getBoardMark()) {
                break;
            }
            if (board[row][col] == 0) {
                empty = true;
                break;
            }
            row--;
            col++;
        }
        if (row >= 0 && col < 8 && !empty) {
            while (row != boardPlace.getRow()
                    && col != boardPlace.getCol()) {
                if (board[row][col] != 0) {
                    board[row][col] = abstractPlayer.getBoardMark();
                }
                row++;
                col--;
            }
        }
        //search own mark in forwards inverse diagonal
        row = boardPlace.getRow() + 1;
        col = boardPlace.getCol() - 1;
        empty = false;
        while (row < 8 && col >= 0) {
            if (board[row][col] == abstractPlayer.getBoardMark()) {
                break;
            }
            if (board[row][col] == 0) {
                empty = true;
                break;
            }
            row++;
            col--;
        }
        if (row < 8 && col >= 0 && !empty) { 
            while (row != boardPlace.getRow()
                    && col != boardPlace.getCol()) {
                if (board[row][col] != 0) {
                    board[row][col] = abstractPlayer.getBoardMark();
                }
                row--;
                col++;
            }
        }
        /**
         * ******************************************************
         */
        return board;
    }

    @Override
    public int[][] start_board() {
        int[][] board = new int[size][size];
        board[3][3] = -1;
        board[4][4] = -1;
        board[3][4] = 1;
        board[4][3] = 1;
        return board;
    }

    public boolean noSpace(int[][] tab) {
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if (tab[row][col] == 0) {
                    return false;
                }
            }
        }
        return true;
    }
}
