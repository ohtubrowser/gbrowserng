#if __VERSION__ != 110
precision mediump float;
#endif

uniform vec3 color;

void main()
{
    gl_FragColor = vec4(color,0.6);
}

