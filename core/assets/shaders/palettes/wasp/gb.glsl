#ifdef GL_ES
    precision mediump float;
#endif

varying vec4 v_color;
varying vec2 v_texCoords;
varying float base;
uniform sampler2D u_texture;
uniform mat4 u_projTrans;

void main() {
        vec3 color = texture2D(u_texture, v_texCoords).rgb * v_color;
        color.r = (color.r + color.g + color.b)/3;
        color.g = color.r + 40.0/255.0;
        color.b = 15.0/255.0;
        gl_FragColor = vec4(color, texture2D(u_texture, v_texCoords).a);
}