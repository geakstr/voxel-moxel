#version 330

in vec3 attr_pos;

uniform mat4 uniform_transform;
uniform mat4 uniform_camera_view;
uniform mat4 uniform_camera_projection;


out vec4 color;

void main()
{
    color = vec4(clamp(attr_pos, 0.0, 1.0), 1.0);
    gl_Position = uniform_camera_projection * uniform_camera_view * uniform_transform * vec4(attr_pos, 1.0);
}
