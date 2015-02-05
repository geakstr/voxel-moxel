#version 120

varying vec2 out_texture_offset;

uniform sampler2D uniform_texture;

void main()
{
    vec2 texCoord = gl_TexCoord[0].xy;

    float pixel_size = 1.0 / 80.0;

    float crop_size = pixel_size * 16.0;

    texCoord.x = fract(texCoord.x) * crop_size + out_texture_offset.x;
    texCoord.y = fract(texCoord.y) * crop_size + out_texture_offset.y;

    gl_FragColor = texture2D(uniform_texture, texCoord);
}
