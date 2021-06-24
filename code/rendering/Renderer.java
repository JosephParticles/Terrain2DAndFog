package Terrain2DAndFog.code.rendering;

import Terrain2DAndFog.code.Utils.Timer;
import Terrain2DAndFog.code.main.Window;
import Terrain2DAndFog.code.rendering.gameObjects.components.Texture;
import Terrain2DAndFog.code.rendering.gameObjects.meshes.mesh2d.Screen;
import Terrain2DAndFog.code.rendering.gameObjects.objects.objects3D.GameObject3D;
import Terrain2DAndFog.code.rendering.gameObjects.objects.objects3D.TerrainChunk3D;
import Terrain2DAndFog.code.rendering.openGL.handlers.Program;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL46;

import java.util.ArrayList;

public class Renderer {
    private Program programColourShapes;
    private Program programTextureShapes;
    private Program programTerrain;

    private Program programDepthBuffer;

    private Program programScreen;
    
    private ArrayList<Program> programs;
    
    private Program currentProgram;

    private Vector3f backgroundColour;

    public Renderer(){

    }

    public void init()throws Exception{
    	programs=new ArrayList<>();
        programColourShapes=new Program("Colour shapers");
        programTextureShapes=new Program("Texture shapes");
        programTerrain=new Program("Terrain");
        programDepthBuffer=new Program("Depth buffer");
        programScreen=new Program("Screen");
        
        programs.add(programColourShapes);
        programs.add(programTextureShapes);
        programs.add(programTerrain);
        programs.add(programDepthBuffer);
        programs.add(programScreen);
        

        programColourShapes.attachShaders(new String[]{
        		"src/Terrain2DAndFog/code/rendering/openGL/glsl/shader3D/colourCube/vertex.glsl",
        		"src/Terrain2DAndFog/code/rendering/openGL/glsl/shader3D/colourCube/geometry.glsl",
        		"src/Terrain2DAndFog/code/rendering/openGL/glsl/shader3D/colourCube/fragment.glsl"},
                new int[]{
                        GL46.GL_VERTEX_SHADER,
                        GL46.GL_GEOMETRY_SHADER,
                        GL46.GL_FRAGMENT_SHADER});
        
        programTextureShapes.attachShaders(new String[]{
        		"src/Terrain2DAndFog/code/rendering/openGL/glsl/shader3D/TextureShapes/vertex.glsl",
        		"src/Terrain2DAndFog/code/rendering/openGL/glsl/shader3D/TextureShapes/geometry.glsl",
        		"src/Terrain2DAndFog/code/rendering/openGL/glsl/shader3D/TextureShapes/fragment.glsl"},
                new int[]{
                        GL46.GL_VERTEX_SHADER,
                        GL46.GL_GEOMETRY_SHADER,
                        GL46.GL_FRAGMENT_SHADER
                });
        
        programTerrain.attachShaders(
                new String[]{"src/Terrain2DAndFog/code/rendering/openGL/glsl/shader3D/terrain/vertex.glsl",
        		"src/Terrain2DAndFog/code/rendering/openGL/glsl/shader3D/terrain/geometry.glsl",
        		"src/Terrain2DAndFog/code/rendering/openGL/glsl/shader3D/terrain/fragment.glsl"},
                new int[]{
                        GL46.GL_VERTEX_SHADER,
                        GL46.GL_GEOMETRY_SHADER,
                        GL46.GL_FRAGMENT_SHADER
                });

        programDepthBuffer.attachShaders(new String[]{
                "src/Terrain2DAndFog/code/rendering/openGL/glsl/shader3D/depthBuffer/vertex.glsl",
                "src/Terrain2DAndFog/code/rendering/openGL/glsl/shader3D/depthBuffer/fragment.glsl"},
                new int[]{
                        GL46.GL_VERTEX_SHADER,
                        GL46.GL_FRAGMENT_SHADER
                });

        programScreen.attachShaders(new String[]{
                "src/Terrain2DAndFog/code/rendering/openGL/glsl/shaderEffects/frame/vertex.glsl",
                "src/Terrain2DAndFog/code/rendering/openGL/glsl/shaderEffects/frame/fragment.glsl"},
                new int[]{
                        GL46.GL_VERTEX_SHADER,
                        GL46.GL_FRAGMENT_SHADER
                });
        
        programColourShapes.link();
        programTextureShapes.link();
        programTerrain.link();
        programDepthBuffer.link();
        programScreen.link();
        
        createUniforms();

        backgroundColour= new Vector3f(0.1f,0.1f,0.2f);
    }
    
    public Program getCurrentProgram(){
    	return currentProgram;
    }
    
     public void setProgram(Program program){
        if(currentProgram!=null){
            currentProgram.unlinkProgram();
        }
        program.useProgram();
        currentProgram=program;
     }

     public void renderScreen(Window window, Timer timer, Camera camera, ArrayList<GameObject3D> objects3D, Screen screen, Texture texture){

         screen.getFrameBuffer().bindBuffer();
         clear();
         GL46.glEnable(GL46.GL_DEPTH_TEST);

         GL46.glViewport(0, 0, window.getWidth(), window.getHeight());

         renderScene(window,timer,camera,objects3D);

         screen.getFrameBuffer().unbindBuffer();

         GL46.glDisable(GL46.GL_DEPTH_TEST);

         GL46.glClearColor(backgroundColour.x, backgroundColour.y, backgroundColour.z, 1.0f);

         GL46.glClear(GL46.GL_COLOR_BUFFER_BIT| GL46.GL_DEPTH_BUFFER_BIT);

         setProgram(programScreen);
         screen.render(currentProgram,backgroundColour,texture);
     }

    public void renderScene(Window window, Timer timer, Camera camera, ArrayList<GameObject3D> objects3D){


        GL46.glViewport(0, 0, window.getWidth(), window.getHeight());

        if (window.getResized()) {
            GL46.glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResized(false);
        }

        //Rendering game objects to screen
        for(GameObject3D object3D: objects3D) {
            if(object3D instanceof TerrainChunk3D){
                if(currentProgram!=programTerrain){
                    setProgram(programTerrain);
                }
                currentProgram.setUniform("transformationMatrix", object3D.getTransformMatrix());
                currentProgram.setUniform("magnitude", 25);
                currentProgram.setUniform("projectionMatrix", camera.getProjMatrix());
                currentProgram.setUniform("worldMatrix",camera.getViewMatrix());
            }else{
                if(currentProgram!=programColourShapes){
                    setProgram(programColourShapes);
                }

            }
        	object3D.render(currentProgram,camera, (float)timer.getCurrentTime());
        }
        //colourMesh.render(programColourShapes,(float)timer.getCurrentTime());
    }
    
    public void createUniforms(){
		programColourShapes.createUniform("projectionMatrix");
		programColourShapes.createUniform("worldMatrix");
		programColourShapes.createUniform("transformationMatrix");
		
		programTextureShapes.createUniform("projectionMatrix");
		programTextureShapes.createUniform("worldMatrix");
    	programTextureShapes.createUniform("textureSampler");
    	
		programTerrain.createUniform("projectionMatrix");
		programTerrain.createUniform("worldMatrix");
		programTerrain.createUniform("transformationMatrix");
		programTerrain.createUniform("magnitude");
		programTerrain.createUniform("cameraPosition");
		programTerrain.createUniform("maxDistance");

		programDepthBuffer.createUniform("projectionMatrix");
        programDepthBuffer.createUniform("worldMatrix");
        programDepthBuffer.createUniform("transformationMatrix");

        programScreen.createUniform("screenColourTexture");
        programScreen.createUniform("bgColour");
    }

    public void clear(){
        GL46.glClear(GL46.GL_COLOR_BUFFER_BIT | GL46.GL_DEPTH_BUFFER_BIT);
    }

    public void cleanup(){
        currentProgram.unlinkProgram();
        
        for (Program program : programs) {
			program.cleanup();
		}
    }

}
