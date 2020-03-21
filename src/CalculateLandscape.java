import waterflowsim.Vector2D;
import waterflowsim.WaterSourceUpdater;

import java.awt.geom.Point2D;

public class CalculateLandscape {

    private Point2D pixLandDim;

    public Point2D trueLandDim;
    public Point2D deltaScale;

    private double minCoordX;
    private double minCoordY;
    private double maxCoordX;
    private double maxCoordY;

    public CalculateLandscape(Vector2D<Integer> landDim, Vector2D<Double> delta) {
        pixLandDim = new Point2D.Double(landDim.x, landDim.y);
        computeModelDimensions(landDim, delta);
    }

    private void computeModelDimensions(Vector2D<Integer> landDim, Vector2D<Double> delta) {
        double width = ((landDim.x - 1) * Math.abs(delta.x));
        double height = ((landDim.y - 1) * Math.abs(delta.y));

        trueLandDim = new Point2D.Double(width, height);
        deltaScale = getDeltaScale();
    }

    private Point2D getDeltaScale() {
        double scaleX = trueLandDim.getX() / pixLandDim.getX();
        double scaleY = trueLandDim.getY() / pixLandDim.getY();

        return new Point2D.Double(scaleX, scaleY);
    }

    public double getScale(int width, int height) {
        double x = Math.max(trueLandDim.getX(), maxCoordX);
        double y = Math.max(trueLandDim.getY(), maxCoordY);

        double scaleX = width / (x + Math.abs(minCoordX));
        double scaleY = height / (y + Math.abs(minCoordY));

        return Math.min(scaleY, scaleX);
    }

    public void createArrowDimension(Point2D pixLandDim, Point2D arrowOffset, double arrowLength,
                                   WaterSourceUpdater[] waterSources) {

        maxCoordX = trueLandDim.getX();
        maxCoordY = trueLandDim.getY();

        for (WaterSourceUpdater source : waterSources) {
            double y = (int) (source.getIndex() / pixLandDim.getX());
            double x = (int) (source.getIndex() % pixLandDim.getX());

            x = ((x + arrowOffset.getX()) * deltaScale.getX());
            y = ((y + arrowOffset.getY()) * deltaScale.getY());

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

    public double getMinCoordX() {
        return minCoordX;
    }

    public double getMinCoordY() {
        return minCoordY;
    }
}
