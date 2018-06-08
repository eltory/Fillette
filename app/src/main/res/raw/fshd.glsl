/*
   Copyright 2012 Harri Smatt

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

precision mediump float;

uniform sampler2D sTexture;

uniform float uBrightness;
uniform float uContrast;
uniform float uSaturation;
uniform float uCornerRadius;

varying vec2 vTextureCoord;

vec3 brightness(vec3 color, float brightness) {
    float scaled = brightness / 2.0;
    if (scaled < 0.0) {
        return color * (1.0 + scaled);
    } else {
        return color + ((1.0 - color) * scaled);
    }
}

vec3 contrast(vec3 color, float contrast) {
    const float PI = 3.14159265;
    return min(vec3(1.0), ((color - 0.5) * (tan((contrast + 1.0) * PI / 4.0) ) + 0.5));
}

vec3 overlay(vec3 overlayComponent, vec3 underlayComponent, float alpha) {
    vec3 underlay = underlayComponent * alpha;
    return underlay * (underlay + (2.0 * overlayComponent * (1.0 - underlay)));
}

vec3 multiplyWithAlpha(vec3 overlayComponent, float alpha, vec3 underlayComponent) {
    return underlayComponent * overlayComponent * alpha;
}

vec3 screenPixelComponent(vec3 maskPixelComponent, float alpha, vec3 imagePixelComponent) {
    return 1.0 - (1.0 - (maskPixelComponent * alpha)) * (1.0 - imagePixelComponent);
}

vec3 rgbToHsv(vec3 color) {
    vec3 hsv;

    float mmin = min(color.r, min(color.g, color.b));
    float mmax = max(color.r, max(color.g, color.b));
    float delta = mmax - mmin;

    hsv.z = mmax;
    hsv.y = delta / mmax;

    if (color.r == mmax) {
        hsv.x = (color.g - color.b) / delta;
    } else if (color.g == mmax) {
        hsv.x = 2.0 + (color.b - color.r) / delta;
    } else {
        hsv.x = 4.0 + (color.r - color.g) / delta;
    }

    hsv.x *= 0.166667;
    if (hsv.x < 0.0) {
        hsv.x += 1.0;
    }

    return hsv;
}

vec3 hsvToRgb(vec3 hsv) {
    if (hsv.y == 0.0) {
        return vec3(hsv.z);
    } else {
        float i;
        float aa, bb, cc, f;

        float h = hsv.x;
        float s = hsv.y;
        float b = hsv.z;

        if (h == 1.0) {
            h = 0.0;
        }

        h *= 6.0;
        i = floor(h);
        f = h - i;
        aa = b * (1.0 - s);
        bb = b * (1.0 - (s * f));
        cc = b * (1.0 - (s * (1.0 - f)));

        if (i == 0.0) return vec3(b, cc, aa);
        if (i == 1.0) return vec3(bb, b, aa);
        if (i == 2.0) return vec3(aa, b, cc);
        if (i == 3.0) return vec3(aa, bb, b);
        if (i == 4.0) return vec3(cc, aa, b);
        if (i == 5.0) return vec3(b, aa, bb);
    }
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

void main() {
    vec3 color = texture2D(sTexture, vTextureCoord).rgb;

    //color = filter(color, sTexture, vTextureCoord);

    // Calculate brightness, contrast and saturation.
    color = brightness(color, uBrightness);
    color = contrast(color, uContrast);
    color = saturation(color, uSaturation);

    // Calculate darkened corners.
    const float sqrt2 = 1.414213562373;
    float len = distance(vTextureCoord, vec2(0.5)) * sqrt2;
    len = smoothstep(1.0 - uCornerRadius, 1.0, len);
    color *= mix(0.5, 1.0, 1.0 - len);

    gl_FragColor = vec4(color, 1.0);
}
