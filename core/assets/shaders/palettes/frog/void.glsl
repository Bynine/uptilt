#ifdef GL_ES
    precision mediump float;
#endif

varying vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;
uniform mat4 u_projTrans;

void main() {
        vec3 color = texture2D(u_texture, v_texCoords).rgb * v_color;
        float base = (color.r*1.6 + color.g*1.4 + color.b/2.5)/3.0;
        color.r = (base + pow(base, 1.1) - 30.0/255.0)/2;
        color.g = (base + pow(base/1.8, 1.8) - 15.0/255.0)/2;
        color.b = (base + pow(base*1.8, 1.0/1.8) - 30.0/255.0)/2;
        gl_FragColor = vec4(color, texture2D(u_texture, v_texCoords).a);
}