#if __VERSION__ != 110
precision mediump float;
#endif

varying vec2 texCoordFrag;
uniform sampler2D texture;
void main() {
    gl_FragColor = vec4(0.0, 0.0, 0.0, 1.0) + texture2D(texture, texCoordFrag);
}
