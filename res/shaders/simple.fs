#version 330 core

in vec2 out_tex_offset;
in vec2 out_tex_coord;

uniform sampler2D uniform_texture;

out vec4 FragColor;

void main()
{
    vec2 tex_coord = out_tex_coord.xy;

    float pixel_size = 1.0 / 80.0;

    float crop_size = pixel_size * 16.0;

    tex_coord.x = fract(tex_coord.x) * crop_size + out_tex_offset.x;
    tex_coord.y = fract(tex_coord.y) * crop_size + out_tex_offset.y;

    FragColor = texture(uniform_texture, tex_coord);
}
