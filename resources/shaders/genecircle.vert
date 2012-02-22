#if __VERSION__ != 110
precision mediump float;
#endif

uniform mat4 modelMatrix;

uniform vec2 mouse;

attribute vec2 vertexPosition;
varying float dist;
varying float dotproduct;

float own_pow(float base, int exp)
{
    float ret = 1.0;
    for(int i = 0; i < exp; ++i)
        ret *= base;
    return ret;
}

void main()
{
    float dot = mouse.x * vertexPosition.x + mouse.y * vertexPosition.y;

    dot = own_pow(dot, 231); // TODO : this is horrible

    dotproduct = max(0.0, dot);

    vec4 vertexPos = modelMatrix * vec4(vertexPosition, 0.0, 1.0);

    dist = length(vertexPosition.xy);

    gl_Position = vertexPos;
}