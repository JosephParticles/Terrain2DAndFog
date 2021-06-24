#version 450

layout(triangles) in;
layout(triangle_strip,max_vertices=3) out;

in vec2 textCoord[];
out vec2 outTextCoord;

void main() {
    gl_Position=gl_in[0].gl_Position;
    outTextCoord=textCoord[0];
    EmitVertex();
    gl_Position=gl_in[1].gl_Position;
    outTextCoord=textCoord[1];
    EmitVertex();
    gl_Position=gl_in[2].gl_Position;
    outTextCoord=textCoord[2];
    EmitVertex();

    EndPrimitive();
}
