package Terrain2DAndFog.code.gameLogic.generation.perlin;

import Terrain2DAndFog.code.rendering.gameObjects.meshes.mesh3d.TerrainMesh;
import Terrain2DAndFog.code.rendering.gameObjects.objects.objects3D.TerrainChunk3D;
import org.joml.Vector2f;

import java.util.concurrent.ThreadLocalRandom;

public class Perlin {
	
	private float amplitude;
	private float frequency;
	private float persistance;
	private int octaves;
	private int chunkSize;
	
	private PointValues pointValues;
	
	public Perlin(float amplitude, float frequency,float persistance, int octaves,int chunkSize){
		this.amplitude=amplitude;
		this.frequency=frequency;
		this.octaves=octaves;
		this.persistance=persistance;
		this.chunkSize=chunkSize;
		this.pointValues=new PointValues();
	}
	
	public TerrainChunk3D genChunk(int chunkX, int chunkY){
		//Generates the heights for the points of the chunk
		
		int vertexCount=(chunkSize+1)*(chunkSize+1);
		int squareCount=chunkSize*chunkSize;
		
		float[] vertices=new float[vertexCount*3];
		int[] indices=new int[squareCount*6];
		float[] colours=new float[vertexCount*3];
		
		ThreadLocalRandom rand=ThreadLocalRandom.current();
		
		int index=0;
		
		
		for(int y=0;y<=chunkSize;y++) {//Generate the vertices
			for(int x=0;x<=chunkSize;x++) {
				int i=x+y*(chunkSize+1);
				
				vertices[index]=(float)(x-chunkSize/2);
				vertices[index+1]=PerlinNoise(chunkX+(float)x/chunkSize, chunkY+(float)y/chunkSize,(1/(chunkSize*2)));;
				vertices[index+2]=(float)(y-chunkSize/2);
				
				colours[index]=rand.nextFloat();
				colours[index+1]=rand.nextFloat();
				colours[index+2]=rand.nextFloat();
				
				index+=3;
			}
		}
		
		index=0;
		
		for(int y=0;y<chunkSize;y++){
			for(int x=0;x<chunkSize;x++){
				int bLIndex=x+y*(chunkSize+1);
				indices[index]=bLIndex;		//BL->TR->BR
				indices[index+1]=bLIndex+(chunkSize+1)+1;
				indices[index+2]=bLIndex+1;
				
				indices[index+3]=bLIndex;		//BL->TL->TR
				indices[index+4]=bLIndex+(chunkSize+1);
				indices[index+5]=bLIndex+(chunkSize+1)+1;
				
				index+=6;
			}
		}
		
		TerrainMesh mesh=new TerrainMesh(vertices,indices,colours);

		TerrainChunk3D chunk=new TerrainChunk3D(mesh,chunkX,chunkY,chunkSize);

		chunk.setPosition(chunkX*chunkSize,0,chunkY*chunkSize);

		//qSystem.out.println(perlin.getUnitVectors());
		
		return chunk;
	}
	
	public float PerlinNoise(float x, float y,float offset){
		float total=0;
		float maxValue=0;
		float localAmplitude=1;
		float frequency=this.frequency;
		for(int i=0;i<octaves;i++){
			float sampleNum=sample(x*frequency,y*frequency,offset);
			total+=(sampleNum*localAmplitude);
			maxValue+=localAmplitude;

			localAmplitude*=persistance;
			frequency*=2;
		}

		
		return (total/maxValue)*amplitude;
	}
	
	private float smoothStep(float t){
		return t * t * t * (t * (t * 6 - 15) + 10);
	}
	
	private float lerp(float x, float y, float f){
		return x+(smoothStep(f)*(y-x));
	}

	private Vector2f normalise(Vector2f vec){
		if(vec.x==0&&vec.y==0){
			return new Vector2f(0,0);
		}else{
			return new Vector2f(vec).normalize();
		}
	}
	
	private float sample(float x, float y, float offset){
		int xFloor=(int)Math.floor(x);
		int xCeil=(int)Math.floor(x)+1;
		int yFloor=(int)Math.floor(y);
		int yCeil=(int)Math.floor(y)+1;
		
		Vector2f BLCorner=pointValues.get(xFloor, yFloor);
		Vector2f TLCorner=pointValues.get(xFloor, yCeil);
		Vector2f TRCorner=pointValues.get(xCeil, yCeil);
		Vector2f BRCorner=pointValues.get(xCeil, yFloor);
		
		float BLValue=BLCorner.dot(normalise(new Vector2f(x-xFloor+offset,y-yFloor+offset)));
		float TLValue=TLCorner.dot(normalise(new Vector2f(x-xFloor+offset,y-yCeil+offset)));
		float TRValue=TRCorner.dot(normalise(new Vector2f(x-xCeil+offset,y-yCeil+offset)));
		float BRValue=BRCorner.dot(normalise(new Vector2f(x-xCeil+offset,y-yFloor+offset)));
		
		float TopValue=lerp(TLValue,TRValue,x-xFloor);
		float BottomValue=lerp(BLValue,BRValue,x-xFloor);
		
		float value=lerp(TopValue,BottomValue,y-yFloor);	//Returns a value between (-1 -> 1)
		
		return (value+1)/2;	//Modifies value to be between (0 -> 1)
	}
}
