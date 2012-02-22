#if __VERSION__ != 110
precision mediump float;
#endif

varying vec2 outVertexPos;

void main()
{
	float len = 0.2*outVertexPos.x*outVertexPos.x + outVertexPos.y*outVertexPos.y;
	float alpha = 0.8 - len;
	gl_FragColor = vec4(0.5, 0.5, 0.5, alpha);
}

