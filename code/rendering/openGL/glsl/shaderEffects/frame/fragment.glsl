#version 450

in vec2 fragTextureCoordinates;

uniform sampler2D screenColourTexture;
uniform vec3 bgColour;

out vec4 fragColour;

void main() {
	vec4 fragment=texture(screenColourTexture,fragTextureCoordinates);
	float depth=fragment.w;
    fragColour=vec4(mix(fragment.xyz,bgColour,depth),1);
}
