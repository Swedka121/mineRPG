package mine.Engine.Untils;

import org.lwjgl.opengl.GL46;

import java.util.ArrayList;

public class EBO {
    public int eboId;
    public EBO(ArrayList<Integer> indexesList) {
        this.eboId = GL46.glCreateBuffers();
        GL46.glBindBuffer(GL46.GL_ELEMENT_ARRAY_BUFFER, this.eboId);
        GL46.glBufferData(GL46.GL_ELEMENT_ARRAY_BUFFER, Convertor.convertI(indexesList), GL46.GL_STATIC_DRAW);
    }
    public void clear() {
        GL46.glDeleteBuffers(this.eboId);
    }
}
