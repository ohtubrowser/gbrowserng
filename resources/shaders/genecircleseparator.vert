#if __VERSION__ != 110
precision mediump float;
#endif

uniform mat4 viewMatrix;
uniform mat4 modelMatrix;
uniform mat4 projectionMatrix;

attribute vec2 vertexPosition;
varying vec2 positionGradient;

void main()
{
    positionGradient = vertexPosition;
    vec4 vertexPos = modelMatrix * vec4(vertexPosition, 0.0, 1.0);
    gl_Position = projectionMatrix * viewMatrix * vertexPos;
}

