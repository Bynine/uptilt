#ifdef GL_ES
    precision mediump float;
#endif

varying vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;
uniform mat4 u_projTrans;

vec3 setColor(vec3 colorVec, float r, float g, float b){
	colorVec.r = (colorVec.r + r/130.0)/3.0;
	colorVec.g = (colorVec.g + g/130.0)/3.0;
	colorVec.b = (colorVec.b + b/130.0)/3.0;
	return colorVec;
}

void main() {
        vec3 color = texture2D(u_texture, v_texCoords).rgb * v_color;
        float base = (color.r*1.6 + color.g*1.4 + color.b/2.5)/3.0;
        if (color.b == 155.0/255.0) color = setColor(color, 67, 150, 70);
        else if (color.r == 213.0/255.0) color = setColor(color, 244, 141, 38);
		else if (base < 40.0/255.0) color = setColor(color, 67, 64, 112);
		else if (base < 150.0/255.0) color = setColor(color, 134, 93, 180);
		else color = setColor(color, 244, 141, 38);
        gl_FragColor = vec4(color, texture2D(u_texture, v_texCoords).a);
}