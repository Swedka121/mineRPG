package mine.Engine.Untils;

public class ControlsDTO {
    private boolean forward = false;
    private boolean backward = false;
    private boolean up = false;
    private boolean down = false;
    private boolean right = false;
    private boolean left = false;

    private float mouseMoveX = 0;
    private float mouseMoveY = 0;

    private float mouseDeltaX = 0;
    private float mouseDeltaY = 0;

    public float getMouseDeltaX() {
        return mouseDeltaX;
    }

    public void setMouseDeltaX(float mouseDeltaX) {
        this.mouseDeltaX = mouseDeltaX;
    }

    public float getMouseDeltaY() {
        return mouseDeltaY;
    }

    public void setMouseDeltaY(float mouseDeltaY) {
        this.mouseDeltaY = mouseDeltaY;
    }

    public void resetMoveX() {
        this.mouseMoveX = 0;
    }

    public void resetMoveY() {
        this.mouseMoveY = 0;
    }

    public void setMoveX(float move) {
        this.mouseMoveX = move;
    }

    public void setMoveY(float move) {
        this.mouseMoveY = move;
    }

    public void switchOnForward() {
        if (!this.backward) {
            this.forward = true;
        }
    }
    public void switchOffForward() {
        this.forward = false;
    }

    public void switchOnBackward() {
        if (!this.forward) {
            this.backward = true;
        }
    }
    public void switchOffBackward() {
        this.backward = false;
    }

    public void switchOnUp() {
        if (!this.down) {
            this.up = true;
        }
    }
    public void switchOffUp() {
        this.up = false;
    }

    public void switchOnDown() {
        if (!this.up) {
            this.down = true;
        }
    }
    public void switchOffDown() {
        this.down = false;
    }

    public void switchOnLeft() {
        if (!this.right) {
            this.left = true;
        }
    }
    public void switchOffLeft() {
        this.left = false;
    }

    public void switchOnRight() {
        if (!this.left) {
            this.right = true;
        }
    }
    public void switchOffRight() {
        this.right = false;
    }

    public boolean isForward() {
        return forward;
    }

    public boolean isBackward() {
        return backward;
    }

    public boolean isUp() {
        return up;
    }

    public boolean isDown() {
        return down;
    }

    public boolean isRight() {
        return right;
    }

    public boolean isLeft() {
        return left;
    }

    public float getMouseMoveX() {
        return mouseMoveX;
    }

    public float getMouseMoveY() {
        return mouseMoveY;
    }
}
