
#if __VERSION__ != 110
precision mediump float;
#endif

uniform float thickness;

varying float dist;
varying float dotproduct;

void main() {
    float d = ( dist-(1.0-thickness)) / thickness;

	float alpha1 = abs(0.5 - d)*2;
	alpha1 = alpha1*alpha1*alpha1;
    d = (d - 0.5) * 2.0;
    d = 1.0 - d * d;

	float alpha = alpha1 * d;

    vec4 color1 = vec4(d, 0.0, 0.0, 1.0f*d);
    vec4 color2 = vec4(0.0, 0.0, 0.0, alpha);
    gl_FragColor = dotproduct * color1 + (1.0 - dotproduct) * color2;
}