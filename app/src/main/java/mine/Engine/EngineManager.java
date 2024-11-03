package mine.Engine;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;

import mine.Engine.Cameras.FlyCamera;
import mine.Engine.Cameras.OrbitCamera;
import mine.Engine.Untils.Mesh;
import mine.Utils.Vector3;
import mine.Voxels.World;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import org.lwjgl.opengl.GL46;

import mine.App;
import mine.Engine.Untils.Texture;

public class EngineManager {
    private boolean isRunning;
    private WindowManager wnm;
    private int shaderProgrammId;
    private World world;

    private HashMap<UUID, Mesh> meshes = new HashMap<>();

    private Matrix4f modelMatrix;
    private Matrix4f viewMatrix;
    private Matrix4f projectionMatrix;

    private FlyCamera camera;

    private int timer = 0;

    public final Queue<Runnable> taskQueue = new ConcurrentLinkedQueue<>();

    public void init() {
        this.wnm = App.getWindow();
        Shaders shaders = new Shaders();
        this.shaderProgrammId = shaders.load();

        this.modelMatrix = new Matrix4f();
        this.viewMatrix = new Matrix4f();
        this.projectionMatrix = new Matrix4f();

        this.viewMatrix = this.viewMatrix.lookAt(new Vector3f(0.0f, 0.0f, 20.0f), new Vector3f(0.0f, 0.0f, 0.0f),
                new Vector3f(0.0f, 1.0f, 0.0f));
        this.projectionMatrix = this.projectionMatrix.perspective((float) Math.toRadians(45.0f),
                (float) wnm.getWidth() / wnm.getHeight(), 0.1f, 100.0f);
    }

    public void run() {
        this.isRunning = true;

        this.camera = new FlyCamera(this.viewMatrix, 0.1f);

        Texture blocksTextures = new Texture("/images/blocks.png");

        GL46.glUseProgram(this.shaderProgrammId);
        int texUni = GL46.glGetUniformLocation(this.shaderProgrammId, "tex0");
        GL46.glUniform1i(texUni, 0);

        int mat4ViewUni = GL46.glGetUniformLocation(this.shaderProgrammId, "view");
        int mat4ModelUni = GL46.glGetUniformLocation(this.shaderProgrammId, "model");
        int mat4ProjUni = GL46.glGetUniformLocation(this.shaderProgrammId, "proj");
        int scaleUni = GL46.glGetUniformLocation(this.shaderProgrammId, "scale");

        this.world = new World();
        world.getQue(0,0,0,24);
        world.executeQue();

        Vector3 pos = camera.getPos();
        Vector3 lastPos = pos;

        while (this.isRunning) {
            timer++;

            pos = camera.getPos();

            if (glfwWindowShouldClose(this.wnm.getWindow())) {
                this.stop();
            }

            GL46.glClear(GL46.GL_COLOR_BUFFER_BIT | GL46.GL_DEPTH_BUFFER_BIT);

            GL46.glUseProgram(this.shaderProgrammId);

            this.viewMatrix = this.camera.update();

            FloatBuffer viewMatBuffer = BufferUtils.createFloatBuffer(16);
            GL46.glUniformMatrix4fv(mat4ViewUni, false, this.viewMatrix.get(viewMatBuffer));
            FloatBuffer modelMatBuffer = BufferUtils.createFloatBuffer(16);
            GL46.glUniformMatrix4fv(mat4ModelUni, false, this.modelMatrix.get(modelMatBuffer));
            FloatBuffer projMatBuffer = BufferUtils.createFloatBuffer(16);
            GL46.glUniformMatrix4fv(mat4ProjUni, false, this.projectionMatrix.get(projMatBuffer));
            GL46.glUniform1f(scaleUni, 2f);

            if (timer == 20) {
                this.world.getQue((int) camera.getPos().getX(), (int) camera.getPos().getY(), (int) camera.getPos().getZ(), 32);
                this.world.executeQue();
                timer = 0;
            }

            meshes.values().stream().forEach((Mesh mesh) -> {
                mesh.updateArrays();
                mesh.render(blocksTextures);
                mesh.clean();
            });

            wnm.update();

            glfwPollEvents();

            int error = GL46.glGetError();
            if (error != GL46.GL_NO_ERROR) {
                System.out.println("OpenGL Error: " + error);
            }

            lastPos = pos;
        }

        GL46.glDeleteProgram(this.shaderProgrammId);
        blocksTextures.clear();
    }

    private void stop() {
        if (!this.isRunning) {
            return;
        }
        this.isRunning = false;
    }

    public WindowManager getWnm() {
        return wnm;
    }

    public World getWorld() {
        return world;
    }

    public void regMesh(Mesh mesh) {
        meshes.put(mesh.getId(), mesh);
    }

    public void unRegMesh(Mesh mesh) {
        meshes.remove(mesh.getId());
    }
}
