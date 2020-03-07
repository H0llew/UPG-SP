import waterflowsim.Cell;
import waterflowsim.Scenarios;
import waterflowsim.Simulator;
import waterflowsim.WaterSourceUpdater;

import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) {

        JFrame mainFrame = new JFrame();
        mainFrame.setSize(500,500);
        mainFrame.setTitle("Martin Jakubašek");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setVisible(true);

        Scenarios[] scenarios = Simulator.getScenarios();
        Scenarios upg = scenarios[1];
        Simulator.runScenario(2);
        Cell[] cells = Simulator.getData();
        int counter = 0;
        for (Cell cell : cells) {
            counter++;
            if (cell.getWaterLevel() > 0)
                System.out.println("hi");
        }

        System.out.println("Počet buněk = " + counter);

        Test drawingPanel = new Test(cells);
        drawingPanel.setPreferredSize(new Dimension(800, 800));
        mainFrame.add(drawingPanel);
        mainFrame.pack();

        System.out.println("W x H: " + Simulator.getDimension().x + "/" + Simulator.getDimension().y);

        Simulator.nextStep(0.99999);
        Simulator.nextStep(0.99999);
        Simulator.nextStep(0.99999);
        Simulator.nextStep(0.99999);
        Simulator.nextStep(0.99999);Simulator.nextStep(0.99999);Simulator.nextStep(0.99999);Simulator.nextStep(0.99999);Simulator.nextStep(0.99999);Simulator.nextStep(0.99999);Simulator.nextStep(0.99999);Simulator.nextStep(0.99999);Simulator.nextStep(0.99999);Simulator.nextStep(0.99999);Simulator.nextStep(0.99999);Simulator.nextStep(0.99999);Simulator.nextStep(0.99999);Simulator.nextStep(0.99999);Simulator.nextStep(0.99999);Simulator.nextStep(0.99999);Simulator.nextStep(0.99999);Simulator.nextStep(0.99999);Simulator.nextStep(0.99999);Simulator.nextStep(0.99999);Simulator.nextStep(0.99999);Simulator.nextStep(0.99999);Simulator.nextStep(0.99999);Simulator.nextStep(0.99999);Simulator.nextStep(0.99999);Simulator.nextStep(0.99999);Simulator.nextStep(0.99999);Simulator.nextStep(0.99999);Simulator.nextStep(0.99999);Simulator.nextStep(0.99999);Simulator.nextStep(0.99999);Simulator.nextStep(0.99999);Simulator.nextStep(0.99999);Simulator.nextStep(0.99999);Simulator.nextStep(0.99999);Simulator.nextStep(0.99999);Simulator.nextStep(0.99999);Simulator.nextStep(0.99999);Simulator.nextStep(0.99999);Simulator.nextStep(0.99999);Simulator.nextStep(0.99999);Simulator.nextStep(0.99999);Simulator.nextStep(0.99999);


    }
}
