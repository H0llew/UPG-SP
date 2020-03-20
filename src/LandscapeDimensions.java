import waterflowsim.Simulator;
import waterflowsim.WaterSourceUpdater;

import java.awt.geom.Point2D;

public class LandscapeDimensions {

    private Point2D landscapeDim;
    private Point2D landscapeDimPix;

    private double minCoordX;
    private double minCoordY;
    private double maxCoordX;
    private double maxCoordY;

    public LandscapeDimensions(double widthPix, double heightPix, double deltaX, double deltaY, int arrowLength, WaterSourceUpdater[] waterSourceUpdaters) {
        landscapeDimPix = new Point2D.Double(widthPix, heightPix);
        computeModelDimensions(widthPix, heightPix, deltaX, deltaY);
        getArrowsDimension(widthPix, heightPix, arrowLength, waterSourceUpdaters);
    }

    /**
     * Vypočítá rozměry krajiny s deltou
     *
     * @param widthPix šířka krajiny
     * @param heightPix výška krajiny
     * @param deltaX delta x
     * @param deltaY delta y
     */
    private void computeModelDimensions(double widthPix, double heightPix, double deltaX, double deltaY) {
        double width = ((widthPix - 1) * Math.abs(deltaX));
        double height = ((heightPix - 1) * Math.abs(deltaY));

        landscapeDim = new Point2D.Double(width, height);
    }

    /**
     * Vrátí o kolik se musí zvětšit původní krajina
     *
     * @return hodnoty zvětšení
     */
    public Point2D getScale() {
        double scaleX = landscapeDim.getX() / landscapeDimPix.getX();
        double scaleY = landscapeDim.getY() / landscapeDimPix.getY();

        return new Point2D.Double(scaleX, scaleY);
    }

    /**
     * Vrátí jakou velikost bude mít bitmapa šipek
     *
     * @return dimensions šipkové bitmapy
     */
    public Point2D getArrowBitmapDimensions() {
        double sizeX = (minCoordX < 0) ? Math.abs(minCoordX) + maxCoordX : maxCoordX;
        double sizeY = (minCoordY < 0) ? Math.abs(minCoordY) + maxCoordY : maxCoordY;

        return new Point2D.Double(sizeX, sizeY);
    }

    /**
     * Vrátí kladný přesah x a y do záporných hodnot
     *
     * @return přesah do -x a -y
     */
    public Point2D getOverlap() {
        return new Point2D.Double(Math.abs(minCoordX), Math.abs(minCoordY));
    }

    /**
     * Určí border, ve kterém se vykreslují šipky, slouží pro zjištění bitmapy, která obsahuje šipky
     * tj. určuje min-max souřadnice
     *
     * @param widthPix šířka vykreslované krajiny
     * @param heightPix výška vykreslované krajiny
     * @param arrowLength délka šipky
     * @param waterSources data vodních zdrojů
     */
    public void getArrowsDimension(double widthPix, double heightPix, double arrowLength, WaterSourceUpdater[] waterSources) {
        double scaleX = landscapeDim.getX() / widthPix;
        double scaleY = landscapeDim.getY() / heightPix;

        maxCoordX = landscapeDim.getX();
        maxCoordY = landscapeDim.getY();

        for (WaterSourceUpdater source : waterSources) {
            double y = (int) (source.getIndex() / widthPix);
            double x = (int) (source.getIndex() % widthPix);

            x *= scaleX;
            y *= scaleY;

            // vlevo
            double borderX = x - arrowLength;
            if (borderX < 0) {
                minCoordX = Math.min(minCoordX, borderX);
            }
            // nahoře
            double borderY = y - arrowLength;
            if (borderY < 0) {
                minCoordY = Math.min(minCoordY, borderY);
            }
            // vpravo
            borderX = x + arrowLength;
            if (borderX > 0) {
                maxCoordX = Math.max(maxCoordX, borderX);
            }
            // dole
            borderY = y + arrowLength;
            if (borderY > 0) {
                maxCoordY = Math.max(maxCoordY, borderY);
            }
        }
    }
}
