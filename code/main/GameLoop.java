package Terrain2DAndFog.code.main;

import java.util.ArrayList;

import Terrain2DAndFog.code.IO.Input;
import Terrain2DAndFog.code.Utils.Timer;
import Terrain2DAndFog.code.gameLogic.generation.Generator;
import Terrain2DAndFog.code.gameLogic.player.Player;
import Terrain2DAndFog.code.rendering.Renderer;
import Terrain2DAndFog.code.rendering.gameObjects.components.Texture;
import Terrain2DAndFog.code.rendering.gameObjects.components.buffers.FrameBuffer;
import Terrain2DAndFog.code.rendering.gameObjects.meshes.mesh2d.Screen;
import Terrain2DAndFog.code.rendering.gameObjects.objects.objects3D.GameObject3D;
import Terrain2DAndFog.code.rendering.openGL.handlers.Program;
import org.joml.Math;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL46;


public class GameLoop{
    private float FPS=60.0f;    //Frames per second
    private float UPS=30.0f;    //Updates per second

    private final Window window;
    private final Renderer renderer;
    private final Timer timer;
    private final Player player;
    private final Input input;
    private final Generator generator;

    private final FrameBuffer frameBuffer;

    private ArrayList<GameObject3D> objects3D;
    
    private Screen screen;

    private Program computeShaderProgram;

    private Texture textureInp;
    private Texture textureOut;


    public GameLoop(Window window){
        this.window=window;
        this.renderer=new Renderer();
        this.timer=new Timer(UPS,FPS);
        this.player=new Player(window,Math.toRadians(60));
        this.input=new Input();
        this.generator=new Generator(32);
        this.frameBuffer=new FrameBuffer();

    }

    private void init() throws Exception{
        window.init();
        renderer.init();
        input.init(window);
        
        Texture tempTexture=new Texture("src/Terrain2DAndFog/images/TomScott.png");
        
        Texture[] textures=new Texture[]{
                new Texture(window.getWidth(), window.getHeight(), GL46.GL_RGBA, GL46.GL_RGBA, GL46.GL_UNSIGNED_BYTE)
        };

        int[] attachments=new int[]{
                GL46.GL_COLOR_ATTACHMENT0
        };

        frameBuffer.init(window.getWidth(), window.getHeight(),textures,attachments);

        screen=new Screen(frameBuffer);

        objects3D=new ArrayList<>();
        //effects=new ArrayList<>();
        
        generator.genStart();

        textureInp =new Texture(32, 32, GL46.GL_RGBA32F,GL46.GL_RGBA,GL46.GL_UNSIGNED_BYTE);
        textureOut=new Texture(32,32,GL46.GL_RGBA32F,GL46.GL_RGBA,GL46.GL_UNSIGNED_BYTE);

        GL46.glBindImageTexture(0, textureInp.getId(), 0, false, 0, GL46.GL_WRITE_ONLY, GL46.GL_RGBA32F);
        GL46.glBindImageTexture(0, textureOut.getId(), 0, false, 0, GL46.GL_WRITE_ONLY, GL46.GL_RGBA32F);

        int[] troll=new int[1];

        for(int i=0;i<3;i++){
            GL46.glGetIntegeri_v(GL46.GL_MAX_COMPUTE_WORK_GROUP_COUNT,i,troll);

            System.out.println(troll[0]);
        }

        GL46.glGetIntegerv(GL46.GL_MAX_COMPUTE_WORK_GROUP_INVOCATIONS,troll);

        System.out.println(troll[0]);

        computeShaderProgram=new Program("Compute shader test");

        computeShaderProgram.attachShaders(new String[]{
            "src/Terrain2DAndFog/code/gameLogic/generation/perlin/perlinCompute.glsl"
        },new int[]{
                GL46.GL_COMPUTE_SHADER
        });

        computeShaderProgram.link();

        objects3D=generator.getAllChunks();

        window.loop();
    }

    private void loop(){

        while(!window.shouldClose()){
        	timer.update(System.currentTimeMillis());
            if(timer.getFrame()){
                render((float)timer.getDeltaFrame());
            }if(timer.getUpdate()){
                update((float)timer.getDeltaUpdate());
            }
        }

    }
    
    private void input(float deltaUpdate){

        float cameraMoveSpeed=80.0f;
        float cameraRotateSpeed=4.0f;

    	if(input.isKeyDown(GLFW.GLFW_KEY_ESCAPE)){
    		window.close();
    	}
    	if(input.isKeyDown(GLFW.GLFW_KEY_W)){
    		player.translateDirectionFacing(0, 0, -deltaUpdate * cameraMoveSpeed);
    	}else if(input.isKeyDown(GLFW.GLFW_KEY_S)){
    		player.translateDirectionFacing(0, 0, deltaUpdate * cameraMoveSpeed);
    	}
    	if(input.isKeyDown(GLFW.GLFW_KEY_A)){
    		player.translateDirectionFacing(-deltaUpdate * cameraMoveSpeed, 0, 0);
    	}else if(input.isKeyDown(GLFW.GLFW_KEY_D)){
    		player.translateDirectionFacing(deltaUpdate * cameraMoveSpeed, 0, 0);
    	}
    	if(input.isKeyDown(GLFW.GLFW_KEY_Q)){
    		player.translateDirectionFacing(0, deltaUpdate * cameraMoveSpeed, 0);
    	}else if(input.isKeyDown(GLFW.GLFW_KEY_E)){
    		player.translateDirectionFacing(0, -deltaUpdate * cameraMoveSpeed, 0);
    	}
    	
    	if(input.isKeyDown(GLFW.GLFW_KEY_LEFT)){
    		player.rotate(0,-deltaUpdate*cameraRotateSpeed,0);
    	}else if(input.isKeyDown(GLFW.GLFW_KEY_RIGHT)){
    		player.rotate(0,deltaUpdate*cameraRotateSpeed,0);
    	}
    	if(input.isKeyDown(GLFW.GLFW_KEY_UP)){
    		player.rotate(-deltaUpdate*cameraRotateSpeed,0,0);
    	}else if(input.isKeyDown(GLFW.GLFW_KEY_DOWN)){
    		player.rotate(deltaUpdate*cameraRotateSpeed,0,0);
    	}
    }

    private void render(float deltaFrame){
        window.loop();

        computeShaderProgram.useProgram();
        GL46.glDispatchCompute(32,32,1);

        GL46.glMemoryBarrier(GL46.GL_SHADER_IMAGE_ACCESS_BARRIER_BIT);

        renderer.renderScreen(window,timer,player,objects3D,screen, textureInp);


    }
    
    private float time=0;
    
    private int x=0;

    private void update(float deltaUpdate){
    	input(deltaUpdate);

    	if(!input.isKeyDown(GLFW.GLFW_KEY_SPACE)){
            generator.genLoop(player,deltaUpdate);
        }

    	if(generator.changeMade()){
    		objects3D=generator.getAllChunks();
    	}
    }

    private void cleanup(){
        window.cleanup();
        renderer.cleanup();
        
        frameBuffer.cleanup();

        for(GameObject3D obj:objects3D){
        	obj.cleanup();
        }
//        for(effectMesh effect:effects){
//            effect.cleanup();
//        }
        
    }

    public void run(){
        try {
            init();
            loop();
        }catch (Exception e){
            e.printStackTrace();
        }finally{
            cleanup();
        }

    }
}
