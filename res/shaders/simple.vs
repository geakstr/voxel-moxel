#version 120

varying vec4 color;

uniform float uniform_color;
uniform mat4 uniform_transform;
uniform mat4 uniform_camera_view;
uniform mat4 uniform_camera_projection;

void main()
{
    color = vec4(clamp(vec3(gl_Vertex), 0.0, uniform_color), 1.0);
    gl_Position = uniform_camera_projection * uniform_camera_view * uniform_transform * gl_Vertex;
}
