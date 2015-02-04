#version 120

uniform float uniform_color;
uniform mat4 uniform_transform;
uniform mat4 uniform_camera_view;
uniform mat4 uniform_camera_projection;

void main()
{
	gl_TexCoord[0] = gl_MultiTexCoord0;
    gl_Position = uniform_camera_projection * uniform_camera_view * uniform_transform * gl_Vertex;
}
