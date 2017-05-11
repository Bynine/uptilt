#ifdef GL_ES
    precision mediump float;
#endif

varying vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;
uniform mat4 u_projTrans;

float contrast(float x){
	return (x - 30.0/255.0)*1.2;
}

void main() {
        vec3 color = texture2D(u_texture, v_texCoords).rgb * v_color;
        float oldR = color.r;
        float oldG = color.g;
        float oldB = color.b;
        float modder = 0.20;
        color.r = 1.02*(color.r + (modder * oldG) - (modder * oldB));
        color.g = 0.85*(color.g + (modder * oldR) - (modder * oldB));
        color.b = 0.85*(color.b + (modder * oldR) - (modder * oldG));
        gl_FragColor = vec4(color, texture2D(u_texture, v_texCoords).a);
}