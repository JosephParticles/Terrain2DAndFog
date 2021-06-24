#version 450

layout(local_size_x = 1, local_size_y = 1) in;
layout(rgba32f, binding = 0) uniform image2D img_output;
layout(rgba32f, binding = 1) uniform image2D img_second;

float pseudoRand(ivec2 randVal){
    return fract(sin(dot(randVal, vec2(12.9898, 78.233))) * 43758.5453);
}

void main() {
    vec4 pixel=vec4(0,0,0,1);

    ivec2 pixelCoords= ivec2(gl_GlobalInvocationID.xy);

    pixel.xyz=vec3(pseudoRand(pixelCoords));

    imageStore(img_output,pixelCoords,pixel);
    imageStore(img_second,pixelCoords,1-pixel);
}
