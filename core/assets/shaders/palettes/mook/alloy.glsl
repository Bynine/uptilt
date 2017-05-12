#ifdef GL_ES
    precision mediump float;
#endif

varying vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;
uniform mat4 u_projTrans;

void main() {
        vec3 color = texture2D(u_texture, v_texCoords).rgb * v_color;
        color.r = (color.r*1.5 + color.g*2 - color.b)/2.2;
        //color.r = (color.r + color.g + color.b)/3.0;
        color.g = color.r;
        color.b = color.r;
        gl_FragColor = vec4(color, texture2D(u_texture, v_texCoords).a);
}