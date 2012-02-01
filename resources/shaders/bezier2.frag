#if __VERSION__ != 110
precision mediump float;
#endif

uniform vec3 color;

varying vec2 positionGradient;

void main()
{
    vec2 asd = normalize(positionGradient);

    float alpha = max(0.3, 0.2f);


    gl_FragColor = vec4(color,alpha);
}

