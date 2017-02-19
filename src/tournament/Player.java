package tournament;

import javax.swing.JCheckBox;

/**
 *
 * @author Marcelo Paglione
 */
class Player implements Comparable<Player> {

    private String player;
    private int score;
    private JCheckBox jCheckBox;
    public static final int WINNER = 1;
    public static final int LOOSER = -1;
    public static final int DRAW = 0;
    public static int type = 1;
    private String playerNameDisplay;

    public Player(String player) {
        this.player = player;
        jCheckBox = new JCheckBox();
        jCheckBox.setSelected(false);
    }

    public Player(String player, int score, JCheckBox jCheckBox) {
        this.player = player;
        this.score = score;
        this.jCheckBox = jCheckBox;
    }

    public Player(String player, int score) {
        this.player = player;
        this.score = score;
    }

    public String getPlayerNameDisplay() {
        return playerNameDisplay;
    }

    public void setPlayerNameDisplay(String playerNameDisplay) {
        this.playerNameDisplay = playerNameDisplay;
    }

    public JCheckBox getjCheckBox() {
        return jCheckBox;
    }

    public void setjCheckBox(JCheckBox jCheckBox) {
        this.jCheckBox = jCheckBox;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {

        this.score += score;

    }

    @Override
    public String toString() {
        return "\n" + player + "\nscore=" + score;
    }

    @Override
    public int compareTo(Player o) {
        if (o.getScore() < score) {
            return -1;
        } else if (o.getScore() > score) {
            return 1;
        }
        return 0;
    }

}
