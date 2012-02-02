#if __VERSION__ != 110
precision mediump float;
#endif

uniform mat4 viewMatrix;
uniform mat4 modelMatrix;
uniform mat4 projectionMatrix;

uniform vec2 ControlPoint1;
uniform vec2 ControlPoint2;
uniform vec2 ControlPoint3;

attribute float t;

void main()
{
    vec2 vertexPosition = (1.0-t)*((1.0-t)*ControlPoint1 + t*ControlPoint2)+t*((1.0-t)*ControlPoint2+t*ControlPoint3);
    vec4 vertexPos = modelMatrix * vec4(vertexPosition, 0.0, 1.0);
    gl_Position = projectionMatrix * viewMatrix * vertexPos;
}
