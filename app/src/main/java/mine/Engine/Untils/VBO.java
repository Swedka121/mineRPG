package mine.Engine.Untils;

import org.lwjgl.opengl.GL46;

import java.util.ArrayList;

public class VBO {
    public int vboId;
    public VBO(ArrayList<Float> points) {
        this.vboId = GL46.glGenBuffers();
        GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, this.vboId);
        GL46.glBufferData(GL46.GL_ARRAY_BUFFER, Convertor.convertF(points), GL46.GL_STATIC_DRAW);
        GL46.glVertexAttribPointer(0, 3, GL46.GL_FLOAT, false, 6 * Float.BYTES, 0);
        GL46.glEnableVertexAttribArray(0);
        GL46.glVertexAttribPointer(1, 2, GL46.GL_FLOAT, false, 6 * Float.BYTES, 3 * Float.BYTES);
        GL46.glEnableVertexAttribArray(1);
        GL46.glVertexAttribPointer(2, 1, GL46.GL_FLOAT, false, 6 * Float.BYTES, 5 * Float.BYTES);
        GL46.glEnableVertexAttribArray(2);
    }
    public void clear() {

        GL46.glDeleteBuffers(this.vboId);
    }
    public void bind() {
        GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, this.vboId);
    }
}
