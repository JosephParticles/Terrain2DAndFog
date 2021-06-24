package Terrain2DAndFog.code.IO;

import org.lwjgl.glfw.GLFW;

import Terrain2DAndFog.code.main.Window;

public class Input {
    private boolean[] keys=new boolean[GLFW.GLFW_KEY_LAST];
    private boolean[] mouseButtons=new boolean[GLFW.GLFW_MOUSE_BUTTON_LAST];
    
    private float mouseX;
    private float mouseY;

    public Input(){

    }
    
    public void init(Window windowObject){
    	
    	GLFW.glfwSetKeyCallback(windowObject.getWindowHandle(), (window, key, scancode, action, mods) -> {
            keys[key]=(action == GLFW.GLFW_PRESS||action==GLFW.GLFW_REPEAT);
        });
        GLFW.glfwSetMouseButtonCallback(windowObject.getWindowHandle(), (window, button, action, mods) -> {
            mouseButtons[button]=(action== GLFW.GLFW_PRESS||action==GLFW.GLFW_REPEAT);
        });
        GLFW.glfwSetCursorPosCallback(windowObject.getWindowHandle(), (window,xPos,yPos) -> {
            mouseX=(float)xPos;
            mouseY=(float)yPos;
        });
    }

    public boolean isKeyDown(int key){
        return keys[key];
    }	
    
    public boolean isMouseButtonDown(int mouseButton){
        return mouseButtons[mouseButton];
    }
    
    public float[] getMousePos(){
        return new float[]{mouseX,mouseY};
    }
}

