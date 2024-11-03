package mine.Engine.Cameras;

import org.joml.Matrix4f;

public class Camera {
    private Matrix4f localProjectiveMatrix;

    Camera(Matrix4f projectiveMatrix) {
        this.localProjectiveMatrix = projectiveMatrix;
    }

    public Matrix4f getLocalProjectiveMatrix() {
        return localProjectiveMatrix;
    }

    public void setLocalProjectiveMatrix(Matrix4f localProjectiveMatrix) {
        this.localProjectiveMatrix = localProjectiveMatrix;
    }
}
