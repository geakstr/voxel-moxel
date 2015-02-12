#version 330 core

in vec2 out_colored_tex_off;
in vec2 out_colored_tex_repeat_number;
in vec2 out_texture_info;
in vec2 out_tex_coord;

uniform sampler2D uniform_texture;

out vec4 FragColor;

void main()
{
    vec2 tex_coord = out_tex_coord.xy;
    vec2 colored_tex_off = out_colored_tex_off.xy;

    float pixel_size = 1.0 / 4096.0;

    float crop_size = pixel_size * 16.0;

    tex_coord.x = fract(tex_coord.x / out_colored_tex_repeat_number.x) * pixel_size * out_colored_tex_repeat_number.x + colored_tex_off.x;
    tex_coord.y = fract(tex_coord.x / out_colored_tex_repeat_number.x) * pixel_size * out_colored_tex_repeat_number.x + colored_tex_off.x;

    vec4 color = texture(uniform_texture, tex_coord);

    FragColor = color;
}
