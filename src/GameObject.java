public abstract class GameObject {
    protected Point pos = new Point();
    protected Point v = new Point();
    public double life = 0;
    protected double maxLife = 0;
    public double radius = 0;

    public abstract void step(double dt);

    public abstract void collisionHandler(GameObject go);

    public Point getPos() {
        return pos;
    }
}
