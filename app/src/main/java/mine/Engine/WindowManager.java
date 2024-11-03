package mine.Engine;

import java.nio.IntBuffer;

import org.joml.Matrix4f;
import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MAJOR;
import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MINOR;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_CORE_PROFILE;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_FORWARD_COMPAT;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_PROFILE;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwGetWindowSize;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.glfw.GLFW.glfwSetWindowSizeCallback;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowSizeCallbackI;
import org.lwjgl.opengl.GL;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_LESS;
import static org.lwjgl.opengl.GL11.GL_RENDERER;
import static org.lwjgl.opengl.GL11.GL_VERSION;
import org.lwjgl.opengl.GL46;
import org.lwjgl.system.FunctionProvider;
import org.lwjgl.system.MemoryStack;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class WindowManager {
    private final String title;

    private int width = 0;
    private int height = 0;

    private long window;

    private boolean resize, vSync, fullScreen;

    public WindowManager(String title, int width, int height, boolean vSync, boolean fullScreen) {
        System.out.println("Init windowManager");
        this.title = title;
        this.width = width;
        this.height = height;
        this.vSync = vSync;
        this.fullScreen = fullScreen;
    }

    public void init() {
        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if (!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");

        // Configure GLFW
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 6);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
        // Create the window
        window = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);
        if (window == NULL)
            throw new RuntimeException("Failed to create the GLFW window");

        // Setup a key callback. It will be called every time a key is pressed, repeated
        // or released.
        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
                glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
        });

        // Get the thread stack and push a new frame
        try (MemoryStack stack = stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(window, pWidth, pHeight);

            // Get the resolution of the primary monitor
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            // Center the window
            glfwSetWindowPos(
                    window,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2);

        } // the stack frame is popped automatically

        // Enable v-sync
        if (isVSync())
            glfwSwapInterval(1);

        // Make the OpenGL context current
        glfwMakeContextCurrent(window);

        GL.createCapabilities();

        String version = GL46.glGetString(GL_VERSION);
        String renderer = GL46.glGetString(GL_RENDERER);

        System.out.println("Used OpenGL " + version + "\nRenderer " + renderer);

        GL46.glEnable(GL_DEPTH_TEST);
        GL46.glDepthFunc(GL_LESS);

        // Make the window visible
        glfwShowWindow(window);
    }

    public void current() {
        GLFW.glfwMakeContextCurrent(this.getWindow());
    }

    public void release() {
        GLFW.glfwMakeContextCurrent(0);
    }

    public void update() {
        GL46.glClearColor(0.5f, 0.5f, 0.5f, 0.0f);
        glfwSwapBuffers(this.window);
    }

    public void setResizeHandller(GLFWWindowSizeCallbackI handllerFunc) {
        glfwSetWindowSizeCallback(this.window, handllerFunc);
    }

    public void clear() {
        glfwDestroyWindow(window);
        glfwTerminate();
    }

    public String getTitle() {
        return this.title;
    }

    public int getWidth() {
        return this.width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return this.height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public long getWindow() {
        return this.window;
    }

    public void setWindow(long window) {
        this.window = window;
    }

    public boolean isResize() {
        return this.resize;
    }

    public boolean getResize() {
        return this.resize;
    }

    public void setResize(boolean resize) {
        this.resize = resize;
    }

    public boolean isVSync() {
        return this.vSync;
    }

    public boolean isFullscreen() {
        return this.fullScreen;
    }

    public void setVSync(boolean vSync) {
        this.vSync = vSync;
    }
}
