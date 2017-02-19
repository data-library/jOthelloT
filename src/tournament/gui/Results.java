/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tournament.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import tournament.OthelloTournament;

/**
 *
 * @author Marcelo Paglione
 */
public class Results implements Runnable {

    private final String PATH = System.getProperty("user.dir") + File.separator + "Results";
    private JPanel panel = new JPanel();
    private List<JPanel> listPanel = new ArrayList<>();
    private OthelloTournament othelloChampionship;
    private JFrame frame;

    public Results(OthelloTournament othelloChampionship) {
        this.othelloChampionship = othelloChampionship;
    }

    public JPanel getPanel() {
        return panel;
    }

    public void add(JPanel panel) {
        listPanel.add(panel);
        this.panel.add(panel);
        this.panel.revalidate();
        this.panel.repaint();
    }

    public void saveResults(List<PvpScore> btResults, Results resultadoParcial,List<Qualifying> listQualifyings) {
        for (int i = 0; i < btResults.size(); i++) {
            btResults.get(i).setjToggleButton1(true);
            frame.pack();
            saveFinal(btResults.get(i).getPanel(), "#"+(i + 1) );
        }
        
        frame.pack();
        for (int i = 0; i < listQualifyings.size(); i++) {
            saveFinal(listQualifyings.get(i).getPanel(), "W#"+(i + 1) );
        }

        for (int i = 0; i < btResults.size(); i++) {
            btResults.get(i).setjToggleButton1(false);
        }
        frame.pack();
    }

    public void saveFinal(JPanel panel, String jogoNro) {
        try {
            panel.validate();
            panel.repaint();
            panel.validate();
            panel.repaint();
            Container c = panel;
            BufferedImage im = new BufferedImage(c.getWidth(), c.getHeight(), BufferedImage.TYPE_INT_ARGB);
            c.paint(im.getGraphics());
            ImageIO.write(im, "PNG", new File(PATH + File.separator + "jogo" + jogoNro + ".png"));
        } catch (IOException ex) {
            Logger.getLogger(OthelloTournament.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        frame = new JFrame();
        frame.setTitle("Results - jOthelloT");
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        JScrollPane mainScroll = new JScrollPane(panel);
        mainScroll.getVerticalScrollBar().setUnitIncrement(16);
        mainScroll.setPreferredSize(new Dimension(800, 600));
        frame.getContentPane().add(mainScroll, BorderLayout.CENTER);
        JButton button = new JButton("Save");
        button.addActionListener((ActionEvent e) -> {
            saveResults(othelloChampionship.getBtResult(), othelloChampionship.getPartialResults(),othelloChampionship.getlistQualifying());
        });
        frame.getContentPane().add(button, BorderLayout.SOUTH);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
