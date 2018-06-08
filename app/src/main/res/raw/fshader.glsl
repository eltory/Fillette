#extension GL_OES_EGL_image_external : require

#ifdef GL_FRAGMENT_PRECISION_PHIGH
precision highp float;
#else
precision mediump float;
#endif

uniform float time;
uniform int pointerCount;
uniform vec3 pointers[10];
uniform vec2 resolution;

uniform samplerExternalOES sTexture;
//uniform sampler2D sTexture;
varying vec2 vTextureCoord;
uniform float uBrightness;
uniform float uContrast;
uniform float uSaturation;
uniform float uCornerRadius;

uniform vec3 iResolution;
uniform float iGlobalTime;
uniform float uRed;
uniform float uGreen;
uniform float uBlue;
uniform float uAlpha;
uniform float dX;

vec3 brightness(vec3 color, float brightness) {
    float scaled = brightness / 2.0;
    
    if(brightness < 0.0){
        return color / (1.0 - brightness);
    }else{
        return color * (1.0 + brightness);
    }
}

vec3 contrast(vec3 color, float contrast) {
    const float PI = 3.14159265;
    return min(vec3(1.0), ((color - 0.5) * (tan((contrast + 1.0) * PI / 4.0) ) + 0.5));
}

vec3 saturation(vec3 color, float sat) {
    const float lumaR = 0.212671;
    const float lumaG = 0.715160;
    const float lumaB = 0.072169;
    
    float v = sat + 1.0;
    float i = 1.0 - v;
    float r = i * lumaR;
    float g = i * lumaG;
    float b = i * lumaB;
    
    mat3 mat = mat3(r + v, r, r, g, g + v, g, b, b, b + v);
    
    return mat * color;
}

vec4 filt(float r, float g, float b, float a){
    vec4 color = vec4(r, g, b, 1.0);
    color.r = r;
    color.g = g;
    color.b = b;
    color *= a;
    return color;
}

vec3 threeCol(vec2 fragCoord)
{
    vec2 uv = fragCoord.xy;

	float amount = 0.0;

	amount = (1.0 + sin(iGlobalTime*6.0)) * 0.5;
	amount *= 1.0 + sin(iGlobalTime*16.0) * 0.5;
	amount *= 1.0 + sin(iGlobalTime*19.0) * 0.5;
	amount *= 1.0 + sin(iGlobalTime*27.0) * 0.5;
	amount = pow(amount, 3.0);

	amount *= 0.05;

    vec3 col;
    col.r = texture2D( sTexture, vec2(uv.x,uv.y+dX) ).r;
    col.g = texture2D( sTexture, uv ).g;
    col.b = texture2D( sTexture, vec2(uv.x,uv.y-dX) ).b;

	col *= (1.0 - amount * 0.5);

    return col;
}
vec3 mosaic(vec2 position){
	vec2 p = floor(position)/8.;
	return texture2D(sTexture, p).rgb;
}

vec2 sw(vec2 p) {return vec2( floor(p.x) , floor(p.y) );}
vec2 se(vec2 p) {return vec2( ceil(p.x) , floor(p.y) );}
vec2 nw(vec2 p) {return vec2( floor(p.x) , ceil(p.y) );}
vec2 ne(vec2 p) {return vec2( ceil(p.x) , ceil(p.y) );}

vec3 glass(vec2 p) {
	vec2 inter = smoothstep(0., 1., fract(p));
	vec3 s = mix(mosaic(sw(p)), mosaic(se(p)), inter.x);
	vec3 n = mix(mosaic(nw(p)), mosaic(ne(p)), inter.x);
	return mix(s, n, inter.y);
}
float gamma = 1.2;

vec3 linearToneMapping(vec3 color)
{
	float exposure = 1.;
	color = clamp(exposure * color, 0., 1.);
	color = pow(color, vec3(1. / gamma));
	return color;
}

vec3 simpleReinhardToneMapping(vec3 color)
{
	float exposure = 1.5;
	color *= exposure/(1. + color / exposure);
	color = pow(color, vec3(1. / gamma));
	return color;
}

vec3 lumaBasedReinhardToneMapping(vec3 color)
{
	float luma = dot(color, vec3(0.2126, 0.7152, 0.0722));
	float toneMappedLuma = luma / (1. + luma);
	color *= toneMappedLuma / luma;
	color = pow(color, vec3(1. / gamma));
	return color;
}

vec3 whitePreservingLumaBasedReinhardToneMapping(vec3 color)
{
	float white = 2.;
	float luma = dot(color, vec3(0.2126, 0.7152, 0.0722));
	float toneMappedLuma = luma * (1. + luma / (white*white)) / (1. + luma);
	color *= toneMappedLuma / luma;
	color = pow(color, vec3(1. / gamma));
	return color;
}

vec3 RomBinDaHouseToneMapping(vec3 color)
{
    color = exp( -1.0 / ( 2.72*color + 0.15 ) );
	color = pow(color, vec3(1. / gamma));
	return color;
}

vec3 filmicToneMapping(vec3 color)
{
	color = max(vec3(0.), color - vec3(0.004));
	color = (color * (6.2 * color + .5)) / (color * (6.2 * color + 1.7) + 0.06);
	return color;
}

vec3 Uncharted2ToneMapping(vec3 color)
{
	float A = 0.15;
	float B = 0.50;
	float C = 0.10;
	float D = 0.20;
	float E = 0.02;
	float F = 0.30;
	float W = 11.2;
	float exposure = 2.;
	color *= exposure;
	color = ((color * (A * color + C * B) + D * E) / (color * (A * color + B) + D * F)) - E / F;
	float white = ((W * (A * W + C * B) + D * E) / (W * (A * W + B) + D * F)) - E / F;
	color /= white;
	color = pow(color, vec3(1. / gamma));
	return color;
}

vec3 rgb2hsv(vec3 c)
{
    vec4 K = vec4(0.0, -1.0 / 3.0, 2.0 / 3.0, -1.0);
    vec4 p = mix(vec4(c.bg, K.wz), vec4(c.gb, K.xy), step(c.b, c.g));
    vec4 q = mix(vec4(p.xyw, c.r), vec4(c.r, p.yzx), step(p.x, c.r));

    float d = q.x - min(q.w, q.y);
    float e = 1.0e-10;
    return vec3(abs(q.z + (q.w - q.y) / (6.0 * d + e)), d / (q.x + e), q.x);
}

vec3 hsv2rgb(vec3 c)
{
    vec4 K = vec4(1.0, 2.0 / 3.0, 1.0 / 3.0, 3.0);
    vec3 p = abs(fract(c.xxx + K.xyz) * 6.0 - K.www);
    return c.z * mix(K.xxx, clamp(p - K.xxx, 0.0, 1.0), c.y);
}

void main(void){
    vec2 uv = vTextureCoord.xy;
vec4 color = texture2D(sTexture, vTextureCoord);

    vec3 hsv = rgb2hsv(color.rgb);

    float intensity = hsv.z; // the third component holds the brightness

    float log_factor = log(intensity + 1.0);

    log_factor = exp(log_factor) - 1.0;

    hsv.z = log_factor;

    color.rgb = hsv2rgb(hsv);


    vec3 col;
    col.r = texture2D(sTexture, uv).r;
    col.g = texture2D(sTexture, uv).g;
    col.b = texture2D(sTexture, uv).b;
    col =  threeCol(vTextureCoord);

    col = brightness(col, uBrightness);
    col = contrast(col, uContrast);
    col = saturation(col, uSaturation);

//col = Uncharted2ToneMapping(col);
    const float sqrt2 = 1.414213562373;
        float len = distance(vTextureCoord, vec2(0.5)) * sqrt2;
        len = smoothstep(1.0 - uCornerRadius, 1.0, len);
        col *= mix(0.5, 1.0, 1.0 - len);

    vec4 fil = filt(uRed, uGreen, uBlue, uAlpha);
//col = glass(vTextureCoord*80);
    gl_FragColor = vec4(col, 1.0);
    gl_FragColor += fil;
    //gl_FragColor = color;

}