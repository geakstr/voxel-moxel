#version 330

in vec2 out_tex_offset;
in vec2 out_tex_coord;
in vec3 out_color;
in vec2 out_texture_info;

uniform sampler2D uniform_texture;

out vec4 FragColor;

void main()
{
    vec2 tex_coord = out_tex_coord.xy;
	
    if (out_texture_info.x != 0 && out_texture_info.y != 0) {
    	float pixel_size = 1.0 / out_texture_info.x;
   	    float crop_size = pixel_size * out_texture_info.y;
   	    
   	    tex_coord.x = fract(tex_coord.x) * crop_size + out_tex_offset.x;
    	tex_coord.y = fract(tex_coord.y) * crop_size + out_tex_offset.y;
    }
    vec4 tex_color = texture(uniform_texture, tex_coord);

    FragColor = vec4(out_color, 1.0) * tex_color;
}
