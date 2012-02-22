#if __VERSION__ != 110
precision mediump float;
#endif

varying vec2 positionGradient;

void main()
{
    float x = positionGradient.x;
    float y = positionGradient.y;
    float d = distance(vec2(1,0),vec2(abs(x),y));
    float d2 = distance(vec2(1.5,0),vec2(abs(x)*1.5,y));
    float d3 = d2*d2*d2*3*(1.0-d2*d2);
    float d4 = d2*d2*d2*d2;

    vec4 innerColor = vec4(1.0, 1.0, 1.0, 1.0*d4);
    vec4 borderColor = vec4(0.0, 0.0, 0.0, 1.0*d4);
	gl_FragColor = mix(innerColor, borderColor, d3);
}

