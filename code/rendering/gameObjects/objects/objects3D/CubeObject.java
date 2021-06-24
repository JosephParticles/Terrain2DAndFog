package Terrain2DAndFog.code.rendering.gameObjects.objects.objects3D;

import Terrain2DAndFog.code.rendering.gameObjects.meshes.mesh3d.Mesh3D;
import org.joml.Vector3f;

public class CubeObject extends GameObject3D{

	public CubeObject(Mesh3D mesh) {
		super(mesh);
	}
	
	public CubeObject(Mesh3D mesh, Vector3f position) {
		this(mesh);
		this.position=new Vector3f(position);
	}
	
	public CubeObject(Mesh3D mesh, Vector3f position, Vector3f rotation) {
		this(mesh);
		this.position=new Vector3f(position);
		this.rotation=new Vector3f(rotation);
	}
	
	public CubeObject(Mesh3D mesh, Vector3f position, Vector3f rotation,Vector3f scale) {
		this(mesh);
		this.position=new Vector3f(position);
		this.rotation=new Vector3f(rotation);
		this.scale=scale;
	}

}
