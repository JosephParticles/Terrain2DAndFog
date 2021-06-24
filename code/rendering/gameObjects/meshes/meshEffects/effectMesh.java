package Terrain2DAndFog.code.rendering.gameObjects.meshes.meshEffects;

import Terrain2DAndFog.code.rendering.openGL.handlers.Program;

public class effectMesh {

    protected int vaoId;

    protected int vertexVboId;
    protected int indexVboId;

    protected int vertexCount;

    public effectMesh(){

    }

    public void render(Program program, float currentTime){}

    public void cleanup(){}

    public int getVaoId(){
        return vaoId;
    }

    public int getVertexCount(){
        return vertexCount;
    }

}
