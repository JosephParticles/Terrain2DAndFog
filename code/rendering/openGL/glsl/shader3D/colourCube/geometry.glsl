#version 450

layout(triangles) in;
layout(triangle_strip,max_vertices=3) out;



in vec4 vertexColour[];
in vec3 outPosition[];

out vec4 outColour;
out vec3 fragPosition;

void main() {
    gl_Position=gl_in[0].gl_Position;
    outColour=vertexColour[0];
    fragPosition=outPosition[0];
    EmitVertex();
    gl_Position=gl_in[1].gl_Position;
    outColour=vertexColour[1];
    fragPosition=outPosition[1];
    EmitVertex();
    gl_Position=gl_in[2].gl_Position;
    outColour=vertexColour[2];
    fragPosition=outPosition[2];
    EmitVertex();


    EndPrimitive();
}
