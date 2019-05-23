package batmanintegral.model;

public class FPoint {

    public double x;
    public double y;

    public FPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public FPoint(FPoint point) {
        this.x = point.x;
        this.y = point.y;
    }

}
