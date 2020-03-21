import waterflowsim.Vector2D;

import java.awt.geom.Point2D;

public class CalculateLandscape {

    private Point2D pixLandDim;

    public Point2D trueLandDim;
    public Point2D deltaScale;

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

        //double scaleX = width / (trueLandDim.getX() * deltaScale.getX());
        //double scaleY = height / (trueLandDim.getY() * deltaScale.getY());

        double scaleX = width / (trueLandDim.getX());
        double scaleY = height / (trueLandDim.getY());

        return Math.min(scaleY, scaleX);
    }
}
