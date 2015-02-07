#version 330 core

in vec3 attr_pos;
in vec2 attr_tex_offset;
in vec2 attr_tex_coord;
in vec3 attr_color;

out vec2 out_tex_offset;
out vec2 out_tex_coord;
out vec3 out_color;

uniform mat4 uniform_transform;
uniform mat4 uniform_camera_view;
uniform mat4 uniform_camera_projection;

void main()
{
    out_tex_offset = attr_tex_offset;
	out_tex_coord = attr_tex_coord;
	out_color = attr_color;

    gl_Position = uniform_camera_projection * uniform_camera_view * uniform_transform * vec4(attr_pos, 1.0);
}
