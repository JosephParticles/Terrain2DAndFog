package Terrain2DAndFog.code.rendering.gameObjects.meshes.mesh2d;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.joml.Vector3f;
import org.lwjgl.opengl.GL46;
import org.lwjgl.system.MemoryUtil;

import Terrain2DAndFog.code.rendering.gameObjects.components.Texture;
import Terrain2DAndFog.code.rendering.gameObjects.components.buffers.FrameBuffer;
import Terrain2DAndFog.code.rendering.openGL.handlers.Program;

public class Screen {
    private int vaoId;

    private int vertexVboId;
    private int indexVboId;
    private int textureVboId;

    private int vertexCount;

    private FrameBuffer frameBuffer;

    private Texture tempTexture;

    public Screen( FrameBuffer frameBuffer){

        float[] vertices=new float[]{
                -1,1,
                1,1,
                1,-1,
                -1,-1
        };

        int[] indices=new int[]{
                0,1,2,
                0,2,3
        };

        float[] textureCoords=new float[]{
                0,1,
                1,1,
                1,0,
                0,0
        };

        vertexCount=indices.length;

        FloatBuffer verticesBuffer=null;
        IntBuffer indicesBuffer=null;
        FloatBuffer textureBuffer=null;

        this.frameBuffer=frameBuffer;

        try{
            // Create the VAO and bind to it

            vaoId = GL46.glGenVertexArrays();
            GL46.glBindVertexArray(vaoId);

            //Position VBO
            vertexVboId = GL46.glGenBuffers();
            verticesBuffer = MemoryUtil.memAllocFloat(vertices.length);
            verticesBuffer.put(vertices).flip();
            GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, vertexVboId);
            GL46.glBufferData(GL46.GL_ARRAY_BUFFER, verticesBuffer, GL46.GL_STATIC_DRAW);
            GL46.glEnableVertexAttribArray(0);
            GL46.glVertexAttribPointer(0, 2, GL46.GL_FLOAT, false, 0, 0);

            //Indices VBO
            indexVboId=GL46.glGenBuffers();
            indicesBuffer = MemoryUtil.memAllocInt(indices.length);
            indicesBuffer.put(indices).flip();
            GL46.glBindBuffer(GL46.GL_ELEMENT_ARRAY_BUFFER, indexVboId);
            GL46.glBufferData(GL46.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL46.GL_STATIC_DRAW);

            //Texture VBO
            textureVboId=GL46.glGenBuffers();
            textureBuffer=MemoryUtil.memAllocFloat(textureCoords.length);
            textureBuffer.put(textureCoords).flip();
            GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, textureVboId);
            GL46.glBufferData(GL46.GL_ARRAY_BUFFER, textureBuffer, GL46.GL_STATIC_DRAW);
            GL46.glEnableVertexAttribArray(1);
            GL46.glVertexAttribPointer(1, 2, GL46.GL_FLOAT, false, 0, 0);

            GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, 0);
            GL46.glBindVertexArray(0);
        }finally{
            if (verticesBuffer != null) {
                MemoryUtil.memFree(verticesBuffer);
            }if (indicesBuffer != null) {
                MemoryUtil.memFree(indicesBuffer);
            }if(textureBuffer!=null){
                MemoryUtil.memFree(textureBuffer);
            }
        }
    }

    public FrameBuffer getFrameBuffer(){
        return frameBuffer;
    }

    public void render(Program program, Vector3f bgColour, Texture texture){
        GL46.glActiveTexture(GL46.GL_TEXTURE0);
        GL46.glBindTexture(GL46.GL_TEXTURE_2D, frameBuffer.getTexture(0).getId());
//        GL46.glBindTexture(GL46.GL_TEXTURE_2D, texture.getId());

        GL46.glBindVertexArray(vaoId);

        GL46.glEnableVertexAttribArray(0);
        GL46.glEnableVertexAttribArray(1);

        program.setUniform("screenColourTexture", 0);
        program.setUniform("bgColour",bgColour);

        GL46.glDrawElements(GL46.GL_TRIANGLES, vertexCount, GL46.GL_UNSIGNED_INT, 0);

        GL46.glDisableVertexAttribArray(0);
        GL46.glDisableVertexAttribArray(1);

        // Restore state
        GL46.glBindVertexArray(0);
    }

    public void cleanup(){
        GL46.glDisableVertexAttribArray(0);

        GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, 0);
        GL46.glDeleteBuffers(vertexVboId);
        GL46.glDeleteBuffers(indexVboId);
        GL46.glDeleteBuffers(textureVboId);

        // Delete the VAO
        GL46.glBindVertexArray(0);
        GL46.glDeleteVertexArrays(vaoId);
    }
}
