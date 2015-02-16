#version 330

in vec2 attr_pos;
in vec3 attr_color;

out vec4 color;

void main()
{
    color = vec4(attr_color, 1.0);
    gl_Position = vec4(attr_pos, 0.0, 1.0);
}
