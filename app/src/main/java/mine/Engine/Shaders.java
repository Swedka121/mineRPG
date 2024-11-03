package mine.Engine;

import java.util.Scanner;
import java.io.IOException;
import java.io.File;
import java.net.URL;
import java.net.URISyntaxException;

import org.lwjgl.opengl.GL46;

public class Shaders {
    public int load() {
        String vert_shader = this.loadAsString("Shaders/vert_shader.glsl");
        String frag_shader = this.loadAsString("Shaders/frag_shader.glsl");

        int programm = GL46.glCreateProgram();

        int vert_shader_gl = GL46.glCreateShader(GL46.GL_VERTEX_SHADER);
        int frag_shader_gl = GL46.glCreateShader(GL46.GL_FRAGMENT_SHADER);

        GL46.glShaderSource(vert_shader_gl, vert_shader);
        GL46.glShaderSource(frag_shader_gl, frag_shader);

        GL46.glCompileShader(vert_shader_gl);
        if (GL46.glGetShaderi(vert_shader_gl, GL46.GL_COMPILE_STATUS) == GL46.GL_FALSE) {
            System.err.println("ERORR: Compile status of vert_shader is false!");
        }

        GL46.glCompileShader(frag_shader_gl);
        if (GL46.glGetShaderi(frag_shader_gl, GL46.GL_COMPILE_STATUS) == GL46.GL_FALSE) {
            System.err.println("ERORR: Compile status of frag_shader is false!");
        }

        GL46.glAttachShader(programm, vert_shader_gl);
        GL46.glAttachShader(programm, frag_shader_gl);

        GL46.glLinkProgram(programm);
        GL46.glValidateProgram(programm);

        this.checkProgramLink(programm);

        GL46.glDeleteShader(frag_shader_gl);
        GL46.glDeleteShader(vert_shader_gl);

        return programm;
    }

    public String loadAsString(String path) {
        URL resourceUrl = Shaders.class.getClassLoader().getResource(path);
        StringBuffer result = new StringBuffer();
        if (resourceUrl != null) {
            try {
                File file = new File(resourceUrl.toURI());
                Scanner myReader = new Scanner(file);
                while (myReader.hasNextLine()) {
                    String data = myReader.nextLine();
                    System.out.println(data);
                    result.append(data + "\n");
                }
                myReader.close();
            } catch (URISyntaxException e) {
                System.err.println("ERROR: " + e.toString());
            } catch (IOException e) {
                System.out.println("ERROR: " + e.toString());
            }
        }
        return result.toString();
    }

    private void checkProgramLink(int program) {
        if (GL46.glGetProgrami(program, GL46.GL_LINK_STATUS) == GL46.GL_FALSE) {
            throw new RuntimeException("Program link error: " + GL46.glGetProgramInfoLog(program));
        }
    }
}
