package Terrain2DAndFog.code.rendering.openGL.handlers;

import Terrain2DAndFog.code.Utils.FileHandling;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL46;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Program {
    private int programId;

    private ArrayList<Shader> shaders;

    public String programName;

    private final Map<String,Integer> uniforms;

    public Program(String name) throws Exception{
        this.programName=name;
        programId = GL46.glCreateProgram();
        if (programId == 0) {
            throw new Exception("Could not create Shader");
        }
        uniforms=new HashMap<>();
        shaders=new ArrayList<>();
    }

    public void attachShaders(Shader[] shaders){
        for(int i=0;i< shaders.length;i++){
            this.shaders.add(shaders[i]);
            GL46.glAttachShader(programId, shaders[i].getId());
        }
    }

    public void attachShaders(String[] shaders, int[] shaderType)throws Exception{
        if(shaders.length!=shaderType.length){
            System.out.println("ERROR: Shaders and shadertypes don't match");
        }else if(shaders.length==0){
            System.out.println("ERROR: No shaders attached");
        }

        for(int i=0;i<shaders.length;i++){
            this.shaders.add(new Shader(FileHandling.loadResource(shaders[i]),shaderType[i]));
            GL46.glAttachShader(programId,this.shaders.get(i).getId());
        }
    }

    
    public void link() throws Exception{
        GL46.glLinkProgram(programId);
        if (GL46.glGetProgrami(programId, GL46.GL_LINK_STATUS) == 0) {
            throw new Exception("Error linking Shader Terrain2DAndFog.code: " + GL46.glGetProgramInfoLog(programId, 1024));
        }

        for(Shader shader: shaders){
            if(shader!=null){
                if(shader.getId()==0){
                    GL46.glDetachShader(programId,shader.getId());
                }
            }
        }

        GL46.glValidateProgram(programId);
        if (GL46.glGetProgrami(programId, GL46.GL_VALIDATE_STATUS) == 0) {
            System.err.println("Warning validating Shader Terrain2DAndFog.code: " + GL46.glGetProgramInfoLog(programId, 1024));
        }
    }

    public void useProgram(){
        GL46.glUseProgram(programId);
    }

    public void unlinkProgram(){
        GL46.glUseProgram(0);
    }

    public void createUniform(String uniformName){
        int uniformLocation = GL46.glGetUniformLocation(programId,
                uniformName);
        if (uniformLocation < 0) {
            System.out.println("Uniform "+uniformName+" does not exist in this program.This could be because it isn't used or is in a different program");
        }else{
            uniforms.put(uniformName, uniformLocation);
        }

    }

    public void setUniform(String uniformName, Matrix4f value){
        if(hasUniform(uniformName)){
            try (MemoryStack stack = MemoryStack.stackPush()) {
                FloatBuffer fb = stack.mallocFloat(16);
                value.get(fb);
                GL46.glUniformMatrix4fv(uniforms.get(uniformName), false, fb);
            }
        }else{
            System.out.println("Uniform: "+uniformName+" does not exist in this program. Perhaps it exists in different program?");
        }
    }

    public void setUniform(String uniformName, float value){
        if(hasUniform(uniformName)){
            GL46.glUniform1f(uniforms.get(uniformName),value);
        }else{
            System.out.println("Uniform: "+uniformName+" does not exist in this program. Perhaps it exists in different program?");
        }
    }

    public void setUniform(String uniformName, double value){
        if(hasUniform(uniformName)){
            GL46.glUniform1d(uniforms.get(uniformName),value);
        }else{
            System.out.println("Uniform: "+uniformName+" does not exist in this program. Perhaps it exists in different program?");
        }
    }

    public void setUniform(String uniformName, int value){
        if(hasUniform(uniformName)){
            GL46.glUniform1i(uniforms.get(uniformName),value);
        }else{
            System.out.println("Uniform: "+uniformName+" does not exist in this program. Perhaps it exists in different program?");
        }

    }

    public void setUniform(String uniformName, Vector2f vector){
        if(hasUniform(uniformName)){
            GL46.glUniform2f(uniforms.get(uniformName),vector.x,vector.y);
        }else{
            System.out.println("Uniform: "+uniformName+" does not exist in this program. Perhaps it exists in different program?");
        }
    }

    public void setUniform(String uniformName, Vector3f vector){
        if(hasUniform(uniformName)){
            GL46.glUniform3f(uniforms.get(uniformName),vector.x,vector.y,vector.z);
        }else{
            System.out.println("Uniform: "+uniformName+" does not exist in this program. Perhaps it exists in different program?");
        }

    }


    public boolean hasUniform(String uniformName){
        return uniforms.containsKey(uniformName);
    }


    public int getId(){
        return programId;
    }
    
    public void cleanup(){
        if (programId != 0) {
            GL46.glDeleteProgram(programId);
        }
    }
}
