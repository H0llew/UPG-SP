package taks_1;

import waterflowsim.Cell;
import waterflowsim.Simulator;
import waterflowsim.Vector2D;
import waterflowsim.WaterSourceUpdater;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class DrawLandscape extends JPanel {

    private Cell[] landscapeData;
    private int widthPixels;
    private int heightPixels;

    public DrawLandscape(Cell[] landscapeData, int widthPixels, int heightPixels) {
        this.landscapeData = landscapeData;
        this.widthPixels = widthPixels;
        this.heightPixels = heightPixels;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        //g.setColor(Color.PINK);

        Graphics2D g2 = (Graphics2D) g;
        g2.translate(100,100);
        drawWaterFlowState(g2);
    }

    private void drawWaterFlowState(Graphics2D g) {
        drawTerrain(g);
        drawWaterLayer(g);
    }

    private void drawTerrain(Graphics2D g) {

    }

    private void drawWaterLayer(Graphics2D g) {

        for (int y = 0; y < heightPixels; y++) {
            for (int x = 0; x < widthPixels; x++) {
                if (landscapeData[heightPixels*y+x].getWaterLevel() > 0) {
                    g.setColor(Color.BLUE);
                    //System.out.println(Simulator.getDelta().y);
                    Line2D line2D = new Line2D.Double(x + Simulator.getDelta().x, y + Simulator.getDelta().y,x + Simulator.getDelta().x,y + Simulator.getDelta().y);
                    g.draw(line2D);
                }
                else
                {
                    g.setColor(Color.BLACK);
                    Line2D line2D = new Line2D.Double(x + Simulator.getDelta().x, y + Simulator.getDelta().y,x + Simulator.getDelta().x,y + Simulator.getDelta().y);
                    g.draw(line2D);
                }

                //Rectangle2D rectangle2D = new Rectangle2D.Double(x,y,x * Simulator.getDelta().x, y * Simulator.getDelta().y);

                //g.draw(rectangle2D);
            }
        }
    }

    void drawWaterFlowLabel(Point2D position, Vector2D dirFlow, String name, Graphics2D g) {

    }
}
