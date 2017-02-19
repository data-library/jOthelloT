package display;

import players.HumanPlayerDisplay;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.io.File;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JPanel;
import game.AbstractPlayer;
import game.BoardSquare;
import game.Move;

/**
 *
 * @author Marcelo Paglione
 */
public class Display extends JFrame {

    private ButtonBoardPlace[][] buttons;
    private ButtonBoardPlace[][] buttonsGameRecord;
    private GridLayout gridLayout;
    private static String path;
    private static final int BUTTON_SIZE = 50;
    private static int row, col;
    private static boolean click = false;
    private JLabel playerTurn, player1Name, player2Name, winner;
    public static JLabel player1Score, player2Score;
    public static AbstractPlayer player1class, player2class,currentPlayer;
    private int[][] gameRecord;
    private int gameRecordCounter = 0;
    public static boolean showGameRecord = false;

    private JFrame frame;
    private JPanel panel;

    public static boolean ENABLE = true;
    public static int TIME = 500;
    public static Icon blue, red, blue_red, red_blue, blue_winner, red_winner, blue_player, red_player;

    /**
     * Reports to display who will be the players of this game
     *
     * @param player1
     * @param player2
     */
    public void setPlayersName(AbstractPlayer player1, AbstractPlayer player2) {
        this.player1class = player1;
        this.player2class = player2;
        player1Name.setText(player1.getClass().getSimpleName());
        player2Name.setText(player2.getClass().getSimpleName());
    }

    /**
     * Initialize all variables, the side panel and the main panel
     */
    public Display() {
        initComponents();
        setTitle("jOthelloT");
        getContentPane().setBackground(Color.BLACK);
        String separator = File.separator;
        path = System.getProperty("user.dir") + separator + "src" + separator + "Display" + separator + "images" + separator;
        blue_red = new ImageIcon(path + "blueRed.gif");
        red_blue = new ImageIcon(path + "redBlue.gif");
        red = new ImageIcon(path + "red.png");
        red_player = new ImageIcon(path + "redPlayer.gif");
        red_winner = new ImageIcon(path + "redWinner.gif");
        blue = new ImageIcon(path + "blue.png");
        blue_player = new ImageIcon(path + "bluePlayer.gif");
        blue_winner = new ImageIcon(path + "blueWinner.gif");
        click = false;
        row = -1;
        col = -1;

        setSideMenu();
        buttons = new ButtonBoardPlace[8][8];
        setBoard(board, buttons);
        setBlackBorders();

        gameRecord = new int[buttons.length][buttons[0].length];
        gameRecord[buttons.length / 2][buttons.length / 2] = -1;
        gameRecord[buttons.length / 2 - 1][buttons.length / 2] = -1;
        gameRecord[buttons.length / 2][buttons.length / 2 - 1] = -1;
        gameRecord[buttons.length / 2 - 1][buttons.length / 2 - 1] = -1;
        panel = new JPanel();
        buttonsGameRecord = new ButtonBoardPlace[8][8];
        setBoard(panel, buttonsGameRecord);
        setRecordGameTab();
        frame = new JFrame("Game Record");
        frame.add(panel);
        //frame.setVisible(true);
        frame.pack();
        setResizable(false);

    }

    private synchronized void set() {
        while (click) {
            try {
                wait();
            } catch (InterruptedException exception) {
            }
        }
        click = true;
        notify();
    }

    public synchronized BoardSquare get() {
        while (!click) {
            try {
                wait();
            } catch (InterruptedException exception) {

            }
        }
        click = false;
        notify();
        return new BoardSquare(row, col);
    }

    private void setBoard(JPanel board, ButtonBoardPlace[][] buttons) {

        gridLayout = new GridLayout(buttons.length + 1, buttons[0].length + 1, 0, 0);
        board.setLayout(gridLayout);
        board.setBackground(new Color(41, 110, 55));//green

        JButton button = new JButton();
        button.setBackground(new Color(2, 23, 7));//darked green
        button.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
        button.setEnabled(false);
        board.add(button);
        for (int i = 0; i < buttons.length; i++) {
            if (i == 0) {
                for (int j = 0; j < buttons[i].length; j++) {
                    button = new JButton((char) (j + 97) + "");
                    button.setEnabled(false);
                    button.setForeground(Color.yellow);
                    button.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
                    button.setBackground(new Color(4, 51, 15));
                    board.add(button);
                }
            }
            button = new JButton((i + 1) + "");
            button.setEnabled(false);
            button.setForeground(new Color(255, 255, 255));
            button.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
            button.setBackground(new Color(4, 51, 15));
            board.add(button);
            for (int j = 0; j < buttons[i].length; j++) {
                buttons[i][j] = new ButtonBoardPlace();
                buttons[i][j].setBoardPlace(new BoardSquare(i, j));
                buttons[i][j].getButton().setContentAreaFilled(false);
                buttons[i][j].getButton().setPreferredSize(new Dimension(BUTTON_SIZE, BUTTON_SIZE));
                addMouseEvent(buttons[i][j]);
                board.add(buttons[i][j].getButton());
            }
        }
    }

    private void setSideMenu() {
        gridLayout = new GridLayout(8, 1, 0, 0);
        sideMenu.setLayout(gridLayout);
        sideMenu.setBackground(Color.BLACK);

        playerTurn = new JLabel("Turn");
        playerTurn.setFont(new Font("Verdana", 0, 20));
        playerTurn.setForeground(new Color(255, 255, 255));

        player1Name = new JLabel();
        player1Name.setFont(new Font("Verdana", 0, 20));
        player1Name.setForeground(new Color(255, 255, 255));

        player1Score = new JLabel();
        player1Score.setIcon(blue);
        player1Score.setFont(new Font("Verdana", 0, 35));
        player1Score.setForeground(new Color(255, 255, 255));

        player2Name = new JLabel();
        player2Name.setFont(new Font("Verdana", 0, 20));
        player2Name.setForeground(new Color(255, 255, 255));

        player2Score = new JLabel();
        player2Score.setIcon(red);
        player2Score.setFont(new Font("Verdana", 0, 35));
        player2Score.setForeground(new Color(255, 255, 255));

        winner = new JLabel("Winner");
        winner.setFont(new Font("Verdana", 0, 20));
        winner.setForeground(new Color(255, 255, 255));
        winner.setVisible(false);

        sideMenu.add(playerTurn);
        sideMenu.add(player1Name);
        sideMenu.add(player1Score);
        sideMenu.add(new JLabel());
        sideMenu.add(player2Name);
        sideMenu.add(player2Score);
        sideMenu.add(new JLabel());
        sideMenu.add(winner);
    }

    private void setScore(int player1, int player2) {
        this.player1Score.setText(String.valueOf(player1));
        this.player2Score.setText(String.valueOf(player2));
    }

    /**
     * Display State Who won the game on the side menu
     *
     * @param winnerPlayer
     */
    public void setWinner(int winnerPlayer) {
        switch (winnerPlayer) {
            case 1:
                winner.setIcon(blue_winner);
                break;
            case -1:
                winner.setIcon(red_winner);
                break;
            default:
                winner.setText("Draw");
                break;
        }
        winner.setVisible(true);
    }

    /**
     * Toggles the icon representing the player's turn on the side menu
     *
     * @param player
     */
    public void setPlayerTurn(int player) {
        try {
            Thread.sleep(TIME);
        } catch (InterruptedException ex) {
            System.err.println(ex.getMessage());
        }
        if (player == 1) {
            playerTurn.setIcon(blue_player);
        } else if (player == -1) {
            playerTurn.setIcon(red_player);
        }
    }

    private void addMouseEvent(ButtonBoardPlace button) {
        button.getButton().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                row = button.getBoardPlace().getRow();
                col = button.getBoardPlace().getCol();
                if (button.getMouseListener()) {
                    set();
                }
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                if (button.getMouseListener()) {
                    button.getButton().setBorder(BorderFactory.createLineBorder(new Color(255, 255, 0)));
                }
            }
            @Override
            public void mouseExited(MouseEvent e) {
                if (button.getMouseListener()) {
                    button.getButton().setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255)));
                }
            }
        });
    }

    /**
     * Updates the display with the valid moves.
     *
     * 1 - Change all the inside edges of the board to black and the
     * Cursor to the type DEFAULT_CURSOR
     *
     * 2 - Change the borders that are played valid for the white and the mouse cursor to the type HAND_CURSOR
     *
     * @see setBordasPretas()
     * @param validPlays
     */
    public void validPlays(List<Move> validPlays, AbstractPlayer player) {
        setBlackBorders();
        for (Move move : validPlays) {
            int i = move.getBardPlace().getRow();
            int j = move.getBardPlace().getCol();
            buttons[i][j].getButton().setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255)));
            if (player instanceof HumanPlayerDisplay ) {
                buttons[i][j].getButton().setCursor(new Cursor(Cursor.HAND_CURSOR));
                buttons[i][j].setMouseListener(true);
            }
        }
        try {
            Thread.sleep(TIME);
        } catch (InterruptedException ex) {
            System.err.println(ex.getMessage());
        }
    }

    private void setBlackBorders() {
        for (ButtonBoardPlace[] jButtons : buttons) {
            for (ButtonBoardPlace button : jButtons) {
                button.getButton().setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
                button.getButton().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                button.setMouseListener(false);
            }
        }
    }

    /**
     * 1 Updates all display positions with blue or red icons.
     * Performs the transition between the current color of the play for the second color option
     *
     * 2 - Check out the score of each player and updates the display
     *
     * 3 - Gives the physical size of objects internal and external expands to accommodate all objects in the scene
     * 
     *
     * 4 - Centralizes the display on the user screen
     *
     * 5 - Shows Display
     *
     * @see setPlacar(int player1, int player2)
     * @param board
     */
    public void updateDisplay(int[][] board) {
        int p1 = 0, p2 = 0;
        for (int i = 0; i < buttons.length; i++) {
            for (int j = 0; j < buttons[i].length; j++) {
                if (board[i][j] == 1) {
                    if (buttons[i][j].getButton().getIcon() == null) {
                        buttons[i][j].getButton().setIcon(blue);
                    } else if (String.valueOf(buttons[i][j].getButton().getIcon()).equals(String.valueOf(red))) {
                        RedBlue transition = new RedBlue(buttons[i][j].getButton());
                        new Thread(transition).start();
                    }
                    p1++;
                } else if (board[i][j] == -1) {
                    if (buttons[i][j].getButton().getIcon() == null) {
                        buttons[i][j].getButton().setIcon(red);
                    } else if (String.valueOf(buttons[i][j].getButton().getIcon()).equals(String.valueOf(blue))) {
                        BlueRed transition = new BlueRed(buttons[i][j].getButton());
                        new Thread(transition).start();
                    }
                    p2++;
                }
            }
        }
        setScore(p1, p2);
        pack();
        if (showGameRecord) {
            if (!frame.isVisible()) {
                showGameRecord();
            }
        }
        if (!isVisible()) {
            setLocationRelativeTo(null);
            setVisible(true);
        }
    }

    public int[][] getGameRecor() {
        return gameRecord;
    }

    public JLabel getPlayer1Score() {
        return player1Score;
    }

    public JLabel getPlayer2Score() {
        return player2Score;
    }

    public AbstractPlayer getPlayer1class() {
        return player1class;
    }

    public AbstractPlayer getPlayer2class() {
        return player2class;
    }

    /**
     * Show the Game Record interface of the current move
     *
     * Interface is updated every iteration of the players
     */
    public void showGameRecord() {
        if (!frame.isVisible()) {
            frame.setVisible(true);
            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                    frame.setVisible(false);
                }
            });
        }
    }

    /**
     * Updates the main display and the frame Game Record
     *
     * @param board
     * @param boardPlace
     * @param player
     */
    public void updateDisplay(int[][] board, BoardSquare boardPlace, AbstractPlayer player) {
        currentPlayer = player;
        updateDisplay(board);
        for (int i = 0; i < buttons.length; i++) {
            for (int j = 0; j < buttons[i].length; j++) {
                buttons[i][j].setMouseListener(false);
            }
        }
        if(boardPlace.getRow()!=-1 && boardPlace.getCol()!=-1){
        gameRecordCounter++;
            try {
                if (player.getBoardMark() == player1class.getBoardMark()) {
                    gameRecord[boardPlace.getRow()][boardPlace.getCol()] = gameRecordCounter;
                    buttonsGameRecord[boardPlace.getRow()][boardPlace.getCol()].getButton().setIcon(blue);

                } else {
                    gameRecord[boardPlace.getRow()][boardPlace.getCol()] = gameRecordCounter;
                    buttonsGameRecord[boardPlace.getRow()][boardPlace.getCol()].getButton().setIcon(red);
                }
                buttonsGameRecord[boardPlace.getRow()][boardPlace.getCol()].getButton().setText(gameRecordCounter + "");
                buttonsGameRecord[boardPlace.getRow()][boardPlace.getCol()].getButton().setContentAreaFilled(false);
                buttonsGameRecord[boardPlace.getRow()][boardPlace.getCol()].getButton().setFont(new java.awt.Font("Lucida Grande", 1, 18)); // NOI18N
                buttonsGameRecord[boardPlace.getRow()][boardPlace.getCol()].getButton().setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
                buttonsGameRecord[boardPlace.getRow()][boardPlace.getCol()].getButton().setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
                buttonsGameRecord[boardPlace.getRow()][boardPlace.getCol()].getButton().setPreferredSize(new Dimension((int) (BUTTON_SIZE * 0.5), (int) (BUTTON_SIZE * 0.5)));
            } catch (Exception e) {

            }
        }
    }
    

    private void setRecordGameTab() {
        buttonsGameRecord[buttons.length / 2][buttons.length / 2].getButton().setIcon(red);
        buttonsGameRecord[buttons.length / 2 - 1][buttons.length / 2].getButton().setIcon(blue);
        buttonsGameRecord[buttons.length / 2][buttons.length / 2 - 1].getButton().setIcon(blue);
        buttonsGameRecord[buttons.length / 2 - 1][buttons.length / 2 - 1].getButton().setIcon(red);

        buttonsGameRecord[buttons.length / 2][buttons.length / 2].getButton().setText("");
        buttonsGameRecord[buttons.length / 2 - 1][buttons.length / 2].getButton().setText("");
        buttonsGameRecord[buttons.length / 2][buttons.length / 2 - 1].getButton().setText("");
        buttonsGameRecord[buttons.length / 2 - 1][buttons.length / 2 - 1].getButton().setText("");

        buttonsGameRecord[buttons.length / 2][buttons.length / 2].getButton().setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
        buttonsGameRecord[buttons.length / 2 - 1][buttons.length / 2].getButton().setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
        buttonsGameRecord[buttons.length / 2][buttons.length / 2 - 1].getButton().setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
        buttonsGameRecord[buttons.length / 2 - 1][buttons.length / 2 - 1].getButton().setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
        for (int i = 0; i < gameRecord.length; i++) {
            for (int j = 0; j < gameRecord[i].length; j++) {
                buttonsGameRecord[i][j].getButton().setContentAreaFilled(false);
                buttonsGameRecord[i][j].getButton().setFont(new java.awt.Font("Lucida Grande", 1, 18));
                buttonsGameRecord[i][j].getButton().setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
                buttonsGameRecord[i][j].getButton().setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
                buttonsGameRecord[i][j].getButton().setPreferredSize(new Dimension((int) (BUTTON_SIZE * 0.8), (int) (BUTTON_SIZE * 0.8)));
                buttonsGameRecord[i][j].setMouseListener(false);
            }
        }
    }

    public void closeDisplay() {
        frame.dispose();
        gameRecord = null;
        this.dispose();
    }

    public JFrame getGameRecordFrame() {
        return frame;
    }
    
    public void validateRepaint(){        
        player1Score.validate();
        player1Score.repaint();
        player2Score.validate();
        player2Score.repaint();
        player1Name.validate();
        player1Name.repaint();
        player2Name.validate();
        player2Name.repaint();
        playerTurn.validate();
        playerTurn.repaint();
        sideMenu.validate();
        sideMenu.repaint();
        frame.validate();
        frame.repaint();
        
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        sideMenu = new javax.swing.JPanel();
        board = new javax.swing.JPanel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout sideMenuLayout = new javax.swing.GroupLayout(sideMenu);
        sideMenu.setLayout(sideMenuLayout);
        sideMenuLayout.setHorizontalGroup(
            sideMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 137, Short.MAX_VALUE)
        );
        sideMenuLayout.setVerticalGroup(
            sideMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout boardLayout = new javax.swing.GroupLayout(board);
        board.setLayout(boardLayout);
        boardLayout.setHorizontalGroup(
            boardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 315, Short.MAX_VALUE)
        );
        boardLayout.setVerticalGroup(
            boardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 295, Short.MAX_VALUE)
        );

        jMenuBar1.setBackground(new java.awt.Color(0, 0, 0));
        jMenuBar1.setOpaque(false);

        jMenu1.setText("Game Record");

        jMenuItem1.setText("Show");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(sideMenu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(board, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(board, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(sideMenu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        showGameRecord();
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel board;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPanel sideMenu;
    // End of variables declaration//GEN-END:variables

}
