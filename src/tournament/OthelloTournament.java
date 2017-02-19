package tournament;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import tournament.gui.PvpDialog;
import tournament.gui.PvpScore;
import tournament.gui.Qualifying;
import tournament.gui.Results;
import tournament.gui.Winner;
import game.AbstractPlayer;
import game.Game;

/**
 *
 * @author Marcelo Paglione
 */
public class OthelloTournament extends JFrame implements Runnable {

    private List<Qualifying> listQualifying = new ArrayList<>();
    private List<PvpScore> btResult = new ArrayList<>();
    private Results board = new Results(this);
    private String NEXTGAMEAUTO = "Next Game (automatic)",NEXTGAMEMANUAL = "Next Game (manual)";
    private String gameAutomatic = NEXTGAMEAUTO, gameType = "All versus All";    
    private int depth = 1;
    private JComboBox<String> score;
    private final String SCORESTYLE1 = "Score Tournament";
    private final String SCORESTYLE2 = "Score Points";
    private String tournamentType = SCORESTYLE1;
    private JButton buttonStart;
    private JPanel mainPanel;
    private List<Player> abstractPlayers = new ArrayList<>();
    private String[] excludedAbstracPlayers
            = {"players.HumanPlayer", "players.HumanPlayerDisplay", "players.PlayerAleatorio",
                "playerOthelloSilla.Player", "playerVelhaSilla.Player",
                "jogo.PlayerOthello", "jogo.PlayerVelha"};
    private final String PATH = System.getProperty("user.dir") + File.separator + "Results";
    private Thread mainThread;
    
    private Player first, second, third, fourth;

    public List<PvpScore> getBtResult() {
        return btResult;
    }

    public Results getPartialResults() {
        return board;
    }

    public List<Qualifying> getlistQualifying() {
        return listQualifying;
    }

    public OthelloTournament() {
        display.Display.TIME = 10;
        File dir = new File(PATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));

        JScrollPane jScrollPane = new JScrollPane(mainPanel);
        jScrollPane.getVerticalScrollBar().setUnitIncrement(16);

        try {
            depth = Integer.parseInt(JOptionPane.showInputDialog("Depth: "));
        } catch (HeadlessException | NumberFormatException e) {
            depth = 1;
        }
        findAbstractPlayers();

        buttonStart = new JButton("Start");
        buttonStart.addActionListener((java.awt.event.ActionEvent evt) -> {
            for (Iterator<Player> iterator = abstractPlayers.iterator(); iterator.hasNext();) {
                Player player = iterator.next();
                if (!player.getjCheckBox().isSelected()) {
                    iterator.remove();
                }
            }
            close();
            new Thread(this).start();
        });

        score = new JComboBox<>();
        score.setModel(new DefaultComboBoxModel<>(new String[]{SCORESTYLE1, SCORESTYLE2}));
        score.addActionListener((ActionEvent e) -> {
            tournamentType = (String) score.getSelectedItem();
        });

        JButton buttonCheckAll = new JButton("Check all");
        buttonCheckAll.addActionListener((ActionEvent e) -> {
            for (int i = 0; i < abstractPlayers.size(); i++) {
                abstractPlayers.get(i).getjCheckBox().setSelected(true);
            }
            repaint();
        });

        JButton buttonUncheckAll = new JButton("Uncheck all");
        buttonUncheckAll.addActionListener((ActionEvent e) -> {
            for (int i = 0; i < abstractPlayers.size(); i++) {
                abstractPlayers.get(i).getjCheckBox().setSelected(false);
            }
            repaint();
        });

        JPanel panelTime = new JPanel();
        JTextField jtextFieldTime = new JTextField();
        jtextFieldTime.setPreferredSize(new Dimension(50, 30));
        jtextFieldTime.setText(String.valueOf(display.Display.TIME));
        jtextFieldTime.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
                try {
                    display.Display.TIME = Integer.parseInt(jtextFieldTime.getText());
                } catch (Exception ex) {
                    display.Display.TIME = 10;
                }
            }
        });

        panelTime.add(new JLabel("Time per move(ms)"), BorderLayout.WEST);
        panelTime.add(jtextFieldTime, BorderLayout.EAST);

        JComboBox<String> gameAuto = new JComboBox<>();
        gameAuto.setModel(new DefaultComboBoxModel<>(new String[]{NEXTGAMEAUTO, NEXTGAMEMANUAL}));
        gameAuto.addActionListener((ActionEvent e) -> {
            gameAutomatic = (String) gameAuto.getSelectedItem();
        });

        JComboBox<String> jComboBoxGameType = new JComboBox<>();
        jComboBoxGameType.setModel(new DefaultComboBoxModel<>(new String[]{"All versus All","KnockOut"}));
        jComboBoxGameType.addActionListener((ActionEvent e) -> {
            this.gameType = (String) jComboBoxGameType.getSelectedItem();
        });

        JPanel bottom = new JPanel();
        bottom.setLayout(new GridLayout(0, 2, 10, 10));

        bottom.add(buttonCheckAll);
        bottom.add(buttonUncheckAll);
        bottom.add(panelTime);
        bottom.add(gameAuto);
        bottom.add(score);
        bottom.add(buttonStart);
        bottom.add(jComboBoxGameType);

        setTitle("jOthelloT");
        add(jScrollPane, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(450, 600));
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void findAbstractPlayers() {
        try {
            Files.walk(Paths.get(System.getProperty("user.dir") + File.separator + "src" + File.separator)).forEach((Path filePath) -> {
                if (Files.isRegularFile(filePath)) {
                    int lastIndex = filePath.toString().lastIndexOf('.');
                    String str = filePath.toString().substring(lastIndex);
                    if (str.equals(".java")) {
                        try {
                            int s = 0;
                            //System.out.println(filePath.toString());
                            int m = filePath.toString().lastIndexOf("src");
                            String fileName = filePath.toString().substring(m + 4, filePath.toString().length() - 5);
                            //System.out.println("FileName: " + fileName);
                            StringBuilder path = new StringBuilder();
                            for (int i = 0; i < fileName.length(); i++) {
                                if (fileName.charAt(i) == (char) File.separatorChar) {
                                    path.append(".");
                                } else {
                                    path.append(fileName.charAt(i));
                                }
                            }
                            fileName = path.toString();
                            //System.out.println("FileName3: " + path.toString());

                            //Testing whether class is AbstractPlayer type
                            AbstractPlayer abstractPlayer = (AbstractPlayer) Class.forName(fileName).getConstructor(int.class).newInstance(depth);
                            boolean add = true;
                            for (int i = 0; i < excludedAbstracPlayers.length; i++) {
                                if (excludedAbstracPlayers[i].equals(fileName)) {
                                    add = false;
                                    break;
                                }
                            }
                            if (add) {
                                abstractPlayers.add(new Player(fileName));
                            }
                        } catch (ClassNotFoundException | NoSuchMethodException | SecurityException |
                                InstantiationException | IllegalAccessException | IllegalArgumentException |
                                InvocationTargetException e) {
                        }
                    }
                }
            }
            );
            Collections.sort(abstractPlayers);
            List<String> packages = new ArrayList<>();
            for (Player foundPlayer : abstractPlayers) {
                StringBuilder pckage = new StringBuilder();
                int index = foundPlayer.toString().lastIndexOf(".");
                for (int i = 0; i < index - 1; i++) {
                    pckage.append(foundPlayer.getPlayer().charAt(i));
                }
                if (!packages.contains(pckage.toString())) {
                    packages.add(pckage.toString());
                }
            }
            for (int i = 0; i < packages.size(); i++) {
                JPanel panelPackages = new JPanel();
                panelPackages.setLayout(new GridLayout(0, 1, 1, 1));
                panelPackages.setAlignmentX(Component.LEFT_ALIGNMENT);
                int size = 50;
                size -= packages.get(i).length();
                StringBuilder packageName = new StringBuilder();
                for (int j = 0; j < size; j++) {
                    packageName.append("-");
                }
                packageName.append(packages.get(i));
                for (int j = 0; j < size; j++) {
                    packageName.append("-");
                }
                panelPackages.add(new JLabel(packageName.toString()), BorderLayout.PAGE_START);

                for (Player foundPlayers : abstractPlayers) {
                    StringBuilder pckage = new StringBuilder();
                    int index = 0;
                    index = foundPlayers.getPlayer().toString().lastIndexOf(".");
                    for (int k = 0; k < index; k++) {
                        //if (playersEncontrado.getPlayer().charAt(k) != '.') {
                        pckage.append(foundPlayers.getPlayer().charAt(k));
                        //} else {
                        // index = k++;
                        //break;
                        //}
                    }
                    StringBuilder playerName = new StringBuilder();
                    for (int j = ++index; j < foundPlayers.getPlayer().length(); j++) {
                        playerName.append(foundPlayers.getPlayer().charAt(j));
                    }
                    if (pckage.toString().equals(packages.get(i))) {
                        JPanel panelPlayers = new JPanel();
                        panelPlayers.setLayout(new FlowLayout(FlowLayout.LEADING));
                        panelPlayers.add(foundPlayers.getjCheckBox());
                        JLabel label = new JLabel(playerName.toString());
                        label.addMouseListener(new java.awt.event.MouseAdapter() {
                            public void mouseClicked(java.awt.event.MouseEvent evt) {
                                foundPlayers.getjCheckBox().setSelected(!foundPlayers.getjCheckBox().isSelected());
                            }
                        });
                        panelPlayers.add(label);
                        panelPackages.add(panelPlayers);
                        mainPanel.add(panelPackages);
                        mainPanel.revalidate();
                        mainPanel.repaint();
                        pack();
                    }
                }
            }
        } catch (IOException ex) {
        }
    }

    private void close() {
        this.dispose();
    }

    public static void main(String args[]) throws IOException {
        OthelloTournament othelloChampionship = new OthelloTournament();
    }

    @Override
    public void run() {
        if (gameType.equals("KnockOut")) {
            tournamentType = SCORESTYLE2;
            List<Player> winners = new ArrayList<>();
            List<Player> nextGames = new ArrayList<>();
            Thread resultsBoard = new Thread(board);
            resultsBoard.start();

            int nGame = 0;
            List<Player> miniGame = new ArrayList<>();
            do {
                for (Iterator<Player> iterator = abstractPlayers.iterator(); iterator.hasNext();) {
                    Player player = iterator.next();
                    miniGame.add(player);
                    if (miniGame.size() != 2) {
                        continue;
                    }
                    nGame++;
                    match(winners, miniGame, nextGames, nGame);
                    miniGame = new ArrayList<>();
                    if (winners.size() == (abstractPlayers.size() / 2)) {
                        abstractPlayers = winners;
                        if (winners.size() != 1) {
                            winners = new ArrayList<>();
                        }
                    }

                }
            } while (abstractPlayers.size() != 1);

            JPanel p = new JPanel();
            p.setLayout(new GridLayout(1, 1));
            first = winners.get(0);
            p.add(new JLabel(first.getPlayer()));
            board.add(p);
            if (nextGames.get(nextGames.size() - 1).getPlayer().equals(first.getPlayer())) {
                second = nextGames.get(nextGames.size() - 2);
            } else {
                second = nextGames.get(nextGames.size() - 1);
            }

            List<Player> lastMatch = new ArrayList<>();
            for (int i = nextGames.size() - 6; i < nextGames.size(); i++) {//find the three last players
                if ((!nextGames.get(i).getPlayer().equals(second.getPlayer())) && !(nextGames.get(i).getPlayer().equals(first.getPlayer()))) {
                    lastMatch.add(nextGames.get(i));
                }
            }
            miniGame = new ArrayList<>();
            miniGame.add(lastMatch.get(0));
            miniGame.add(lastMatch.get(1));
            nGame++;
            match(winners, miniGame, nextGames, nGame);
            Winner winner = new Winner("1st - " + first.getPlayer());
            board.add(winner.getGui());
            winner = new Winner("2nd - " + second.getPlayer());
            board.add(winner.getGui());
            winner = new Winner("3rd - " + third.getPlayer());
            board.add(winner.getGui());
            winner = new Winner("4th - " + fourth.getPlayer());
            board.add(winner.getGui());

        } else {
            Thread resultsBoard = new Thread(board);
            resultsBoard.start();
            Collections.shuffle(abstractPlayers);
            int gameNumber = 1;
            for (int i = 0; i < abstractPlayers.size(); i++) {
                for (int j = 0; j < abstractPlayers.size(); j++) {
                    if (i != j) {
                        String[] players = new String[2];
                        players[0] = abstractPlayers.get(i).getPlayer();
                        players[1] = abstractPlayers.get(j).getPlayer();
                        PvpDialog pvp = new PvpDialog(players[0], players[1]);
                        PvpScore pvpScore = new PvpScore("#" + gameNumber++, players[0], players[1]);
                        btResult.add(pvpScore);
                        if (!this.gameAutomatic.equals(NEXTGAMEAUTO)) {
                            String[] options = {"OK"};
                            JOptionPane.showOptionDialog(null, pvp.getPanel(), "Next Game", JOptionPane.NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
                        }
                        System.out.print(players[0] + " x " + players[1] + " - ");
                        try {
                            Game.main(players);
                        } catch (Exception e) {
                            System.err.println("Error: " + e.getMessage());
                        }
                        //winner - player 1
                        if (Game.winner.substring(6).equals(players[0])) {
                            System.out.println("winner: " + players[0] + "\n");
                            if (tournamentType.equals(SCORESTYLE1)) {
                                abstractPlayers.get(i).setScore(3);
                                pvpScore.setPlayer1Score(3 + "");
                                pvpScore.setPlayer2Score(0 + "");
                            } else if (tournamentType.equals(SCORESTYLE2)) {
                                if (abstractPlayers.get(i).getPlayer().equals(display.Display.player1class.getClass().getCanonicalName())) {
                                    abstractPlayers.get(i).setScore(Integer.parseInt(display.Display.player1Score.getText()));
                                } else {
                                    abstractPlayers.get(i).setScore(Integer.parseInt(display.Display.player2Score.getText()));
                                }
                                pvpScore.setPlayer1Score(display.Display.player1Score.getText());
                                pvpScore.setPlayer2Score(display.Display.player2Score.getText());
                            }
                            pvpScore.setWinner(players[0]);
                            saveGameRecord(Game.getGameRecordFrame(), players[0] + " x " + players[1] + " - " + "vencedor: " + players[0], pvpScore);
                        } //winner - player 2
                        else if (Game.winner.substring(6).equals(players[1])) {
                            System.out.println("winner: " + players[1] + "\n");
                            pvpScore.setWinner(players[1]);
                            saveGameRecord(Game.getGameRecordFrame(), players[0] + " x " + players[1] + " - " + "vencedor: " + players[1], pvpScore);
                            if (tournamentType.equals(SCORESTYLE1)) {
                                abstractPlayers.get(j).setScore(3);
                                pvpScore.setPlayer2Score(3 + "");
                                pvpScore.setPlayer1Score(0 + "");
                            } else if (tournamentType.equals(SCORESTYLE2)) {
                                if (abstractPlayers.get(j).getPlayer().equals(display.Display.player1class.getClass().getCanonicalName())) {
                                    abstractPlayers.get(j).setScore(Integer.parseInt(display.Display.player1Score.getText()));
                                } else {
                                    abstractPlayers.get(j).setScore(Integer.parseInt(display.Display.player2Score.getText()));
                                }
                                pvpScore.setPlayer1Score(display.Display.player1Score.getText());
                                pvpScore.setPlayer2Score(display.Display.player2Score.getText());
                            }
                        } //draw
                        else {
                            System.out.println("draw\n");
                            if (tournamentType.equals(SCORESTYLE1)) {
                                abstractPlayers.get(i).setScore(1);
                                abstractPlayers.get(j).setScore(1);
                                pvpScore.setPlayer1Score(1 + "");
                                pvpScore.setPlayer2Score(1 + "");
                            } else if (tournamentType.equals(SCORESTYLE2)) {
                                if (abstractPlayers.get(j).getPlayer().equals(display.Display.player1class.getClass().getCanonicalName())) {
                                    abstractPlayers.get(j).setScore(Integer.parseInt(display.Display.player1Score.getText()));
                                } else {
                                    abstractPlayers.get(j).setScore(Integer.parseInt(display.Display.player2Score.getText()));
                                }
                                if (abstractPlayers.get(i).getPlayer().equals(display.Display.player1class.getClass().getCanonicalName())) {
                                    abstractPlayers.get(i).setScore(Integer.parseInt(display.Display.player1Score.getText()));
                                } else {
                                    abstractPlayers.get(i).setScore(Integer.parseInt(display.Display.player2Score.getText()));
                                }
                                pvpScore.setPlayer1Score(display.Display.player1Score.getText());
                                pvpScore.setPlayer2Score(display.Display.player2Score.getText());
                            }
                            pvpScore.setWinner("draw");
                            saveGameRecord(Game.getGameRecordFrame(), players[0] + " x " + players[1] + "draw", pvpScore);
                        }
                        if (!this.gameAutomatic.equals(NEXTGAMEAUTO)) {                            
                            JOptionPane.showMessageDialog(null, "End Game", "Warning", JOptionPane.INFORMATION_MESSAGE);
                            
                        }
                        board.add(pvpScore.getPanel());
                        Game.closeDisplay();
                    }
                }
            }
            Collections.sort(abstractPlayers);
            for (int i = 0; i < abstractPlayers.size(); i++) {
                Qualifying qualificados = new Qualifying((i + 1) + "", abstractPlayers.get(i).getPlayer(), abstractPlayers.get(i).getScore() + "");
                listQualifying.add(qualificados);
                board.add(qualificados.getPanel());
                System.out.println((i + 1) + ": " + abstractPlayers.get(i));
            }
        }
    }

    private void match(List<Player> winners, List<Player> miniGame, List<Player> sequenciaDeJogos, int nGame) {
        int i = 0, j = 1;
        for (int m = 0; m < 2; m++) {//2 matches by game
            try {
                String[] dupla = new String[2];
                if (m == 0) {
                    dupla[0] = miniGame.get(0).getPlayer();
                    dupla[1] = miniGame.get(1).getPlayer();
                    sequenciaDeJogos.add(miniGame.get(0));
                    sequenciaDeJogos.add(miniGame.get(1));
                } else {
                    int score = miniGame.get(i).getScore();
                    miniGame.get(i).setScore(miniGame.get(j).getScore());
                    miniGame.get(j).setScore(score);
                    dupla[0] = miniGame.get(1).getPlayer();
                    dupla[1] = miniGame.get(0).getPlayer();
                }
                PvpDialog pvp = new PvpDialog(dupla[0], dupla[1]);
                PvpScore pvpScore = new PvpScore("#" + nGame + "." + (m + 1), dupla[0], dupla[1]);
                btResult.add(pvpScore);
                if (!this.gameAutomatic.equals(NEXTGAMEAUTO)) {
                    String[] options = {"OK"};
                    JOptionPane.showOptionDialog(null, pvp.getPanel(), "Next match", JOptionPane.NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
                }
                System.out.print(dupla[0] + " x " + dupla[1] + " - ");
                Game.main(dupla);

                if (Game.winner.substring(6).equals(dupla[0])) {//winner player1
                    if (tournamentType.equals("Score tournament style")) {
                        miniGame.get(i).setScore(3);
                        pvpScore.setPlayer1Score(3 + "");//3 points player1
                        pvpScore.setPlayer2Score(0 + "");//3 points player2
                    }
                    if (tournamentType.equals(SCORESTYLE2)) {
                        if (miniGame.get(i).getPlayer().equals(display.Display.player1class.getClass().getCanonicalName())) {
                            miniGame.get(i).setScore(Integer.parseInt(display.Display.player1Score.getText()));//pontos player1
                        } else {
                            miniGame.get(i).setScore(Integer.parseInt(display.Display.player2Score.getText()));//pontos player2
                        }
                        pvpScore.setPlayer1Score(display.Display.player1Score.getText());
                        pvpScore.setPlayer2Score(display.Display.player2Score.getText());
                    }
                    pvpScore.setWinner(dupla[0]);
                    saveGameRecord(Game.getGameRecordFrame(), dupla[0] + " x " + dupla[1] + " - " + "winner: " + dupla[0], pvpScore);
                } else if (Game.winner.substring(6).equals(dupla[1])) {//winner player2
                    if (tournamentType.equals(SCORESTYLE1)) {
                        miniGame.get(j).setScore(3);
                        pvpScore.setPlayer2Score(3 + "");
                        pvpScore.setPlayer1Score(0 + "");
                    }
                    if (tournamentType.equals(SCORESTYLE2)) {
                        if (miniGame.get(j).getPlayer().equals(display.Display.player1class.getClass().getCanonicalName())) {
                            miniGame.get(j).setScore(Integer.parseInt(display.Display.player1Score.getText()));
                        } else {
                            miniGame.get(j).setScore(Integer.parseInt(display.Display.player2Score.getText()));
                        }
                        pvpScore.setPlayer1Score(display.Display.player1Score.getText());
                        pvpScore.setPlayer2Score(display.Display.player2Score.getText());
                    }
                    pvpScore.setWinner(dupla[1]);
                    saveGameRecord(Game.getGameRecordFrame(), dupla[0] + " x " + dupla[1] + " - " + "winner: " + dupla[1], pvpScore);
                } else {//draw
                    //gravarArq.println("draw");
                    if (tournamentType.equals(SCORESTYLE1)) {
                        miniGame.get(i).setScore(1);
                        miniGame.get(j).setScore(1);
                        pvpScore.setPlayer1Score(1 + "");//1 ponto player 1
                        pvpScore.setPlayer2Score(1 + "");//1 ponto player 2
                    } else if (tournamentType.equals(SCORESTYLE2)) {
                        if (miniGame.get(j).getPlayer().equals(display.Display.player1class.getClass().getCanonicalName())) {
                            miniGame.get(j).setScore(Integer.parseInt(display.Display.player1Score.getText()));//ponto player 2
                        } else {
                            miniGame.get(j).setScore(Integer.parseInt(display.Display.player2Score.getText()));//ponto player 2
                        }
                        if (miniGame.get(i).getPlayer().equals(display.Display.player1class.getClass().getCanonicalName())) {
                            miniGame.get(i).setScore(Integer.parseInt(display.Display.player1Score.getText()));//ponto player 1
                        } else {
                            miniGame.get(i).setScore(Integer.parseInt(display.Display.player2Score.getText()));//ponto player 1
                        }
                        pvpScore.setPlayer1Score(display.Display.player1Score.getText());
                        pvpScore.setPlayer2Score(display.Display.player2Score.getText());
                    }
                    System.out.println("sc1" + miniGame.get(i).getScore());
                    System.out.println("sc2" + miniGame.get(j).getScore());
                    pvpScore.setWinner("draw");
                    saveGameRecord(Game.getGameRecordFrame(), dupla[0] + " x " + dupla[1] + "draw", pvpScore);
                }
                if (!this.gameAutomatic.equals(NEXTGAMEAUTO)) {
                    JOptionPane.showMessageDialog(null, "End Game", "Warning", JOptionPane.INFORMATION_MESSAGE);
                }
                board.add(pvpScore.getPanel());

                Game.closeDisplay();

            } catch (Exception ex) {
                Logger.getLogger(OthelloTournament.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (miniGame.get(i).getScore() > miniGame.get(j).getScore()) {
            winners.add(miniGame.get(j));
            tournament.gui.Winner winner = new Winner("Winner: " + miniGame.get(j).getPlayer());
            board.add(winner.getGui());
            third = miniGame.get(j);
            fourth = miniGame.get(i);
        } else {
            winners.add(miniGame.get(i));
            tournament.gui.Winner winner = new Winner("Winner: " + miniGame.get(i).getPlayer());
            board.add(winner.getGui());
            third = miniGame.get(i);
            fourth = miniGame.get(j);
        }
    }

    public void saveGameRecord(JFrame frame, String competidores, PvpScore pvpScore) {
        try {
            frame.validate();
            frame.repaint();            
            Container c = frame.getContentPane();
            c.validate();
            c.repaint();
            BufferedImage im = new BufferedImage(c.getWidth(), c.getHeight(), BufferedImage.TYPE_INT_ARGB);
            c.paint(im.getGraphics());
            pvpScore.setGameRecord(im);
        } catch (Exception ex) {
            Logger.getLogger(OthelloTournament.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }
}
