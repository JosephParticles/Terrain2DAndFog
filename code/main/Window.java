package Terrain2DAndFog.code.main;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL46;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
    private long windowHandle;

    private int width;
    private int height;
    private float aspectRatio;
    
    private final String title;

    private boolean resized;


    public Window(int width, int height, String title){
        this.width=width;
        this.height=height;
        this.aspectRatio=(float)width/height;
        this.title=title;
        this.resized=false;
    }

    public void init(){
        GLFWErrorCallback.createPrint(System.err).set();

        if(!glfwInit()){
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE); // the window will be resizable

        System.out.println(windowHandle);



        windowHandle = glfwCreateWindow(width, height, title, NULL, NULL);

        if(windowHandle==NULL){
            throw new RuntimeException("Failed to create the GLFW window");
        }

        org.lwjgl.glfw.GLFW.glfwSetFramebufferSizeCallback(windowHandle, (window, width, height) -> {
            this.width = width;
            this.height = height;
            this.aspectRatio=(float)width/height;
            this.setResized(true);
        });

        // Make the OpenGL context current
        glfwMakeContextCurrent(windowHandle);
        // Enable v-sync
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(windowHandle);

        GL.createCapabilities();
        
        GL46.glEnable(GL46.GL_DEPTH_TEST);


        // Set the clear color
        glClearColor(0.1f, 0.1f, 0.1f, 0.0f);
    }

    public void loop(){

        glfwSwapBuffers(windowHandle);
        // swap the color buffers
        // Poll for window events. The key callback above will only be
        // invoked during this call.
        glfwPollEvents();

    }


    public void cleanup(){
        System.out.println("Cleaning up");
        // Free the window callbacks and destroy the window

        glfwFreeCallbacks(windowHandle);
        glfwDestroyWindow(windowHandle);

        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    public boolean shouldClose(){
        return glfwWindowShouldClose(windowHandle);
    }
    
    public void close(){
    	glfwSetWindowShouldClose(windowHandle, true); // We will detect this in the rendering loop
    }

    public long getWindowHandle(){
        return windowHandle;
    }

    public void setResized(boolean resized){
        this.resized=resized;
    }

    public boolean getResized(){
        return resized;
    }

    public int getWidth(){
        return width;
    }

    public int getHeight(){
        return height;
    }
    
    public float getAspectRatio(){
    	return aspectRatio;
    }


}
