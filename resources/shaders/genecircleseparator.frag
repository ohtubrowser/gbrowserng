#if __VERSION__ != 110
precision mediump float;
#endif

varying vec2 positionGradient;

void main()
{
    float x_alpha = positionGradient.x * positionGradient.x;
    float y_alpha = 1.0-positionGradient.y * positionGradient.y;
    float alpha = x_alpha * y_alpha;
	gl_FragColor = vec4(0.0, 0.0, 0.0, alpha);
}

