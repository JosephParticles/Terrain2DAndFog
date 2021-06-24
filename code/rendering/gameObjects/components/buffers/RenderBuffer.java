package Terrain2DAndFog.code.rendering.gameObjects.components.buffers;

import org.lwjgl.opengl.GL46;

public class RenderBuffer {
    protected int id;

    protected int width;
    protected int height;

    public RenderBuffer(int width, int height){
        this.width=width;
        this.height=height;

        id= GL46.glGenRenderbuffers();

        GL46.glBindRenderbuffer(GL46.GL_RENDERBUFFER,id);

        GL46.glRenderbufferStorage(GL46.GL_RENDERBUFFER,GL46.GL_DEPTH24_STENCIL8,width,height);
    }

    public int getId(){
        return id;
    }

    public void cleanup(){
        GL46.glDeleteRenderbuffers(id);
    }
}
