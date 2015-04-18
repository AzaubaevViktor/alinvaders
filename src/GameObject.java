public abstract class GameObject {
    protected Point pos = new Point();
    protected Point v = new Point();
    protected double life = 0;

    public abstract void step(double dt);

    public Point getPos() {
        return pos;
    }
}
