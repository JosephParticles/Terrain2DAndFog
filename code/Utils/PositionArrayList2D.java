package Terrain2DAndFog.code.Utils;

import java.util.ArrayList;

public class PositionArrayList2D<Type> {
	
	PositionArrayList<PositionArrayList<Type>> array;
	
	public PositionArrayList2D(){
		array=new PositionArrayList<>();
	}
	
	public Type getValue(int x, int y){
		if (!array.positionHasValue(x)) {
			return null;
		}
		return array.getValue(x).getValue(y);
	}
	
	public void addValue( int x, int y,Type value){
		if (!array.positionHasValue(x)) {
			array.overrideValue(new PositionArrayList<Type>(), x);
		}

		array.getValue(x).overrideValue(value, y);
	}
	
	public boolean checkIfPositionExists(int x, int y){
		if(x>array.getMinPointer()&&x<=array.getMaxIndex()){
			if(array.getValue(x)!=null){
				if(y>array.getValue(x).getMinPointer()&&y<=array.getValue(x).getMaxIndex()){
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean checkIfPositionHasValue(int x, int y){
		if(x>array.getMinPointer()&&x<=array.getMaxIndex()){
			if(array.getValue(x)!=null){
				if(y>array.getValue(x).getMinPointer()&&y<=array.getValue(x).getMaxIndex()){
					return array.getValue(x).getValue(y)!=null;
				}
			}
		}
		return false;
	}
	
	public ArrayList<Type> getRawArray(){
		ArrayList<Type> arrayList=new ArrayList<>();
		for (PositionArrayList<Type> column : array.getRawArray()) {
			if(column!=null){
				for(Type value:column.getRawArray()){
					if(value!=null){
						arrayList.add(value);
					}
				}
			}
		}
		return arrayList;
	}

}
