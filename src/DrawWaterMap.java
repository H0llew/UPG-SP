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
import java.util.ArrayList;
import java.util.List;

/**
 * @author Martin Jakubašek
 * @version 1.0
 */
public class DrawWaterMap extends JPanel {

    private final int arrowlengthExt = 5;
    private final int arrowThickness = 5;

    private int arrowlength;
    private boolean isFistDraw = true;

    private Color waterColor = Color.BLUE;

    private Cell[] data;
    private WaterSourceUpdater[] waterSources;
    private int width;
    private int height;

    public DrawWaterMap(Cell[] data, WaterSourceUpdater[] waterSources, int width, int height) {
        this.data = data;
        this.waterSources = waterSources;
        this.width = width;
        this.height = height;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2D = (Graphics2D) g;

        if (isFistDraw) {
            arrowlength = getArrowlength(g2D);
            isFistDraw = false;
        }

        g2D.translate(50, 50);
        drawWaterLayer(g2D);

        drawWaterSources(g2D);
    }

    /**
     * Vykreslí aktuální stav toku vody v krajině poskytnutý simulátorem
     *
     * @param g {@link Graphics2D}
     */
    private void drawWaterFlowState(Graphics2D g) {

    }

    /**
     * Vykreslí barevné výškové mapy a vrtevnice
     *
     * @param g {@link Graphics2D}
     */
    private void drawTerrain(Graphics2D g) {

    }

    /**
     * Vykreslé rastrový obrázekm, ve kterém každý pixel odpovídá jednomu bodu krajiny,
     * přičemž barva a průhlednost pixelů s vodou je odličná oproti pixelům, kde voda není.
     * Výsledný obrázek je roztažen tak, aby nedocházelo ke zkreslení v nějakém směru
     *
     * @param g {@link Graphics2D}
     */
    private void drawWaterLayer(Graphics2D g) {

        BufferedImage waterImage = getWaterImage();

        g.drawImage(waterImage, 0, 0, waterImage.getWidth(), waterImage.getHeight(), null);
    }

    /**
     * Vytvoří a vrátí rastrový obrázek vody v krajině.
     * Voda je vykreslena na obrázek pomocí {@link #drawWater(Graphics2D)}
     *
     * @return rastrový obrázek vody v krajině
     */
    private BufferedImage getWaterImage() {

        BufferedImage waterImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g = waterImage.createGraphics();
        g.setColor(waterColor);

        drawWater(g);

        return waterImage;
    }

    /**
     * Vykreslí vodu krajiny
     *
     * @param g {@link Graphics2D}
     */
    private void drawWater(Graphics2D g) {

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (!data[width * y + x].isDry()) {
                    Line2D point = new Line2D.Double(x, y, x, y);
                    g.draw(point);
                }
            }
        }
    }

    private void drawWaterSources(Graphics2D g) {
        for (WaterSourceUpdater source : waterSources) {
            int y = source.getIndex() / width;
            int x = source.getIndex() % width;
            //drawWaterFlowLabel(new Point2D.Double(x,y), data[source.getIndex()].getGradient(), source.getName(), g);
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setFont(new Font("Arial", Font.CENTER_BASELINE, 15));
            Vector2D<Double> gradient = Simulator.getGradient(source.getIndex());
            Vector2D<Double> bzum = new Vector2D<>(-gradient.x, -gradient.y);
            drawWaterFlowLabel(new Point2D.Double(x, y), bzum, source.getName(), g);
        }
    }

    private void drawWaterFlowLabel(Point2D position, Vector2D<Double> dirFlow, String name, Graphics2D g) {

        //normalizace vektoru
        double magnitude = Math.sqrt(dirFlow.x * dirFlow.x + dirFlow.y * dirFlow.y);
        double normX = dirFlow.x / magnitude;
        double normY = dirFlow.y / magnitude;
        //tvorba "těla" šipky
        double vX = normX * arrowlength;
        double vY = normY * arrowlength;

        double aX = position.getX();
        double aY = position.getY();
        double bX = aX + vX;
        double bY = aY + vY;
        Line2D body = new Line2D.Double(aX, aY, bX, bY);
        //tvorba "hlavy" šipky
        double pNormX = -normY;
        double pNormY = normX;

        double cX = bX + pNormX * arrowThickness * 2;
        double cY = bY + pNormY * arrowThickness * 2;
        double dX = bX - pNormX * arrowThickness * 2;
        double dY = bY - pNormY * arrowThickness * 2;

        double eX = bX + vX / 2;
        double eY = bY + vY / 2;
        //vykreslení šipky
        g.fillPolygon(new int[]{(int) cX, (int) dX, (int) eX}, new int[]{(int) cY, (int) dY, (int) eY}, 3);
        g.setStroke(new BasicStroke(arrowThickness));
        g.draw(body);

        double sX = (aX + bX) / 2;
        double sY = (aY + bY) / 2;

        AffineTransform oldAf = g.getTransform();

        double angle = Math.toDegrees(Math.atan2(normY, normX));
        //System.out.println(angle + "/" + name);
        //double tX = (angle < 90 || angle > 270) ? aX : bX;
        //double tY = (angle < 90 || angle > 270) ? aY : bY;

        double tX;
        double tY;
        FontMetrics fm = g.getFontMetrics();
        double textOffset = (arrowlength - fm.stringWidth(name)) / 4.0;

        if ((angle > 90 && angle < 270) || (angle < -90 && angle > -270)) {

            tX = aX + (normX * (arrowlength - arrowlengthExt - textOffset));
            tY = aY + (normY * (arrowlength - arrowlengthExt - textOffset));

            angle += 180;
        } else {
            tX = aX + (normX * (arrowlengthExt + textOffset));
            tY = aY + (normY * (arrowlengthExt + textOffset));
        }

        //double tX = aX + (normX * (arrowlength - arrowlengthExt));
        //double tY = aY + (normY * (arrowlength - arrowlengthExt));
        g.translate(tX, tY);

        g.rotate(Math.toRadians(angle));
        g.translate(0, -arrowThickness);
        g.drawString(name, 0, 0);

        g.setTransform(oldAf);
    }

    private void createArrow() {

    }

    /**
     * Vrátí délku těla šipky, kde délka je určena šířkou nejdelšího textu
     *
     * @param g {@link Graphics2D}
     * @return šířka těla šipky
     */
    private int getArrowlength(Graphics2D g) {

        int max = 0;
        FontMetrics fm = g.getFontMetrics();

        for (WaterSourceUpdater source : waterSources) {
            int arrowLength = fm.stringWidth(source.getName());
            max = Math.max(arrowLength, max);
        }

        return max + (2 * arrowlengthExt);
    }

    /**
     * Nastaví barvu vody
     *
     * @param waterColor {@link Color}
     */
    public void setWaterColor(Color waterColor) {
        this.waterColor = waterColor;
    }

    /**
     * Vrátí barvu vody
     *
     * @return barva vody
     */
    public Color getWaterColor() {
        return waterColor;
    }
}
