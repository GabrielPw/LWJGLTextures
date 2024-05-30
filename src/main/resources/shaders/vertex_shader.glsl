#version 330 core
layout (location = 0) in vec3 aPos;
layout (location = 1) in vec3 aVertexColor;
layout (location = 2) in vec2 aVertexTextureCoord;

out vec3 fragPos;
out vec3 vertexColor;
out vec2 vertexTextureCoord;

uniform mat4 transform;

void main()
{
    fragPos = aPos;
    vertexColor        = aVertexColor;
    vertexTextureCoord = aVertexTextureCoord;

    gl_Position = transform * vec4(aPos, 1.0);

};