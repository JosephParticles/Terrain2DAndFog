package Terrain2DAndFog.code.rendering.gameObjects.components.buffers;

import Terrain2DAndFog.code.rendering.gameObjects.components.Texture;
import org.lwjgl.opengl.GL46;

public class ScreenBuffer {
    protected int id;

    protected Texture colourTexture;
    protected Texture depthTexture;
    protected RenderBuffer depthStencilBuffer;

    public ScreenBuffer(){

    }

    public void init(int width, int height){
        id=GL46.glGenFramebuffers();

        GL46.glBindFramebuffer(GL46.GL_FRAMEBUFFER,id);

        colourTexture=new Texture(width,height,GL46.GL_RGB,GL46.GL_RGB,GL46.GL_UNSIGNED_BYTE);
        depthTexture=new Texture(width,height,GL46.GL_DEPTH_COMPONENT16,GL46.GL_DEPTH_COMPONENT,GL46.GL_FLOAT);

        GL46.glFramebufferTexture2D(GL46.GL_FRAMEBUFFER,GL46.GL_COLOR_ATTACHMENT0, GL46.GL_TEXTURE_2D, colourTexture.getId(),0);
        GL46.glFramebufferTexture2D(GL46.GL_FRAMEBUFFER,GL46.GL_DEPTH_ATTACHMENT, GL46.GL_TEXTURE_2D, depthTexture.getId(),0);

        depthStencilBuffer=new RenderBuffer(width,height);

        GL46.glFramebufferRenderbuffer(GL46.GL_FRAMEBUFFER,GL46.GL_DEPTH_STENCIL_ATTACHMENT,GL46.GL_RENDERBUFFER,depthStencilBuffer.getId());

        GL46.glBindRenderbuffer(GL46.GL_RENDERBUFFER,0);

        if(GL46.glCheckFramebufferStatus(GL46.GL_FRAMEBUFFER)!=GL46.GL_FRAMEBUFFER_COMPLETE){
            System.out.println("ERROR: frame buffer failed to complete");
        }
        GL46.glBindFramebuffer(GL46.GL_FRAMEBUFFER, 0);
    }

    public Texture getColourTexture(){
        return colourTexture;
    }

    public Texture getDepthTexture(){
        return depthTexture;
    }

    public void bindBuffer(){
        GL46.glBindFramebuffer(GL46.GL_FRAMEBUFFER,id);
        GL46.glBindRenderbuffer(GL46.GL_RENDERBUFFER,depthStencilBuffer.getId());
    }

    public void unbindBuffer(){
        GL46.glBindFramebuffer(GL46.GL_FRAMEBUFFER, 0);
        GL46.glBindRenderbuffer(GL46.GL_RENDERBUFFER,0);

    }

    public void enable(){
        bindBuffer();
        GL46.glClear(GL46.GL_COLOR_BUFFER_BIT | GL46.GL_DEPTH_BUFFER_BIT);

    }

    public void cleanup(){
        colourTexture.cleanup();
        depthTexture.cleanup();
        GL46.glDeleteFramebuffers(id);
        depthStencilBuffer.cleanup();
    }
}
