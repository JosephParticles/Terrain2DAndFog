package Terrain2DAndFog.code.Utils;

import java.util.ArrayList;

public class PositionArrayList<Type> {
	
	ArrayList<Type> array;
	private int minPointer;

	public PositionArrayList(){

		array=new ArrayList<>();
		minPointer=0;
	}
	
	public Type getValue(int index){
		if((index-minPointer<array.size())&&index>=minPointer){
			return array.get(index-minPointer);
		}else{
			return null;
		}
	}
	
	private boolean positionExists(int index){
		return (index>=minPointer&&index-minPointer<array.size());
	}

	public boolean positionHasValue(int index){
		if(!positionExists(index)){
			return false;
		}else if(getValue(index)==null){
			return false;
		}
		return true;
	}
	
	public void addValue(Type value, int index){
		if(getValue(index)==null){
			if(index<minPointer){	
				shuffleLeft(minPointer-index);
				array.set(0, value);
			}else if(index>array.size()){
				for(int i=array.size();i<index;i++){
					array.add(null);
				}
			}else{
				array.add(index-minPointer, value);
			}
			
		}
	}
	
	public void overrideValue(Type value, int index){
		if(index<minPointer){
			shuffleLeft(minPointer-index);
			array.set(0,value);
		}if(index>=minPointer&&index<array.size()+minPointer){
			array.set(index-minPointer,value);
		}else{
			for(int i=array.size();i<index-minPointer;i++){
				array.add(null);
			}
			array.add(value);
		}
		
	}
	
	public int getMinPointer(){
		return minPointer;
	}
	
	public int getMaxIndex(){
		return (array.size()+minPointer)-1;
	}
	
	public ArrayList<Type> getRawArray(){
		return array;
	}
	
	private void shuffleLeft(int iterations){
		ArrayList<Type> tempArray=new ArrayList<>(array);
		array.clear();
		for(int i=0;i<iterations;i++){
			array.add(null);
			minPointer--;
		}
		array.addAll(tempArray);
		tempArray.clear();
	}
}
