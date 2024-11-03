package World.Entity;

import mine.Utils.Vector3;

public class Player {
    private Vector3 position;
    private Vector3 lookAt;
    private int renderDis = 1;

    public Player(Vector3 position, Vector3 lookAt) {
        this.position = position;
        this.lookAt = lookAt;
    }

    public Vector3 getPosition() {
        return position;
    }

    public void setPosition(Vector3 position) {
        this.position = position;
    }

    public Vector3 getLookAt() {
        return lookAt;
    }

    public void setLookAt(Vector3 lookAt) {
        this.lookAt = lookAt;
    }

    public int getRenderDis() {
        return renderDis;
    }
}
