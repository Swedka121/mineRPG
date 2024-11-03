package mine.Utils;

public class Vector2 {
    float y;
    float x;

    public Vector2(float xI, float yI) {
        x = xI;
        y = yI;
    }

    public Vector2 getInt() {
        int xS = (int) x;
        int yS = (int) y;
        return new Vector2(xS, yS);
    }

    public float getY() {
        return this.y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getX() {
        return this.x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public String toCode() {
        return this.x + "-" + this.y;
    }
}
