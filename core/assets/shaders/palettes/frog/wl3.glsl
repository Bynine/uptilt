#ifdef GL_ES
    precision mediump float;
#endif

varying vec4 v_color;
varying vec2 v_texCoords;
varying float base;
uniform sampler2D u_texture;
uniform mat4 u_projTrans;

vec3 setColor(vec3 colorVec, float r, float g, float b){
	colorVec.r = r/255.0;
    colorVec.g = g/255.0;
    colorVec.b = b/255.0;
    return colorVec;
}

void main() {
        vec3 color = texture2D(u_texture, v_texCoords).rgb * v_color;
        if      (color.r <  22.0/255.0){  // black
        	color = setColor(color, 12, 12, 12);        
        }
        else if (color.r <  102.0/255.0){// brown
        	color = setColor(color, 91, 12, 98);        
        }
        else if (color.r <  116.0/255.0){ // blue
        	color = setColor(color, 12, 161, 238);        
        }
        else if (color.r <  136.0/255.0){ // grey-green
        	color = setColor(color, 22, 142, 76);         
        }
        else if (color.r <  214.0/255.0){ // red
        	color = setColor(color, 238, 238, 230);
        }
        else { 							  // yellow-white
        	color = setColor(color, 238, 120, 80);
        }
        gl_FragColor = vec4(color, texture2D(u_texture, v_texCoords).a);
}