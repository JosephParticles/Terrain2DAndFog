#version 450

in vec4 outColour;
in vec3 fragPosition;

out vec4 FragColour;


void main(){
	if(fragPosition.y<-10){
		discard;
	}
    FragColour=vec4(1,1,1,1);
}
