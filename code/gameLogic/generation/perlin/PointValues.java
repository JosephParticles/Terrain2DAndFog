package Terrain2DAndFog.code.gameLogic.generation.perlin;

import Terrain2DAndFog.code.Utils.PositionArrayList;
import org.joml.Vector2f;

import java.util.concurrent.ThreadLocalRandom;


public class PointValues {
	private PositionArrayList<PositionArrayList<Vector2f>> positions;
	
	public PointValues() {
		positions = new PositionArrayList<>();
	}

	public Vector2f get(int x, int y) {
		if (getVectorAtPosition(x, y) == null) {
			addRandomUnitVector(x, y);
		}if(getVectorAtPosition(x,y)==null){
			System.out.println("Gameing");
		}
		return getVectorAtPosition(x, y);
	}

	public Vector2f getVectorAtPosition(int x, int y) {
		if (!positions.positionHasValue(x)) {
			return null;
		}
		return positions.getValue(x).getValue(y);
	}

	private void addVector(int x, int y, Vector2f value) {
		// Checks if an array exists at that x position and creates one if it doesn't
		if (!positions.positionHasValue(x)) {
			positions.overrideValue(new PositionArrayList<Vector2f>(), x);
		}

		// Don't have to do the check here as I'm overriding whatever was there anyway
		positions.getValue(x).overrideValue(value, y);
	}

	private Vector2f genRandomUnitVector() {
		double randNum = ThreadLocalRandom.current().nextDouble() * Math.PI * 2;
		return new Vector2f((float) Math.cos(randNum), (float) Math.sin(randNum));
	}

	private void addRandomUnitVector(int x, int y) {
		Vector2f vec=genRandomUnitVector();

		addVector(x, y, vec);
	}



}
