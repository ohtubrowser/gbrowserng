#if __VERSION__ != 110
precision mediump float;
#endif

uniform vec3 color;

varying vec4 normVec;

void main()
{
	float dist = normVec.x*normVec.x + normVec.y*normVec.y; // This is not always 1 because it is interpolated
    gl_FragColor = vec4(color,1.0-dist*dist*dist);
}

