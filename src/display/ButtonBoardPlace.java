package display;

import javax.swing.JButton;
import game.BoardSquare;

/**
 *
 * @author Marcelo Paglione
 */
public class ButtonBoardPlace {

    private JButton button;
    private BoardSquare boardPlace;
    private boolean mouseListener;

    public ButtonBoardPlace() {
        boardPlace = new BoardSquare();
        button = new JButton("");
        mouseListener = true;
    }

    public void setMouseListener(boolean mouseListener) {
        this.mouseListener = mouseListener;
    }

    public boolean getMouseListener() {
        return mouseListener;
    }

    public BoardSquare getBoardPlace() {
        return boardPlace;
    }

    public JButton getButton() {
        return button;
    }

    public void setButton(JButton button) {
        this.button = button;
    }

    public void setBoardPlace(BoardSquare boardPlace) {
        this.boardPlace = boardPlace;
    }
}
