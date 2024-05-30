#version 330 core

out vec4 FragColor;

in vec3 fragPos;
in vec3 vertexColor;
in vec2 vertexTextureCoord;

uniform float time;
uniform sampler2D imgtexture;

void main()
{
    vec2 st = gl_FragCoord.xy;

    vec3 color = fragPos;
    color = vec3(1, 1, 1);

    FragColor = texture(imgtexture, vertexTextureCoord) * vec4(color, 1);
};