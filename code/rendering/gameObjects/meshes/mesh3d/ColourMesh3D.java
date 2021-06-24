package Terrain2DAndFog.code.rendering.gameObjects.meshes.mesh3d;

import Terrain2DAndFog.code.rendering.Camera;
import Terrain2DAndFog.code.rendering.openGL.handlers.Program;
import org.lwjgl.opengl.GL46;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class ColourMesh3D extends Mesh3D{

    private int colourVboId;

    public ColourMesh3D(float[] vertices, int[] indices, float[] colours){
    	super(indices);
        FloatBuffer verticesBuffer=null;
        IntBuffer indicesBuffer=null;
        FloatBuffer colourBuffer=null;


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
            GL46.glVertexAttribPointer(0, 3, GL46.GL_FLOAT, false, 0, 0);

            indexVboId=GL46.glGenBuffers();
            indicesBuffer = MemoryUtil.memAllocInt(indices.length);
            indicesBuffer.put(indices).flip();
            GL46.glBindBuffer(GL46.GL_ELEMENT_ARRAY_BUFFER, indexVboId);
            GL46.glBufferData(GL46.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL46.GL_STATIC_DRAW);

            //Colour VBO
            colourVboId = GL46.glGenBuffers();
            colourBuffer = MemoryUtil.memAllocFloat(colours.length);
            colourBuffer.put(colours).flip();
            GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, colourVboId);
            GL46.glBufferData(GL46.GL_ARRAY_BUFFER, colourBuffer, GL46.GL_STATIC_DRAW);
            GL46.glEnableVertexAttribArray(1);
            GL46.glVertexAttribPointer(1, 3, GL46.GL_FLOAT, false, 0, 0);


            GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, 0);
            GL46.glBindVertexArray(0);
        }finally{
            if (verticesBuffer != null) {
                MemoryUtil.memFree(verticesBuffer);
            }if(indicesBuffer!=null){
                MemoryUtil.memFree(indicesBuffer);
            }if(colourBuffer!=null){
                MemoryUtil.memFree(colourBuffer);
            }
        }
    }

    @Override
    public void render(Program program,Camera camera, float currentTime){
        GL46.glBindVertexArray(getVaoId());

        GL46.glEnableVertexAttribArray(0);
        GL46.glEnableVertexAttribArray(1);
        
        program.setUniform("projectionMatrix", camera.getProjMatrix());
        program.setUniform("worldMatrix",camera.getViewMatrix());

        // Draw the vertices
    	GL46.glDrawElements(GL46.GL_TRIANGLES, getVertexCount(), GL46.GL_UNSIGNED_INT, 0);

        GL46.glDisableVertexAttribArray(0);
        GL46.glDisableVertexAttribArray(1);
        GL46.glBindVertexArray(0);
    }

    @Override
    public void cleanup(){
        GL46.glDisableVertexAttribArray(0);

        // Delete the VBO
        GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, 0);

        GL46.glDeleteBuffers(vertexVboId);
        GL46.glDeleteBuffers(indexVboId);
        GL46.glDeleteBuffers(colourVboId);

        // Delete the VAO
        GL46.glBindVertexArray(0);
        GL46.glDeleteVertexArrays(vaoId);
    }

}
