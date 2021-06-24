package Terrain2DAndFog.code.rendering.gameObjects.objects.objects3D;

import Terrain2DAndFog.code.rendering.Camera;
import Terrain2DAndFog.code.rendering.gameObjects.meshes.mesh3d.Mesh3D;
import Terrain2DAndFog.code.rendering.openGL.handlers.Program;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class GameObject3D {
	protected Vector3f position;
	protected Vector3f rotation;
	protected Vector3f scale;
	
	protected Mesh3D mesh;
	
	public GameObject3D(Mesh3D mesh) {
		this.mesh=mesh;
		
		this.position=new Vector3f(0,0,0);
		this.rotation=new Vector3f(0,0,0);
		this.scale=new Vector3f(1,1,1);
	}
	
	public GameObject3D(Mesh3D mesh, Vector3f position) {
		this(mesh);
		this.position=new Vector3f(position);
	}
	public GameObject3D(Mesh3D mesh, Vector3f position, Vector3f rotation) {
		this(mesh);
		this.position=new Vector3f(position);
		this.rotation=new Vector3f(rotation);
	}
	public GameObject3D(Mesh3D mesh, Vector3f position, Vector3f rotation,Vector3f scale) {
		this(mesh);
		this.position=new Vector3f(position);
		this.rotation=new Vector3f(rotation);
		this.scale=scale;
	}
	
	public void translate(Vector3f translation){
		position.add(translation);
	}
	
	public void translate(float x, float y, float z){
		translate(new Vector3f(x,y,z));
	}
	
	public void rotate(Vector3f rotation){
		this.rotation.add(rotation);
	}
	
	public void rotate(float x, float y, float z){
		rotate(new Vector3f(x,y,z));
	}
	
	public void multScale(Vector3f scale){
		this.scale.mul(scale);
	}
	
	public void multScale(float x, float y, float z){
		multScale(new Vector3f(x,y,z));
	}
	
	public void addScale(Vector3f scale){
		this.scale.add(scale);
	}
	
	public void addScale(float x, float y, float z){
		addScale(new Vector3f(x,y,z));
	}
	
	public Matrix4f getTransformMatrix(){
		Matrix4f mat=new Matrix4f().identity();
		
		mat.translate(position);
		
		mat.scale(scale);

		
		mat.rotate(rotation.x, new Vector3f(1, 0, 0))
        .rotate(rotation.y, new Vector3f(0, 1, 0))
        .rotate(rotation.z, new Vector3f(0, 0, 1));

		return mat;
	}
	
	public void setPosition(Vector3f position) {
		this.position=new Vector3f(position);
	}
	
	public void setPosition(float x, float y, float z) {
		setPosition(new Vector3f(x,y,z));
	}
	
	public Vector3f getPosition() {
		return new Vector3f(position);
	}
	
	public void setRotation(Vector3f rotation) {
		this.rotation=new Vector3f(position);
	}
	
	public void setRotation(float x, float y, float z) {
		setRotation(new Vector3f(x,y,z));
	}
	
	public Vector3f getRotation() {
		return new Vector3f(rotation);
	}
	
	public void setScale(Vector3f scale) {
		this.position=new Vector3f(position);
	}
	
	public void setScale(float x, float y, float z) {
		setScale(new Vector3f(x,y,z));
	}
	
	public Vector3f getScale() {
		return new Vector3f(scale);
	}
	
	public void render(Program program, Camera camera, float currentTime){
		if(mesh!=null){
			program.setUniform("transformationMatrix", getTransformMatrix());
			mesh.render(program, camera, currentTime);
		}else{
			System.out.println("No mesh declared");
		}
	}
	
	public void cleanup(){
		if(mesh!=null){
			mesh.cleanup();
		}
	}
}
