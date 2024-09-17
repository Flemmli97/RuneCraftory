#version 150

uniform mat4 ModelViewMat;
uniform mat4 ProjMat;
uniform sampler2D Sampler1;

in vec3 Position;
in vec4 Color;
in vec4 Color2;
in vec2 UV0;
in ivec2 UV1;
in float Time;

out vec4 vertexColor;
out vec4 vertexColor2;
out vec4 overlayColor;
out vec2 texCoord0;
out float tickTime;

void main() {
    gl_Position = ProjMat * ModelViewMat * vec4(Position, 1.0);

    vertexColor = Color;
    vertexColor2 = Color2;
    overlayColor = texelFetch(Sampler1, UV1, 0);
    texCoord0 = UV0;
    tickTime = Time;
}
