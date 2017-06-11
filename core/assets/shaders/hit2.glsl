#ifdef GL_ES
    precision mediump float;
#endif

varying vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;
uniform mat4 u_projTrans;

void main() {
		vec3 color = texture2D(u_texture, v_texCoords).rgb * v_color;
		float value = 1;
        color.g = value;
        color.b = value;
        color.r = value;
        gl_FragColor = vec4(color, texture2D(u_texture, v_texCoords).a);
}