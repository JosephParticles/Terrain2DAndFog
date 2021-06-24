package Terrain2DAndFog.code.rendering.gameObjects.components;

import org.lwjgl.opengl.GL46;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class Texture {
	
	private final int id;
	
	private int width;
	private int height;
	
	public Texture(String fileName) throws Exception{
		int[] array=loadTexture(fileName);
		this.id=array[0];
		width=array[1];
		height=array[2];
	}

	public Texture(int width, int height,int internalFormat, int pixelFormat,int dataType){
	    this.id=GL46.glGenTextures();
	    this.width=width;
	    this.height=height;

	    GL46.glBindTexture(GL46.GL_TEXTURE_2D,this.id);
        GL46.glTexImage2D(GL46.GL_TEXTURE_2D, 0, internalFormat, this.width, this.height, 0, pixelFormat,dataType, (ByteBuffer) null);
        GL46.glTexParameteri(GL46.GL_TEXTURE_2D, GL46.GL_TEXTURE_MIN_FILTER, GL46.GL_NEAREST);
        GL46.glTexParameteri(GL46.GL_TEXTURE_2D, GL46.GL_TEXTURE_MAG_FILTER, GL46.GL_NEAREST);
        GL46.glTexParameteri(GL46.GL_TEXTURE_2D, GL46.GL_TEXTURE_WRAP_S, GL46.GL_CLAMP_TO_EDGE);
        GL46.glTexParameteri(GL46.GL_TEXTURE_2D, GL46.GL_TEXTURE_WRAP_T, GL46.GL_CLAMP_TO_EDGE);
    }
	
    public Texture(int id) {
        this.id = id;
    }
    
    public void bind() {
    	GL46.glBindTexture(GL46.GL_TEXTURE_2D, id);
    }

    public int getId() {
        return id;
    }
    
    public int getWidth() {
    	return width;
    }
    
    public int getHeight() {
    	return height;
    }
    
    private static int[] loadTexture(String fileName) throws Exception {

        int width;
        int height;
        ByteBuffer buf;
        // Load Texture file
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            IntBuffer channels = stack.mallocInt(1);

            buf = STBImage.stbi_load(fileName, w, h, channels, 4);
            if (buf == null) {
                throw new Exception("Image file [" + fileName  + "] not loaded: " + STBImage.stbi_failure_reason());
            }

            width = w.get();
            height = h.get();
        }

        // Create a new OpenGL texture
        int textureId = GL46.glGenTextures();
        // Bind the texture
        GL46.glBindTexture(GL46.GL_TEXTURE_2D, textureId);

        // Tell OpenGL how to unpack the RGBA bytes. Each component is 1 byte size
        GL46.glPixelStorei(GL46.GL_UNPACK_ALIGNMENT, 1);

        // Upload the texture data
        GL46.glTexImage2D(GL46.GL_TEXTURE_2D, 0, GL46.GL_RGBA, width, height, 0,
        		GL46.GL_RGBA, GL46.GL_UNSIGNED_BYTE, buf);
        // Generate Mip Map
        GL46.glGenerateMipmap(GL46.GL_TEXTURE_2D);

        STBImage.stbi_image_free(buf);

        return new int[] {textureId,width,height};
    }
    
    public void cleanup() {
    	GL46.glDeleteTextures(id);
    }
	
}
