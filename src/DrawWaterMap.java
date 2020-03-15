import waterflowsim.Cell;
import waterflowsim.Vector2D;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

/**
 * @author Martin Jakubašek
 * @version 1.0
 */
public class DrawWaterMap extends JPanel {

    private int arrowlength = 40;
    private int arrowThickness = 10;

    private Color waterColor = Color.BLUE;

    private Cell[] data;
    private int width;
    private int height;

    public DrawWaterMap(Cell[] data, int width, int height) {
        this.data = data;
        this.width = width;
        this.height = height;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2D = (Graphics2D) g;
        drawWaterLayer(g2D);

        drawWaterFlowLabel(new Point2D.Double(500,500), new Vector2D<Double>(1.0,1.0), "", g2D);
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

        for(int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (!data[width * y + x].isDry()) {
                    Line2D point = new Line2D.Double(x,y,x,y);
                    g.draw(point);
                }
            }
        }
    }

    void drawWaterFlowLabel(Point2D position, Vector2D<Double> dirFlow, String name, Graphics2D g) {

        //normalizace vektoru
        double magnitude = Math.sqrt(dirFlow.x * dirFlow.x + dirFlow.y * dirFlow.y);
        double normX = dirFlow.x/magnitude;
        double normY = dirFlow.y/magnitude;
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

        g.fillPolygon(new int[] {(int) cX, (int) dX, (int) eX}, new int[]{(int) cY, (int) dY, (int) eY}, 3);
        g.setStroke(new BasicStroke(arrowThickness));
        g.draw(body);

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
