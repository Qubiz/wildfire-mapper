package nl.dronexpert.wildfiremapper.utils;

import android.graphics.Color;

public class LinearGradient {

    private int[] colors;

    public LinearGradient(int[] colors) {
        this.colors = colors;
    }

    public int getColor(double value) {
        int numColors = colors.length;

        if (value >= 1) {
            return colors[colors.length - 1];
        } else if (value <= 0) {
            return colors[0];
        }

        double x = value * (numColors - 1);

        int startColor = Color.WHITE;
        int endColor = Color.WHITE;

        int i;

        for(i = 0; i < numColors; i++) {
            if(x >= i && x < i + 1) {
                startColor = colors[i];
                endColor = colors[i + 1];
                break;
            }
        }

        int startRed = Color.red(startColor);
        int startGreen = Color.green(startColor);
        int startBlue = Color.blue(startColor);

        int endRed = Color.red(endColor);
        int endGreen = Color.green(endColor);
        int endBlue = Color.blue(endColor);

        int red = (int) ((endRed - startRed) * (numColors > 2 ? (x - (numColors - 2)) : x) + startRed);
        int green = (int) ((endGreen - startGreen) * (numColors > 2 ? (x - (numColors - 2)) : x) + startGreen);
        int blue = (int) ((endBlue - startBlue) * (numColors > 2 ? (x - (numColors - 2)) : x) + startBlue);

        return Color.rgb(red, green, blue);
    }
}
