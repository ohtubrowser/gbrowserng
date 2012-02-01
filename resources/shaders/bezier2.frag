#if __VERSION__ != 110
precision mediump float;
#endif

uniform float lifetime;
uniform float uniAlpha;
varying vec2 positionGradient;

void main()
{
    float x_alpha = 1.0;// - positionGradient.x * positionGradient.x;
    float y_alpha = 1.0;// - positionGradient.y * positionGradient.y;

    // float val = (positionGradient.y - lifetime);
    // float height_val = max(0.0, 1.0 - 10.0 * val * val);
    // float alpha = x_alpha * y_alpha * height_val;

    float alpha = x_alpha * y_alpha;

    gl_FragColor = vec4(lifetime * lifetime * 0.2, 0.3, 0.4, alpha * uniAlpha);
}

