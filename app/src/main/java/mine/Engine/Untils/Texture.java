package mine.Engine.Untils;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.opengl.GL46;
import static org.lwjgl.stb.STBImage.stbi_failure_reason;
import static org.lwjgl.stb.STBImage.stbi_image_free;
import static org.lwjgl.stb.STBImage.stbi_load;
import static org.lwjgl.stb.STBImage.stbi_set_flip_vertically_on_load;
import org.lwjgl.system.MemoryStack;
import static org.lwjgl.system.MemoryStack.stackPush;

public class Texture {
    int textureId;
    private int load(String path) {
        stbi_set_flip_vertically_on_load(true);
        String ap = getClass().getResource(path).getPath().replaceFirst("/", "");

        try (MemoryStack stack = stackPush()) {
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            IntBuffer channels = stack.mallocInt(1);

            ByteBuffer image = stbi_load(ap, w, h, channels, 4); 
            
            if (image == null) {
                System.out.println("Load Fail: " + stbi_failure_reason());
                System.out.println("Failed");
                return 0;
            }
            int width = w.get();
            int height = h.get();
            int numChannels = channels.get();

            int texture = GL46.glGenTextures();
            GL46.glActiveTexture(GL46.GL_TEXTURE0);
            GL46.glBindTexture(GL46.GL_TEXTURE_2D, texture);

            GL46.glTexParameteri(GL46.GL_TEXTURE_2D, GL46.GL_TEXTURE_MIN_FILTER, GL46.GL_NEAREST);
            GL46.glTexParameteri(GL46.GL_TEXTURE_2D, GL46.GL_TEXTURE_MAG_FILTER, GL46.GL_NEAREST);

            GL46.glTexParameteri(GL46.GL_TEXTURE_2D, GL46.GL_TEXTURE_WRAP_S, GL46.GL_REPEAT);
            GL46.glTexParameteri(GL46.GL_TEXTURE_2D, GL46.GL_TEXTURE_WRAP_T, GL46.GL_REPEAT);

            GL46.glTexImage2D(GL46.GL_TEXTURE_2D, 0, GL46.GL_RGBA, width, height, 0, GL46.GL_RGBA, GL46.GL_UNSIGNED_BYTE, image);
            GL46.glGenerateMipmap(GL46.GL_TEXTURE_2D);
        
            stbi_image_free(image);

            return texture;  
        }
    }

    public Texture(String path) {
        this.textureId = this.load(path);
    }

    public int getTexture() {
        return this.textureId;
    }

    public void bind() {
        GL46.glBindTexture(GL46.GL_TEXTURE_2D, this.getTexture());
    }

    public void clear() {
        GL46.glDeleteTextures(this.textureId);
    }
}
