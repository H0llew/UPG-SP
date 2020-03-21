import waterflowsim.Cell;
import waterflowsim.Vector2D;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

public class DrawLandscape extends JPanel {

    // atributy z Simulátoru
    private Point2D landDimPix; // velikost krajiny z Simulator.getDimensions()
    private Cell[] landData; // data pomocí nichž se získávají informace o krajině


    // atributy třídy
    private Color waterColor = Color.BLUE; // barva vody -> implicitně nastavena na modrou

    // atributy pro manipulaci s obrázkem
    private Point2D offset; // offset krajiny od horního levého rohu
    private double scale = 1; // scale pomocí něhož se zmenšuje/zvětšuje obrázek, tak aby se vešel do okna
    private Point2D deltaScale; // scale pomocí něhož se změní měřítko krajiny

    private CalculateLandscape cl; // třída pro získání výpočtů nutných pro správnou vizualizaci

    /** TEST KONSTRUKTOR PRO VYKRESLOVÁNÍ VODY **/
    public DrawLandscape(Vector2D<Integer> landDimPix, Cell[] landData, Vector2D<Double> delta) {
        this.landDimPix = new Point2D.Double(landDimPix.x, landDimPix.y);
        this.landData = landData;

        cl = new CalculateLandscape(landDimPix, delta);
        deltaScale = cl.deltaScale;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2D = (Graphics2D) g;

        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        drawWaterLayer(g2D);
    }

    /**
     * Vykreslí rastrový obrázek vody v krajině.
     * Obrázek bude roztažen, tak aby vyhovoval deltě a
     * zároveň se vešel do okna a nedošlo ke zkreslení
     */
    private void drawWaterLayer(Graphics2D g2D) {

        /* Metoda pomocí metody getWaterImage vykreslí na plátno g2D
           rastrový obrázek, a ten následně zvětší/zmenší,
           tak aby se vešel do okna */

        BufferedImage landscapeImage = getLandscapeImage(); //obrázek krajiny

        scale = cl.getScale(this.getWidth(), this.getHeight());;

        int finalWidth = (int) ((landscapeImage.getWidth() * deltaScale.getX()) * scale);
        int finalHeight = (int) ((landscapeImage.getHeight()* deltaScale.getY()) * scale);

        g2D.drawImage(landscapeImage, 0, 0,
                finalWidth, finalHeight, null); //nakreslí na plátno krajinu

    }

    // metody pro vykreslení krajiny

    /**
     * Vykreslí na plátno g2D vodu v krajině z pole {@link waterflowsim.Cell} buněk
     * Jedna buňka == 1 pixel
     *
     * @param g2D {@link Graphics2D}
     */
    private void drawWater(Graphics2D g2D) {

        // vykreslení probíhá pomocí 2 vnořených loopů -> jeden pro y a druhý pro x (osy)
        for (int y = 0; y < landDimPix.getY(); y++) {
            for (int x = 0; x < landDimPix.getX(); x++) {
                if (!landData[((int) landDimPix.getX()) * y + x].isDry()) {
                    Line2D cellPoint = new Line2D.Double(x, y, x, y);
                    g2D.draw(cellPoint);
                }
            }
        }
    }

    /**
     * Vykreslí na plátno g2D terén v krajině z pole {@link waterflowsim.Cell} buněk
     * Jedna buňka == 1 pixel
     *
     * @param g2D {@link Graphics2D}
     */
    private void drawTerrain(Graphics2D g2D) {

        //NOT IMPLEMENTED
    }

    // get set private

    /**
     * Vytvoří rastrový obrázek krajiny
     *
     * @return rastrový obrázek krajiny
     */
    private BufferedImage getLandscapeImage() {

        BufferedImage waterImage = getWaterImage();
        //BufferedImage terrainImage = getTerrainImage();

        BufferedImage landscapeImage = new BufferedImage((int) landDimPix.getX(), (int) landDimPix.getY(),
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2D = landscapeImage.createGraphics();

        g2D.drawImage(waterImage, 0, 0,
                (int) landDimPix.getX(), (int) landDimPix.getY(),
                null);
        /*
        g2D.drawImage(terrainImage, 0, 0,
                (int) landDimPix.getX(), (int) landDimPix.getY(),
                null);
         */

        return landscapeImage;
    }

    /**
     * Vytvoří rastrový obrázek vody v krajině
     *
     * @return rastrový obrázek vody v krajině
     */
    private BufferedImage getWaterImage() {

        BufferedImage waterImage = new BufferedImage((int) landDimPix.getX(), (int) landDimPix.getY(),
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2D = waterImage.createGraphics();
        g2D.setColor(waterColor);

        drawWater(g2D);

        return waterImage;
    }

    /**
     * Vytvoří rastrový obrázek terénu krajiny
     *
     * @return rastrový obrázek terénu krajiny
     */
    private BufferedImage getTerrainImage() {

        //NOT IMPLEMENTED
        return null;
    }

    // get set public

    /**
     * Vrátí barvu vody
     *
     * @return barva vody
     */
    public Color getWaterColor() {
        return waterColor;
    }

    /**
     * Nastaví barvu vody
     *
     * @param waterColor nová barva vody
     */
    public void setWaterColor(Color waterColor) {
        this.waterColor = waterColor;
    }
}
