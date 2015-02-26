#version 330

uniform sampler2D uniform_shadow;

in vec4 out_shadow_coord;

out vec4 FragColor;

void main()
{	
	vec4 shadowCoordinateWdivide = out_shadow_coord / out_shadow_coord.w ;
	
	shadowCoordinateWdivide.z += 0.0005;
	
	float distanceFromLight = texture2D(uniform_shadow, shadowCoordinateWdivide.st).z;
	
 	float shadow = 1.0;
 	if (out_shadow_coord.w > 0.0) {
 		shadow = distanceFromLight < shadowCoordinateWdivide.z ? 0.5 : 1.0;
 	}
  	
	FragColor =	shadow * vec4(1.0, 1.0, 1.0, 1.0);
}