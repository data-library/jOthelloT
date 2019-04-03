/**
 *
 * @author Marcelo Paglione
 */
package display;

import static display.Display.TIME;
import javax.swing.Icon;
import javax.swing.JButton;

public class ButtonTransition implements Runnable {

    private Icon transitionIcon;
    private Icon finalIcon;
    private JButton button;

    public ButtonTransition(JButton button, Icon transitionIcon, Icon finalIcon) {
        this.transitionIcon = transitionIcon;
        this.finalIcon = finalIcon;
        this.button = button;
    }

    @Override
    public void run() {
        button.setIcon(transitionIcon);
        try {
            Thread.sleep(TIME);
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
        button.setIcon(finalIcon);
    }
}

