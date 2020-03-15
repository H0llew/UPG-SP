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
import java.awt.image.BufferedImage;

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

        /*
        for (int y = 0; y < heightPixels; y++) {
            for (int x = 0; x < widthPixels; x++) {
                if (!landscapeData[heightPixels*y+x].isDry()) {
                    g.setColor(Color.BLUE);
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
         */

        BufferedImage landscapeImage = createImage();
        g.drawImage(landscapeImage, 0, 0, landscapeImage.getWidth(), landscapeImage.getHeight(), null);
    }

    void drawWaterFlowLabel(Point2D position, Vector2D dirFlow, String name, Graphics2D g) {

    }

    private BufferedImage createImage() {

        BufferedImage bufferedImage = new BufferedImage(Simulator.getDimension().x,
                                                        Simulator.getDimension().y,
                                                        BufferedImage.TYPE_INT_RGB);

        Graphics2D image = bufferedImage.createGraphics();
        image.setColor(Color.BLUE);

        for (int y = 0; y < heightPixels; y++) {
            for (int x = 0; x < widthPixels; x++) {
                if (!landscapeData[widthPixels*y+x].isDry()) {
                    Line2D pixel = new Line2D.Double(x,y,x,y);
                    image.draw(pixel);
                }
            }
        }

        return bufferedImage;
    }
}
