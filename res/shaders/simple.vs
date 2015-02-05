#version 120

attribute vec3 attr_pos;
attribute vec2 attr_texture_offset;

varying vec2 out_texture_offset;

uniform mat4 uniform_transform;
uniform mat4 uniform_camera_view;
uniform mat4 uniform_camera_projection;

void main()
{
    out_texture_offset = attr_texture_offset;
	gl_TexCoord[0] = gl_MultiTexCoord0;
    gl_Position = uniform_camera_projection * uniform_camera_view * uniform_transform * vec4(attr_pos, 1.0);
}
