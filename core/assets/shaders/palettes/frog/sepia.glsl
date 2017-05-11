#ifdef GL_ES
    precision mediump float;
#endif

varying vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;
uniform mat4 u_projTrans;

void main() {
        vec3 color = texture2D(u_texture, v_texCoords).rgb * v_color;
        color.r = (color.r + color.g + color.b)/4 + 30.0/255.0 + (color.r/2);
        color.g = color.r * 0.65 - (color.b/5);
        color.b = color.r * 0.35 - (color.b/5);
        gl_FragColor = vec4(color, texture2D(u_texture, v_texCoords).a);
}