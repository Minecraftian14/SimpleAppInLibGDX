#ifdef GL_ES
precision highp float;
#endif

const int MAX_SIZE = 32;

uniform sampler2D sceneTex;
uniform float centersx[MAX_SIZE];
uniform float centersy[MAX_SIZE];
uniform float progresses[MAX_SIZE];
uniform int howMany;
uniform float viewWidth;
uniform float viewHeight;
//uniform vec3 shockParams; // 10.0, 0.8, 0.1

varying vec2 v_texCoords;

vec2 calculateVector(float centerx, float centery, float progress) {

    vec2 distortion = vec2(0, 0);
    vec3 shockParams = vec3(10.0, 0.8, 0.1);

    float CurrentTime = progress;

    vec2 scale = vec2(viewWidth / viewHeight, 1);

    vec2 center = vec2(centerx, centery) * scale;
    vec2 n_texCoords = v_texCoords * scale;

    float dist = distance(n_texCoords, center);

    if ((dist <= (CurrentTime + shockParams.z)) && (dist >= (CurrentTime - shockParams.z))) {
        float diff = dist - CurrentTime;
        float powDiff = 0.0;
        if (dist > 0.05) {
            powDiff = 1.0 - pow(abs(diff * shockParams.x), shockParams.y);
        }
        float diffTime = diff * powDiff;
        vec2 diffUV = normalize(n_texCoords - center);
        //Perform the distortion and reduce the effect over time
        distortion = (diffUV * diffTime) / (CurrentTime * dist * 40.0);
    }
    return distortion;
}

void main() {
    vec2 l_texCoords = v_texCoords;
    int size = min(howMany, MAX_SIZE);

    for (int i = 0; i < size; ++i)
    l_texCoords = l_texCoords + calculateVector(centersx[i], centersy[i], progresses[i]);

    gl_FragColor = texture2D(sceneTex, l_texCoords);
}