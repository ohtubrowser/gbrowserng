#if __VERSION__ != 110
precision mediump float;
#endif

const float widthDiffFactor = 6.0;

uniform mat4 viewMatrix;
uniform mat4 modelMatrix;
uniform mat4 projectionMatrix;

uniform vec2 ControlPoint1;
uniform vec2 ControlPoint2;
uniform vec2 ControlPoint3;

uniform float width;
uniform float tstep;

attribute float t;

varying vec4 normVec;

vec2 calcBezierPosition(float t)
{
	return (1.0-t)*((1.0-t)*ControlPoint1 + t*ControlPoint2)+t*((1.0-t)*ControlPoint2+t*ControlPoint3);
}

void main()
{
	float thisT = abs(t);
        vec2 vertexPosition = calcBezierPosition(thisT);
	float prevT = abs(t) - tstep;
	vec2 prevVertexPosition = calcBezierPosition(prevT);

        vec4 vertexPos = modelMatrix * vec4(vertexPosition, 0.0, 1.0);

	normVec = vec4(vertexPosition - prevVertexPosition, 0, 0); normVec.xy = normVec.yx; normVec.y = -normVec.y;
	normVec = normalize(normVec);
	if(t < 0.0)
		normVec = normVec * -1.0;

	vertexPos = vertexPos + (((abs(thisT-0.5)+0.1)* widthDiffFactor * width ) * normVec); // This makes the curve thinner in the center

        gl_Position = projectionMatrix * viewMatrix * vertexPos;
}
