// 矩形框畸变效果着色器
precision mediump float;

//varying vec2 vTextureCoord;
varying vec2 v_TexCoordinate;   // Interpolated texture coordinate per fragment.

//uniform sampler2D sTexture;
uniform sampler2D u_Texture;    // The input texture.

// 桶形畸变
vec2 brownConradyDistortion(vec2 uv)
{
    // 左右视窗缩小倍数设置
    float demoScale = 1.0;
    uv *= demoScale;
    // positive values of K1 give barrel distortion, negative give pincushion（图像畸变程度设置）
    float barrelDistortion1 = 0.08; // K1 in text books
    float barrelDistortion2 = 0.0; // K2 in text books
    float r2 = uv.x*uv.x + uv.y*uv.y;
    uv *= 1.0 + barrelDistortion1 * r2 + barrelDistortion2 * r2 * r2;

    return uv;
}

void main() 
{   
    // uv -> device-coordinate
    // 坐标范围（-1，+1）
    vec2 uv = v_TexCoordinate;
    uv = uv * 2.0 - 1.0;

    // barrel distortion
    uv = brownConradyDistortion(uv);

    //  device-coordinate -> uv
    uv = 0.5 * (uv * 1.0 + 1.0);

    // 
    vec4 color;
    if(uv.x>1.0||uv.y>0.93||uv.x<0.0||uv.y<0.07){
            color = vec4(0.0,0.0,0.0,1.0); // 超出显示范围，设颜色为黑。
    }else{
        color = texture2D(u_Texture, uv);
    }

    gl_FragColor = color;
}