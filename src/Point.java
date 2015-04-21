import java.awt.geom.Point2D;

public class Point extends Point2D {
    private double x = 0;
    private double y = 0;

    Point() {}

    Point(Point2D p) {
        x = p.getX();
        y = p.getY();
    }

    @Override
    public double getX() {
        return x;
    }

    @Override
    public double getY() {
        return y;
    }

    @Override
    public void setLocation(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void move(Point p, double dt) {
        x += p.x * dt;
        y += p.y * dt;
    }

    public int X() {
        return (int) x;
    }

    public int Y() {
        return (int) y;
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }

    public void rotate(double angle) {
        double _x = x, _y = y;
        x = _x * Math.cos(angle) - _y * Math.sin(angle);
        y = _x * Math.sin(angle) + _y * Math.cos(angle);
    }

    public void normalize(double len) {
        double l = this.distance(0, 0);
        if (l == 0) {
            return;
        }
        x /= l;
        x *= len;
        y /= l;
        y *= len;
    }

    public double distance2(Point p) {
        return (p.x - x) * (p.x - x) + (p.y - y) * (p.y - y);
    }
}
