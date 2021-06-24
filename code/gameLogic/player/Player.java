package Terrain2DAndFog.code.gameLogic.player;

import Terrain2DAndFog.code.main.Window;
import Terrain2DAndFog.code.rendering.Camera;
import org.joml.Vector2i;
import org.joml.Vector3f;

public class Player extends Camera{
	
	private Vector3f lastPosition;
	private Vector3f lastRotation;

	public Player(Window window, float FOV) {
		super(window, FOV);
		this.lastPosition=new Vector3f(0,0,0);
		this.lastRotation=new Vector3f(0,0,0);
	}
	
	@Override
	public void translate(Vector3f translation){
		lastPosition.set(position);
		position.add(translation);
	}
	
	@Override
	public void translateDirectionFacing(Vector3f translation){
		lastPosition.set(position);
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
	
	@Override
	public void rotate(Vector3f rotation){
		lastRotation.set(this.rotation);
		this.rotation.add(rotation);
	}

	public Vector2i getChunk(int chunkSize){
		return new Vector2i((int)Math.floor((position.x+chunkSize/2)/chunkSize),(int)Math.floor((position.z+chunkSize/2)/chunkSize));
	}
	
	public Vector3f getLastPosition(){
		return new Vector3f(lastPosition);
	}
	
	public Vector3f getLastRotation(){
		return new Vector3f(lastRotation);
	}
}
