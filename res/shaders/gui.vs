#version 330

in vec3 attr_pos;

void main()
{
    gl_Position = vec4(attr_pos, 1.0);
}
