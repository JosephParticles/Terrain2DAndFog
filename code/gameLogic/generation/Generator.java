package Terrain2DAndFog.code.gameLogic.generation;

import Terrain2DAndFog.code.Utils.PositionArrayList2D;
import Terrain2DAndFog.code.gameLogic.generation.perlin.Perlin;
import Terrain2DAndFog.code.gameLogic.player.Player;
import Terrain2DAndFog.code.rendering.gameObjects.objects.objects3D.GameObject3D;
import org.joml.Vector2i;

import java.util.ArrayList;

public class Generator {
	
	private int chunkSize;
	private Perlin perlin;
	
	private float amplitude=25;
	private float frequency=0.5f;
	private float persistance=0.5f;
	private int octaves=6;
	
	private boolean changeMade;
	
	private PositionArrayList2D<GameObject3D> chunks;

	public Generator(int chunkSize){
		this.chunkSize=chunkSize;
		this.perlin=new Perlin(amplitude,frequency,persistance,octaves,chunkSize);
		this.chunks=new PositionArrayList2D<>();
	}
	
	public void genStart(){
	}
	
	public void genLoop(Player player, float deltaUpdate){
		changeMade=false;
		Vector2i chunk=player.getChunk(chunkSize);

		int genDistance=3;
		for (int x=chunk.x-genDistance;x<=chunk.x+genDistance;x++){
			for(int y=chunk.y-genDistance;y<=chunk.y+genDistance;y++){
				if(chunks.getValue(x,y)==null){
					chunks.addValue(x,y, perlin.genChunk(x,y));
					changeMade=true;
				}
			}
		}
	}
	
	public boolean changeMade(){
		return changeMade;
	}
	
	public ArrayList<GameObject3D> getAllChunks(){
		return chunks.getRawArray();
	}
	
	public float getAmplitude(){
		return amplitude;
	}
}
