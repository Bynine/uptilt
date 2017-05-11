#ifdef GL_ES
    precision mediump float;
#endif

varying vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;
uniform mat4 u_projTrans;

void main() {
        vec3 color = texture2D(u_texture, v_texCoords).rgb * v_color;
        if (color.r < 22.0/255.0 && color.g < 35.0/255.0 && color.b < 44.0/255.0){
        	color.r = 0;
        	color.g = 0;
        	color.b = 0;
        }
        else{
        	color.r = v_texCoords.x;
        	color.g = v_texCoords.y;
       		color.b = 0.5;
        }
        gl_FragColor = vec4(color, texture2D(u_texture, v_texCoords).a);
}
