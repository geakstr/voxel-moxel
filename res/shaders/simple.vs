#version 120

attribute vec3 position;

void main()
{
    gl_Position = vec4(0.25 * position, 1.0);
}
