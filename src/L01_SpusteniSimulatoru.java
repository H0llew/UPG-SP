import waterflowsim.Simulator;

import javax.swing.*;

/**
 * @author Martin Jakubašek
 * @version 1.0
 */
public class L01_SpusteniSimulatoru {

	private static JFrame waterMapFrame;

	private final static String windowTitle = "WaterFlowSim - A19B0069P Martin Jakubašek";

	/**
	 * Hlavní metoda programu, spustí program, který vykreslí mapu vodstva a
	 * pravidelně ji aktualizuje
	 *
	 * @param args použit pro výběr scénáře (0-3)
	 */
	public static void main(String[] args) {
		Simulator.runScenario(0);
		Simulator.nextStep(0.2);
		initWaterMap();
	}

	/**
	 * Inicializuje okno s výškovou mapu krajiny
	 */
	private static void initWaterMap() {
		WaterMapWindow waterMapWindow= new WaterMapWindow(Simulator.getData(),
														  Simulator.getDimension().x,
														  Simulator.getDimension().y);
		waterMapFrame = waterMapWindow.create();
		waterMapFrame.setTitle(windowTitle);
		waterMapFrame.setVisible(true);
	}
}
