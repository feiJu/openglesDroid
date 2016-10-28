precision mediump float;       	// Set the default precision to medium. We don't need as high of a 
								// precision in the fragment shader.
//varying vec4 v_Color;          	// This is the color from the vertex shader interpolated across the 
  								// triangle per fragment.
varying vec2 v_TexCoordinate;   // Interpolated texture coordinate per fragment.

uniform sampler2D u_Texture;    // The input texture.


// The entry point for our fragment shader.
void main()                    		
{                              
	// Multiply the color by the diffuse illumination level to get final output color.
    //gl_FragColor = v_Color;   
    //gl_FragColor = (v_Color * texture2D(u_Texture, v_TexCoordinate)); 
    gl_FragColor = texture2D(u_Texture, v_TexCoordinate);                          		
}                                                                     	