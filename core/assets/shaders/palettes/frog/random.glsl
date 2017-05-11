#ifdef GL_ES
    precision mediump float;
#endif

varying vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;
uniform mat4 u_projTrans;

uniform float time;
out vec4 fragment;

// A single iteration of Bob Jenkins' One-At-A-Time hashing algorithm.
uint hash( uint x ) {
    x += ( x << 10u );
    x ^= ( x >>  6u );
    x += ( x <<  3u );
    x ^= ( x >> 11u );
    x += ( x << 15u );
    return x;
}

// Compound versions of the hashing algorithm I whipped together.
uint hash( uvec2 v ) { return hash( v.x ^ hash(v.y)                         ); }
uint hash( uvec3 v ) { return hash( v.x ^ hash(v.y) ^ hash(v.z)             ); }
uint hash( uvec4 v ) { return hash( v.x ^ hash(v.y) ^ hash(v.z) ^ hash(v.w) ); }

// Construct a float with half-open range [0:1] using low 23 bits.
// All zeroes yields 0.0, all ones yields the next smallest representable value below 1.0.
float floatConstruct( uint m ) {
    const uint ieeeMantissa = 0x007FFFFFu; // binary32 mantissa bitmask
    const uint ieeeOne      = 0x3F800000u; // 1.0 in IEEE binary32

    m &= ieeeMantissa;                     // Keep only mantissa bits (fractional part)
    m |= ieeeOne;                          // Add fractional part to 1.0

    float  f = uintBitsToFloat( m );       // Range [1:2]
    return f - 1.0;                        // Range [0:1]
}

// Pseudo-random value in half-open range [0:1].
float random( float x ) { 
		float randomness = 20.0;
		return ((randomness * x) + floatConstruct(hash(floatBitsToUint(x))))/(randomness+1); 
}
float random( vec3  v ) { return floatConstruct(hash(floatBitsToUint(v))); }

void main() {
        vec3 color = texture2D(u_texture, v_texCoords).rgb * v_color;
        vec3  inputs = vec3( gl_FragCoord.xy, time ); // Spatial and temporal inputs
        float rand = random(inputs);
        color.r = (rand + color.r) - 50.0/255.0;
        color.g = (rand + color.g) - 50.0/255.0;
        color.b = (rand + color.b) - 50.0/255.0;
        gl_FragColor = vec4(color, texture2D(u_texture, v_texCoords).a);
}
