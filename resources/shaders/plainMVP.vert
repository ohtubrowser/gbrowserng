uniform mat4 MVP;

attribute vec4 inPosition;

void main()
{
	gl_Position = MVP*inPosition;
}