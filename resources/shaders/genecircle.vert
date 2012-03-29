#if __VERSION__ != 110
precision mediump float;
#endif

uniform mat4 modelMatrix;

uniform vec2 mouse;

attribute vec2 vertexPosition;
varying float dist;
varying float angle;

void main()
{
    float dot = mouse.x * vertexPosition.x + mouse.y * vertexPosition.y;

    angle = max(0.0, 1.0 - acos(dot) * 6.0);

    vec4 vertexPos = modelMatrix * vec4(vertexPosition, 0.0, 1.0);

    dist = length(vertexPosition.xy);

    gl_Position = vertexPos;
}