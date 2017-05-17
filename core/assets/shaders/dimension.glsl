#ifdef GL_ES
    precision mediump float;
#endif

varying vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;
uniform mat4 u_projTrans;

void main() {
		vec3 color = texture2D(u_texture, v_texCoords).rgb * v_color;
		float oldR = color.r;
		float oldG = color.g;
		float oldB = color.b;
		float nonRedDivisor = 1.0/2.0;
        color.g = oldB * nonRedDivisor;
        color.b = oldG * nonRedDivisor;
        color.r = oldR * 1.5;
        gl_FragColor = vec4(color, texture2D(u_texture, v_texCoords).a);
}