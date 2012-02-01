

uniform vec3 color;

varying vec2 positionGradient;

void main()
{
    vec2 asd = normalize(positionGradient);
    float x_alpha = abs(asd.x);
    float y_alpha = abs(asd.y);

    float alpha = max(0.3, (x_alpha + y_alpha) * x_alpha * y_alpha);

    gl_FragColor = vec4(color,alpha);
}

