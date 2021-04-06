precision mediump float;
varying vec2 textureCoordinate;
uniform samplerExternalOES vTexture;
void main() {
    gl_FragColor = texture2D( vTexture, textureCoordinate );
}
