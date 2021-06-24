#version 450

layout(triangles) in;
layout(triangle_strip,max_vertices=3) out;

in vec3 vertexPosition[];
in vec3 vertexColour[];

out vec3 fragmentPosition;
out vec3 fragColour;

void main() {
    gl_Position=gl_in[0].gl_Position;
    fragmentPosition=vertexPosition[0];
    fragColour=vertexColour[0];
    EmitVertex();
    gl_Position=gl_in[1].gl_Position;
    fragmentPosition=vertexPosition[1];
    fragColour=vertexColour[1];
    EmitVertex();
    gl_Position=gl_in[2].gl_Position;
    fragmentPosition=vertexPosition[2];
    fragColour=vertexColour[2];
    EmitVertex();

    EndPrimitive();
}
