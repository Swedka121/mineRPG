#version 460 core
out vec4 FragColor;

in vec2 texPos;
in float dark;

uniform sampler2D tex0;

void main() {
   vec4 texColor = texture(tex0, texPos);

   // Define the color to mix, normalizing RGB values to the [0.0, 1.0] range
   vec4 color = vec4(0 / 255.0, 0 / 255.0, 0 / 255.0, 1.0);

   // Blend the texture color with the specified color based on mixFactor
   FragColor = mix(texColor, color, dark);
}