#version 330

in vec3 attr_pos;
in vec4 attr_shadow_coord;

out vec4 out_shadow_coord;

uniform mat4 uniform_transform;
uniform mat4 uniform_camera_view;
uniform mat4 uniform_camera_projection;

void main()
{
	out_shadow_coord = attr_shadow_coord;
	gl_Position = uniform_camera_projection * uniform_camera_view * uniform_transform * vec4(attr_pos, 1.0);
}
