package mine.Engine.Cameras;

import mine.App;
import mine.Engine.Untils.ControlsDTO;
import mine.Engine.WindowManager;
import mine.Utils.Vector3;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.lwjgl.glfw.*;

public class FlyCamera extends Camera {
    private ControlsDTO controls;
    private float multiplyer;

    private float yaw;
    private float pitch;

    private Vector3f position = new Vector3f(0,0,0);

    private GLFWCursorPosCallback cursorPosCallback = new GLFWCursorPosCallback() {
        @Override
        public void invoke(long window, double xpos, double ypos) {
            float lastX = controls.getMouseMoveX();
            float lastY = controls.getMouseMoveY();

            controls.setMouseDeltaX(lastX - (float) xpos);
            controls.setMouseDeltaY(lastY - (float) ypos);

            controls.setMoveX((float) xpos);
            controls.setMoveY((float) ypos);
        }
    };

    private GLFWKeyCallback keyCallback = new GLFWKeyCallback() {
        public boolean looked = true;

        @Override
        public void invoke(long window, int key, int scancode, int action, int mods) {
            if (key == GLFW.GLFW_KEY_UNKNOWN) {
                return;
            }
            if (key == GLFW.GLFW_KEY_W && action == GLFW.GLFW_PRESS) {
                controls.switchOnForward();
            }
            if (key == GLFW.GLFW_KEY_W && action == GLFW.GLFW_RELEASE) {
                controls.switchOffForward();
            }
            if (key == GLFW.GLFW_KEY_S && action == GLFW.GLFW_PRESS) {
                controls.switchOnBackward();
            }
            if (key == GLFW.GLFW_KEY_S && action == GLFW.GLFW_RELEASE) {
                controls.switchOffBackward();
            }
            if (key == GLFW.GLFW_KEY_A && action == GLFW.GLFW_PRESS) {
                controls.switchOnLeft();
            }
            if (key == GLFW.GLFW_KEY_A && action == GLFW.GLFW_RELEASE) {
                controls.switchOffLeft();
            }
            if (key == GLFW.GLFW_KEY_D && action == GLFW.GLFW_PRESS) {
                controls.switchOnRight();
            }
            if (key == GLFW.GLFW_KEY_D && action == GLFW.GLFW_RELEASE) {
                controls.switchOffRight();
            }
            if (key == GLFW.GLFW_KEY_SPACE && action == GLFW.GLFW_PRESS) {
                controls.switchOnUp();
            }
            if (key == GLFW.GLFW_KEY_SPACE && action == GLFW.GLFW_RELEASE) {
                controls.switchOffUp();
            }
            if (key == GLFW.GLFW_KEY_LEFT_SHIFT && action == GLFW.GLFW_PRESS) {
                controls.switchOnDown();
            }
            if (key == GLFW.GLFW_KEY_LEFT_SHIFT && action == GLFW.GLFW_RELEASE) {
                controls.switchOffDown();
            }
            if (key == GLFW.GLFW_KEY_L && action == GLFW.GLFW_PRESS) {
                this.looked = !looked;

                if (looked) {
                    WindowManager wnm = App.getWindow();

                    GLFW.glfwSetInputMode(wnm.getWindow(),GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
                } else {
                    WindowManager wnm = App.getWindow();

                    GLFW.glfwSetInputMode(wnm.getWindow(),GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
                }
            }
        }
    };

    public FlyCamera(Matrix4f projectiveMatrix, float multiplyer) {
        super(projectiveMatrix);
        this.controls = new ControlsDTO();

        this.multiplyer = multiplyer;

        WindowManager wnm = App.getWindow();

        GLFW.glfwSetKeyCallback(wnm.getWindow(), this.keyCallback);
        GLFW.glfwSetCursorPosCallback(wnm.getWindow(), this.cursorPosCallback);
    }

    public Matrix4f update() {
        Matrix4f DataMatrix = this.getLocalProjectiveMatrix();

        yaw = controls.getMouseDeltaX() * 0.1f;
        pitch = controls.getMouseDeltaY() * 0.1f;

        if (pitch >= 89.9) pitch = 89.9f;
        if (pitch <= -89.9) pitch = -89.9f;

        Quaternionf rotation = new Quaternionf().rotateYXZ((float) Math.toRadians(yaw), (float) Math.toRadians(pitch), 0.0f);

        if (this.controls.isForward()) {
            position.add(new Vector3f(0, 0, 1*this.multiplyer)); // Move forward in the local direction
        }
        if (this.controls.isBackward()) {
            position.add(new Vector3f(0, 0, -1*this.multiplyer)); // Move backward
        }
        if (this.controls.isUp()) {
            position.add(new Vector3f(0, -1*this.multiplyer, 0)); // Move up
        }
        if (this.controls.isDown()) {
            position.add(new Vector3f(0, 1*this.multiplyer, 0)); // Move down
        }
        if (this.controls.isRight()) {
            position.add(new Vector3f(-1*this.multiplyer, 0, 0)); // Move right
        }
        if (this.controls.isLeft()) {
            position.add(new Vector3f(1*this.multiplyer, 0, 0)); // Move left
        }

        DataMatrix.translation(position.mul(1f));
        DataMatrix.rotate(rotation);

        this.setLocalProjectiveMatrix(DataMatrix);
        return DataMatrix;
    }

    public Vector3 getPos() {
        return new Vector3( (int) position.x, (int) position.y, (int) position.z );
    }
}