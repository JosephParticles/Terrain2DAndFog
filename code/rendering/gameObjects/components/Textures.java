package Terrain2DAndFog.code.rendering.gameObjects.components;
import java.util.HashMap;
import java.util.Map;

public class Textures {
    public static Map<String, Texture> textures=new HashMap<>();

    public static Texture getTexture(String texturePath){
        if(!textures.containsKey(texturePath)){
            try {
                textures.put(texturePath,new Texture(texturePath));
            } catch (Exception e) {
                return null;
            }
        }
        return textures.get(texturePath);
    }
    
    public static void cleanup(){
    	for(Map.Entry<String, Texture> entry : textures.entrySet()){
    		entry.getValue().cleanup();
    	}
    }
}
