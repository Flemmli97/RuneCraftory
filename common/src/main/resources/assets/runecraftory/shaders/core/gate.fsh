#version 150

uniform vec4 ColorModulator;

in vec4 vertexColor;
in vec4 vertexColor2;
in vec4 overlayColor;
in vec2 texCoord0;
in vec2 position;
in float tickTime;

out vec4 fragColor;

#define PI 3.14159

// The MIT License
// Copyright © 2013 Inigo Quilez
// Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions: The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software. THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
// https://www.youtube.com/c/InigoQuilez
// https://iquilezles.org/

vec2 hash(vec2 p)
{
    p = vec2(dot(p, vec2(127.1, 311.7)),
    dot(p, vec2(269.5, 183.3)));
    return fract(sin(p)*18.5453);
}

vec2 voronoi(in vec2 x, float time)
{
    vec2 n = floor(x);
    vec2 f = fract(x);
    vec3 m = vec3(8.0);
    for (int j=-1; j<=1; j++)
    for (int i=-1; i<=1; i++)
    {
        vec2  g = vec2(float(i), float(j));
        vec2  o = hash(n + g);
        vec2  r = g - f + (0.5+0.5*sin(time+6.2831*o));
        float d = dot(r, r);
        if (d<m.x)
        m = vec3(d, o);
    }
    return vec2(sqrt(m.x), m.y+m.z);
}
//==========================================

vec2 swirl(float radiusIn, float angleIn, vec2 uvIn, vec2 center, vec2 resolution){
    angleIn = angleIn * PI;
    center = center == vec2(0.) ? vec2(.5) : center;
    uvIn = uvIn - center;

    float len = length(uvIn * vec2(resolution.x / resolution.y, 1.));
    float angle = atan(uvIn.y, uvIn.x) + angleIn * smoothstep(radiusIn, 0., len);
    float radius = length(uvIn);
    return vec2(radius * cos(angle), radius * sin(angle)) + center;
}

vec4 portalSwirl(vec4 color, float density, float time, float radius, float angle, vec2 uvIn, vec2 center, vec2 resolution) {
    vec2 uv = swirl(radius, angle, uvIn, center, resolution);
    return vec4(1, 1, 1, voronoi(uv * density, time).x) * color;
}

void main() {
    vec4 color = vertexColor * 4.0;
    vec4 color2 = vertexColor2 * 3.;

    float density = 3.5;
    float radius = 0.9;
    float angle = 5.;
    float speed = 1500.;

    float radiantLen = 2.5;
    float radiantPow = 0.9;
    float radInner = 0.5;

    vec2 uv = texCoord0;
    vec2 center = vec2(0.5);

    float dist = distance(uv, center);
    float rad = 1. - pow(dist * radiantLen, radiantPow);
    vec4 finalColor = portalSwirl(color, density, tickTime * speed, radius, angle, uv, center, vec2(1.)) * rad;
    if (rad > 0.) {
        finalColor.rgb = max(color.rgb * 0.5, finalColor.rgb);
    }
    finalColor += portalSwirl(color2, density, tickTime * speed + 500., radius, angle, uv, center, vec2(1.)) * (rad * radInner);
    fragColor = finalColor;

    if (fragColor.a < 0.1) {
        discard;
    }
}
