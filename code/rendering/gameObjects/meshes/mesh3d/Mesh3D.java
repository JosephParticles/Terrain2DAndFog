package Terrain2DAndFog.code.rendering.gameObjects.meshes.mesh3d;

import Terrain2DAndFog.code.rendering.Camera;
import Terrain2DAndFog.code.rendering.openGL.handlers.Program;

public class Mesh3D {
	protected int vaoId;
	
	protected int vertexVboId;
	protected int indexVboId;
	
	protected int vertexCount;
	
	public Mesh3D(int[] indices) {
		vertexCount=indices.length;
	}
	
	public void render(Program program,Camera camera, float currentTime) {
	}
	
	public void cleanup() {}
	
    public int getVaoId(){
        return vaoId;
    }

    public int getVertexCount(){
        return vertexCount;
    }
}
