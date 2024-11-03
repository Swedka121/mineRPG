package mine.Utils;

public class Vector3 {
    float y;
    float z;
    float x;

    public Vector3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3 getInt() {
        int x = (int) this.x;
        int y = (int) this.y;
        int z = (int) this.z;
        return new Vector3(x, y, z);
    }

    public float getY() {
        return this.y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getZ() {
        return this.z;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public float getX() {
        return this.x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public Vector3 add(Vector3 toAdd) {
        this.x += toAdd.getX();
        this.y += toAdd.getY();
        this.z += toAdd.getZ();

        return this;
    }

    public String toCode() {
        return this.x + "-" + this.y + "-" + this.z;
    }
}
