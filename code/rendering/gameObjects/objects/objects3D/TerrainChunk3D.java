package Terrain2DAndFog.code.rendering.gameObjects.objects.objects3D;

import Terrain2DAndFog.code.rendering.Camera;
import Terrain2DAndFog.code.rendering.gameObjects.meshes.mesh3d.Mesh3D;
import Terrain2DAndFog.code.rendering.openGL.handlers.Program;

public class TerrainChunk3D extends GameObject3D{
	
	private int chunkX;
	private int chunkY;
	private int chunkSize;
	
	public TerrainChunk3D(Mesh3D mesh, int chunkX, int chunkY, int chunkSize) {
		super(mesh);
		this.chunkX=chunkX;
		this.chunkY=chunkY;
		this.chunkSize=chunkSize;
	}
	
	@Override
	public void render(Program program, Camera camera, float currentTime){
		if(mesh!=null){

			mesh.render(program, camera, currentTime);
		}else{
			System.out.println("No mesh declared");
		}
	}

}
