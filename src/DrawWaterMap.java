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

    private int plusUltra = 0;

    private LandscapeDimensions ld;

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
            ld = new LandscapeDimensions(width, height,
                    Simulator.getDelta().x , Simulator.getDelta().y,
                    arrowlength + arrowlength / 2,
                    Simulator.getWaterSources());
            isFistDraw = false;
        }

        //g2D.translate(50, 50);
        //drawWaterLayer(g2D);

        //drawWaterSources(g2D);
        BufferedImage arrowImage = getArrowsImage();
        //g.drawImage(arrowImage, 0, 0, arrowImage.getWidth(), arrowImage.getHeight(), null);

        BufferedImage finalImage = new BufferedImage(5000,2000, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = finalImage.createGraphics();
        drawWaterLayer(graphics2D);
        graphics2D.drawImage(arrowImage, 0, 0, arrowImage.getWidth(), arrowImage.getHeight(), null);

        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2D.drawImage(finalImage,0, 0, finalImage.getWidth() + plusUltra, finalImage.getHeight() + plusUltra, null);
        plusUltra += 5;
        if (plusUltra == 100)
            plusUltra = 100;
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
        Point2D trans = ld.getOverlap();
        AffineTransform old = g.getTransform();
        g.translate(Math.abs(trans.getX()), Math.abs(trans.getY()));
        g.drawImage(waterImage, 0, 0, (int) (waterImage.getWidth() * ld.getScale().getX()), (int) (waterImage.getHeight() * ld.getScale().getY()), null);
        g.setTransform(old);
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

    /**
     * Metoda prostřednictvím metody drawWaterFlowLabel vykreslí všechny vodní zdroje v
     * krajině, poskytnuté metodou simulátoru getWaterSources.
     *
     * @param g {@link Graphics2D}
     */
    private void drawWaterSources(Graphics2D g) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setFont(new Font("Arial", Font.PLAIN, 15));

        for (WaterSourceUpdater source : waterSources) {
            int y = source.getIndex() / width;
            int x = source.getIndex() % width;

            Vector2D<Double> gradient = Simulator.getGradient(source.getIndex());
            drawWaterFlowLabel(new Point2D.Double(x, y), gradient, source.getName(), g);
        }
    }

    private BufferedImage getArrowsImage() {

        Point2D dim = ld.getArrowBitmapDimensions();
        System.out.println("x: " + dim.getX() + " y: " + dim.getY());
        BufferedImage arrowImage = new BufferedImage((int) dim.getX(), (int) dim.getY(), BufferedImage.TYPE_INT_ARGB);

        Graphics2D g = arrowImage.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(Color.BLACK);

        Point2D trans = ld.getOverlap();
        AffineTransform old = g.getTransform();
        g.translate(Math.abs(trans.getX()), Math.abs(trans.getY()));

        drawWaterSources(g);
        g.setTransform(old);
        return arrowImage;
    }

    /**
     * Metoda vykreslí na zadané pozici (v pixelech v souřadném systému okna) šipku ve směru
     * dirFlow znázorňující směr toku vody a dále název vodního toku ( name).
     *
     * @param position počáteční bod
     * @param dirFlow směrový vektor
     * @param name jméno řeky
     * @param g {@link Graphics2D}
     */
    private void drawWaterFlowLabel(Point2D position, Vector2D<Double> dirFlow, String name, Graphics2D g) {

        position = new Point2D.Double(position.getX() * ld.getScale().getX(), position.getY() * ld.getScale().getY());
        drawArrow(position, dirFlow, g);
        drawArrowText(dirFlow, position, name, g);
    }

    /**
     * Vykreslí text nad šipkou
     *
     * @param heading směrový vektor
     * @param position pozice, kam se má daný text vykreslit
     * @param name text
     * @param g {@link Graphics2D}
     */
    private void drawArrowText(Vector2D<Double> heading, Point2D position, String name, Graphics2D g) {

        AffineTransform old = g.getTransform();

        //normalizace vektoru
        double magnitude = Math.sqrt(heading.x * heading.x + heading.y * heading.y);
        Vector2D<Double> normalization = new Vector2D<>(heading.x / magnitude,
                                                       heading.y / magnitude);

        double angle = Math.toDegrees(Math.atan2(normalization.y, normalization.x));
        Point2D pos;

        FontMetrics fm = g.getFontMetrics();
        double textOffset = (arrowlength - fm.stringWidth(name));

        if ((angle > 90 && angle < 270) || (angle < -90 && angle > -270)) {
            pos = new Point2D.Double(position.getX() + (normalization.x * (arrowlength - textOffset / 2)),
                                     position.getY() + (normalization.y * (arrowlength - textOffset / 2)));
            angle += 180; //kvůli relativně správnému zovrázení textu
        }
        else {
            pos = new Point2D.Double(position.getX() + (normalization.x * textOffset / 2),
                                     position.getY() + (normalization.y * textOffset / 2));
        }

        g.translate(pos.getX(), pos.getY());

        g.rotate(Math.toRadians(angle));
        g.translate(0, -arrowThickness);
        g.drawString(name, 0, 0);

        g.setTransform(old);
    }

    /**
     * Vykreslí šipku pomocí počátečního bodu a směrového vektoru
     *
     * @param start počáteční bod
     * @param heading směrový vektor
     * @param g {@link Graphics2D}
     */
    private void drawArrow(Point2D start, Vector2D<Double> heading, Graphics2D g) {
        //normalizace vektoru
        //start = new Point2D.Double(start.getX() * ld.getScale().getX(), start.getY() * ld.getScale().getY());

        double magnitude = Math.sqrt(heading.x * heading.x + heading.y * heading.y);
        Vector2D<Double> normalization = new Vector2D<>(heading.x / magnitude,
                                                       heading.y / magnitude);

        //tvorba "těla" šipky
        Vector2D<Double> arrowHeading = new Vector2D<>(normalization.x * arrowlength,  //vektor úsečky
                                                      normalization.y * arrowlength);

        Point2D pointB = new Point2D.Double(start.getX() + arrowHeading.x, //2. bod úsečky
                                            start.getY() + arrowHeading.y);

        Line2D body = new Line2D.Double(start.getX(), start.getY(), pointB.getX(), pointB.getY()); //samotné tělo

        //tvorba "hlavy" šipky
        Vector2D<Double> perpNormalization = new Vector2D<>(-normalization.y, normalization.x); //kolmý jednotkový vektor

        Point2D pointC = new Point2D.Double(pointB.getX() + (perpNormalization.x * arrowThickness * 2), //bod C
                                            pointB.getY() + (perpNormalization.y * arrowThickness * 2));

        Point2D pointD = new Point2D.Double(pointB.getX() - (perpNormalization.x * arrowThickness * 2), //bod D
                                            pointB.getY() - (perpNormalization.y * arrowThickness * 2));

        Point2D pointE = new Point2D.Double(pointB.getX() + arrowHeading.x / 2, //bod E
                                            pointB.getY() + arrowHeading.y / 2);

        //vykreslení šipky
        g.fillPolygon(new int[]{(int) pointC.getX(), (int) pointD.getX(), (int) pointE.getX()},
                      new int[]{(int) pointC.getY(), (int) pointD.getY(), (int) pointE.getY()}, 3);
        g.setStroke(new BasicStroke(arrowThickness));
        g.draw(body);
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
