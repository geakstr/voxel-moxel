#version 120

attribute vec3 attr_pos;
attribute vec2 attr_tex_offset;
attribute vec2 attr_tex_coord;
attribute vec3 attr_color;

varying vec2 out_tex_offset;
varying vec2 out_tex_coord;
varying vec3 out_color;

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
