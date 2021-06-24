package Terrain2DAndFog.code.rendering.gameObjects.components.buffers;

import Terrain2DAndFog.code.rendering.gameObjects.components.Texture;
import org.lwjgl.opengl.GL46;

public class FrameBuffer {
    private int id;

    protected int width;
    protected int height;

    private final Texture[] textures;
    private final int[] attachments;
    private int textureCount;
    
    RenderBuffer depthStencilBuffer;

    public FrameBuffer(){
        textures=new Texture[32];
        attachments=new int[32];
        textureCount=0;
    }

    public void init(int width, int height, Texture[] textures, int[] attachments){
        if(textures.length>32){ //Error handling for the textures attached
            System.out.println("ERROR: Too many shaders attached");
            return;
        }else if(textures.length!=attachments.length){
            System.out.println("ERROR: textures and attachments arn't equal");
            return;
        }else if(textures.length==0){
            System.out.println("ERROR: You need to attach a texture to render to");
            return;
        }

        id= GL46.glGenFramebuffers();   //Generate frameBuffer pointer

        GL46.glBindFramebuffer(GL46.GL_FRAMEBUFFER,id); //Bind frame buffer

        for(int i=0;i<textures.length;i++){
            this.textures[i]=textures[i];
            this.attachments[i]=attachments[i];
            textureCount++;
            GL46.glFramebufferTexture2D(GL46.GL_FRAMEBUFFER,attachments[i], GL46.GL_TEXTURE_2D, textures[i].getId(),0);
        }

        depthStencilBuffer=new RenderBuffer(width,height);      //Generate depth-stencil renderbuffer and attach to the framebuffer
        GL46.glFramebufferRenderbuffer(GL46.GL_FRAMEBUFFER,GL46.GL_DEPTH_STENCIL_ATTACHMENT,GL46.GL_RENDERBUFFER,depthStencilBuffer.getId());

        GL46.glDrawBuffers(attachments);
        
        if(GL46.glCheckFramebufferStatus(GL46.GL_FRAMEBUFFER)!=GL46.GL_FRAMEBUFFER_COMPLETE){
            System.err.println("ERROR: frame buffer failed to complete");
        }

        GL46.glBindRenderbuffer(GL46.GL_RENDERBUFFER,0);    //Unbind buffers now it is generated
        GL46.glBindFramebuffer(GL46.GL_FRAMEBUFFER, 0);


    }

    public int getId(){
        return id;
    }

    public Texture getTexture(int id){
        if(textureCount>id){
            return textures[id];
        }
        return null;
    }

    public Texture[] getAllTextures(){
        return textures;
    }
    
    public int[] getAttachments(){
    	return attachments;
    }

    public void bindBuffer(){
        GL46.glBindFramebuffer(GL46.GL_FRAMEBUFFER,id);
        GL46.glBindRenderbuffer(GL46.GL_RENDERBUFFER,depthStencilBuffer.getId());
    }

    public void unbindBuffer(){
        GL46.glBindFramebuffer(GL46.GL_FRAMEBUFFER, 0);
        GL46.glBindRenderbuffer(GL46.GL_RENDERBUFFER,0);
    }

    public void cleanup(){
        for(int i=0;i<textureCount;i++){
            textures[i].cleanup();
            textures[i]=null;
        }
        GL46.glDeleteFramebuffers(id);
        depthStencilBuffer.cleanup();
    }

}
