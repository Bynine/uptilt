#ifdef GL_ES
    precision mediump float;
#endif

varying vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;
uniform mat4 u_projTrans;

void main() {
        vec3 color = texture2D(u_texture, v_texCoords).rgb * v_color;
        float base = (color.r + color.g/2 + color.b/2)/2.5;
        color.r = base*0.6;
        color.g = base*0.75;
        color.b = base*1.05;
        gl_FragColor = vec4(color, texture2D(u_texture, v_texCoords).a);
}