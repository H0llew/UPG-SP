import waterflowsim.Simulator;

import javax.swing.*;

public class TestLandscape {

    private static JFrame waterMapFrame;

    private final static String windowTitle = "TEST";

    public static void main(String[] args) {
        JFrame win = new JFrame();
        win.setTitle("A19B0069P");

        Simulator.runScenario(0);
        Simulator.nextStep(0.99);

        DrawLandscape drawLandscape = new DrawLandscape(Simulator.getDimension(), Simulator.getData(), Simulator.getDelta());
        win.add(drawLandscape);
        win.pack();

        win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        win.setLocationRelativeTo(null);
        win.setVisible(true);

        while (true) {
            Simulator.nextStep(0.9);
            drawLandscape.repaint();
        }
    }
}
