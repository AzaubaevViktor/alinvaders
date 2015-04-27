public class Shrapnel extends Bullet {

    Shrapnel(Point pos, Point v, double damage, double life, double radius) {
        this.pos = pos;
        this.v = v;
        this.damage = damage;
        maxLife = this.life = life;
        this.radius = radius;
    }

}
