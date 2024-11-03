package mine.Engine.Untils;

import org.lwjgl.opengl.GL46;

public class VAO {
    public int vaoId;
    public VAO() {
        this.vaoId = GL46.glGenVertexArrays();
        GL46.glBindVertexArray(this.vaoId);
    }
    public void bind() {
        GL46.glBindVertexArray(this.vaoId);
    }
    public void clear() {
        GL46.glDeleteVertexArrays(this.vaoId);
    }
}
