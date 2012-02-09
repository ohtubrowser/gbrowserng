#if __VERSION__ != 110
precision mediump float;
#endif

varying vec2 texCoordFrag;
uniform sampler2D texture;
uniform float alpha;
void main() {
    vec4 color = texture2D(texture, texCoordFrag);
    color.w = alpha;
    gl_FragColor = color;
}
