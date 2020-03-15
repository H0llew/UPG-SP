package taks_1;

import waterflowsim.Simulator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainWindow {

    public static void main(String[] args) {
        JFrame mainFrame = new JFrame();
        mainFrame.setSize(500,500);
        mainFrame.setTitle("Martin Jakubašek");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setVisible(true);

        Simulator.runScenario(0);

        DrawLandscape landscape = new DrawLandscape(Simulator.getData(),
                                                    Simulator.getDimension().x,
                                                    Simulator.getDimension().y);
        mainFrame.add(landscape);
        landscape.setPreferredSize(new Dimension(500,500));
        mainFrame.pack();

        System.out.println("X: " + Simulator.getStart().x);
        System.out.println("Y: " + Simulator.getStart().y);
        System.out.println("-------------");
        System.out.println("Delta: " + Simulator.getDelta().x + "|" + Simulator.getDelta().y);
        System.out.println("Dimenze: " + Simulator.getDimension().x + "|" + Simulator.getDimension().y);

        /*
        Timer timer;
        int timerPeriod = 1; // Prekreslit okno 25krat za 1000 milisekund
        long startTime = System.currentTimeMillis();
        timer = new Timer(timerPeriod, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Simulator.nextStep(0.010);
                landscape.repaint();
            }
        });
        timer.start();

         */

        while(true) {
            Simulator.nextStep(0.9999999);
            landscape.repaint();
        }
    }
}
