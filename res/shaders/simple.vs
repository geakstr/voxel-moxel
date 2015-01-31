#version 120

attribute vec3 position;

varying vec4 color;

uniform float uniform_color;
uniform mat4 uniform_transform;

void main()
{
    color = vec4(clamp(position, 0.0, uniform_color), 1.0);
    gl_Position = uniform_transform * vec4(position, 1.0);
}
