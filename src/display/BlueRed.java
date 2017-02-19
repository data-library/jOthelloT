package display;

import static display.Display.TIME;
import javax.swing.JButton;
import static display.Display.red;
import static display.Display.blue_red;

/**
 *
 * @author Marcelo Paglione
 */
public class BlueRed implements Runnable {

    private JButton button;

    public BlueRed(JButton button) {
        this.button = button;
    }

    @Override
    public void run() {
        button.setIcon(blue_red);
        try {
            Thread.sleep(TIME);
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
        button.setIcon(red);
    }
}
