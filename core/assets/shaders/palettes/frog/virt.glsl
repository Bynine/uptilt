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
        color.r = ((color.r*2.5 + color.g + color.b/2.4)/4.0 - 33.0/255.0) * 2.2;
        color.g = 0;
        color.b = 0;
        gl_FragColor = vec4(color, texture2D(u_texture, v_texCoords).a);
}