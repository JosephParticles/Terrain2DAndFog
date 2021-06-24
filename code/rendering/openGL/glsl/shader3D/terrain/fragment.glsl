#version 450

in vec3 fragmentPosition;
in vec3 fragColour;

uniform int magnitude;
uniform vec3 cameraPosition;
uniform float maxDistance;

layout(location=0) out vec4 FragColour;
layout(location=1) out vec4 depth;

float getDist(vec3 A, vec3 B){
	return sqrt((A.x-B.x)*(A.x-B.x)+(A.y-B.y)*(A.y-B.y)+(A.z-B.z)*(A.z-B.z));
}

void main(){
	depth=vec4(getDist(cameraPosition,fragmentPosition)/maxDistance);
	FragColour=vec4(fragmentPosition.y/magnitude,0,0,depth);
}