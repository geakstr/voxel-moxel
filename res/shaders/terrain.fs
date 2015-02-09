#version 330

in vec2 out_tex_offset;
in vec2 out_tex_coord;
in vec3 out_color;

uniform sampler2D uniform_texture;

out vec4 FragColor;

void main()
{
    vec2 tex_coord = out_tex_coord.xy;

    float pixel_size = 1.0 / 80.0;

    float crop_size = pixel_size * 16.0;

    tex_coord.x = fract(tex_coord.x) * crop_size + out_tex_offset.x;
    tex_coord.y = fract(tex_coord.y) * crop_size + out_tex_offset.y;

    vec4 tex_color = texture(uniform_texture, tex_coord);
    vec4 color = vec4(tex_color.r * out_color.r, tex_color.g * out_color.g, tex_color.b * out_color.b, 1.0);

    FragColor = color;
}
