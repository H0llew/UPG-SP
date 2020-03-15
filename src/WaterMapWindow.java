import waterflowsim.Cell;

import javax.swing.*;

/**
 * Třída slouží pro vytvářené nového okna se simulací toku vody v krajině
 *
 * @author Martin Jakubašek
 * @version 1.0
 */
public class WaterMapWindow {

    private Cell[] data;
    private int width;
    private int height;

    private DrawWaterMap waterMap;

    /**
     * Vytvoří novou instanci {@link WaterMapWindow}
     *
     * @param data data výškové mapy krajiny
     * @param width šířka krajiny
     * @param height výška krajiny
     */
    public WaterMapWindow(Cell[] data, int width, int height) {
        this.data = data;
        this.width = width;
        this.height = height;
    }

    /**
     * Vytvoří samotné okno výškové krajiny mapy
     *
     * @return nové okno výškové mapy krajiny
     */
    public JFrame create() {
        JFrame jFrame = new JFrame();
        jFrame = Window.createBasicWindow(width, height, "new Window");

        waterMap = new DrawWaterMap(data, width, height);
        jFrame.add(waterMap);

        return jFrame;
    }
}
