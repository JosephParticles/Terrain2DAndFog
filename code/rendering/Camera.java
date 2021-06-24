package Terrain2DAndFog.code.rendering;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import Terrain2DAndFog.code.main.Window;

public class Camera {
	protected Vector3f position;
	protected Vector3f rotation;
	
	protected float FOV;
	
	protected Window window;
	
	protected float zNear=0.1f;
	protected float zFar=1000f;
	
	protected Matrix4f projectionMatrix;	//Matrix for projecting 3d space into 2d screenSpace
	
	public Camera(Window window,float FOV){
		this.position=new Vector3f();
		this.rotation=new Vector3f();
		this.FOV=FOV;
		this.window=window;
		
		calcProjMatrix();
	}
	
	public Camera(Window window, float FOV, Vector3f position) {
		this(window,FOV);
		this.position=new Vector3f(position);
	}
	
	public Camera(Window window, float FOV, Vector3f position, Vector3f rotation) {
		this(window,FOV);
		this.position=new Vector3f(position);
		this.rotation=new Vector3f(rotation);
	}
	
	public void translate(Vector3f translation){
		position.add(translation);
	}
	
	public void translate(float x, float y, float z){
		this.translate(new Vector3f(x,y,z));
	}
	
	public void translateDirectionFacing(Vector3f translation){
        if ( translation.z != 0 ) {
            position.x += (float)Math.sin(rotation.y) * -1.0f * translation.z;
            position.z += (float)Math.cos(rotation.y) * translation.z;
        }
        if ( translation.x != 0) {
            position.x += (float)Math.sin(rotation.y - Math.toRadians(90)) * -1.0f * translation.x;
            position.z += (float)Math.cos(rotation.y - Math.toRadians(90)) * translation.x;
        }
        position.y += translation.y;
    }
	
	public void translateDirectionFacing(float x, float y, float z){
		translateDirectionFacing(new Vector3f(x,y,z));
	}
	
	
	public void rotate(Vector3f rotation){
		this.rotation.add(rotation);
	}
	
	public void rotate(float x, float y, float z){
		this.rotate(new Vector3f(x,y,z));
	}
	
	public void calcProjMatrix(){
		projectionMatrix=new Matrix4f().setPerspective(FOV, window.getAspectRatio(), zNear, zFar);
	}
	
	public Matrix4f getProjMatrix(){
		return projectionMatrix;
	}

	public float getMaxViewDistance(){
		return zFar;
	}
	
	public Matrix4f getViewMatrix(){
		Matrix4f viewMatrix=new Matrix4f();
		
		viewMatrix.identity();
        
        // First do the rotation so camera rotates over its position
        viewMatrix.rotate(rotation.x, new Vector3f(1, 0, 0))
            .rotate(rotation.y, new Vector3f(0, 1, 0))
            .rotate(rotation.z, new Vector3f(0, 0, 1));
        
        // Then do the translation
        viewMatrix.translate(-position.x, -position.y, -position.z);
        return viewMatrix;
	}
	
	public void setFOV(float newFOV){
		this.FOV=newFOV;
		calcProjMatrix();
	}
	
	public float getFOV(){
		return FOV;
	}
	
	public void setPosition(Vector3f newPos){
		position=new Vector3f(newPos);
	}
	
	public void setPosition(float x, float y, float z){
		this.setPosition(new Vector3f(x,y,z));
	}
	
	public Vector3f getPosition(){
		return new Vector3f(position);
	}
	
	public void setRotation(Vector3f newRot){
		rotation=new Vector3f(newRot);
	}
	
	public void setRotation(float x, float y, float z){
		this.setRotation(new Vector3f(x,y,z));
	}
	
	public Vector3f getRotation(){
		return new Vector3f(rotation);
	}
	
}
