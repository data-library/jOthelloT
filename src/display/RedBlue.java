package display;

import static display.Display.TIME;
import javax.swing.JButton;
import static display.Display.blue;
import static display.Display.red_blue;

/**
 *
 * @author Marcelo Paglione
 */
public class RedBlue implements Runnable {

    private JButton button;

    public RedBlue(JButton button) {
        this.button = button;
    }

    @Override
    public void run() {
        button.setIcon(red_blue);
        try {
            Thread.sleep(TIME);
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
        button.setIcon(blue);
    }
}
