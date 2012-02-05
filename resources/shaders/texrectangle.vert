#if __VERSION__ != 110
precision mediump float;
#endif

uniform mat4 modelViewMatrix;

attribute vec2 vertexCoord;
attribute vec2 texCoord;

varying vec2 texCoordFrag;

void main()
{
    texCoordFrag.x = texCoord.x;
    texCoordFrag.y = 1.0 - texCoord.y;
    gl_Position = modelViewMatrix * vec4(vertexCoord, 0.0, 1.0);
}
