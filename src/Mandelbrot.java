import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Objects;

import javax.swing.*;
        import java.awt.*;
        import java.awt.event.*;
        import java.util.Objects;
import java.util.stream.IntStream;

import static java.lang.Math.*;

public class Mandelbrot extends JPanel {

    private static final int WIDTH = 1000;  // Screen width (in pixels)
    private static final int HEIGHT = 1000; // Screen height (in pixels)
    private static final int CELL_SIZE = 1; // Each cell (pixel) is now 10x10 pixels
    private static double X_MIN = -2.00;
    private static double X_MAX = 0.47;
    private static double Y_MIN = -1.12;
    private static double Y_MAX = 1.12;
    private static final double SCALE = 1;
    private static final double SHIFT_X = 0;
    private static final double SHIFT_Y = 0;
    // cool set (.001., -.55, .56)
    private static final int MAX_ITERATIONS = 1000;
    private static final int START_X = WIDTH/2;
    private static final int START_Y = HEIGHT/2;
    private final int[][] iterationCounts = new int[WIDTH][HEIGHT];
    private final Color[][] screen = new Color[WIDTH][HEIGHT];

    private static final Color[] palette = new Color[] {
            new Color(  0,   7, 100),
            new Color( 32, 107, 203),
            new Color(237, 255, 255),
            new Color(255, 170,   0),
            new Color(  0,   2,   0),
    };


    // Constructor initializes the grid
    public Mandelbrot() {
        X_MIN *= SCALE;
        X_MIN += SHIFT_X;
        X_MAX *= SCALE;
        X_MAX += SHIFT_X;
        Y_MIN *= SCALE;
        Y_MIN += SHIFT_Y;
        Y_MAX *= SCALE;
        Y_MAX += SHIFT_Y;
        double x_step = abs(X_MIN - X_MAX) / WIDTH;
        double y_step = abs(Y_MIN - Y_MAX) / HEIGHT;

        int[] numIterationsPerPixel = new int[MAX_ITERATIONS];
        Color[] colors = mandelbrotGradient();

        // calculate iterations for each point in mandlebrot set
        for (int i = 0; i < WIDTH - 1; i++) {
            for (int j = 0; j < HEIGHT - 1; j++) {
                double x0 = X_MIN + x_step * i;
                double y0 = Y_MIN + y_step * j;
                double x = 0;
                double y = 0;
                double x2 = 0;
                double y2 = 0;
                double iteration = 0;
                while (((x2 + y2) <= (2)) && iteration < MAX_ITERATIONS) {
                    y = 2 * x * y + y0;
                    x = x2 - y2 + x0;
                    x2 = x * x;
                    y2 = y * y;
                    iteration++;
                }

                if (iteration < MAX_ITERATIONS) {
                    double logZn = log(x*x + y*y) / 2;
                    double nu = log(logZn / log(2)) / log(2);
                    iteration = iteration + 1 - nu;
                }

                if (iteration < MAX_ITERATIONS) {
                    Color color1 = colors[(int) floor(iteration)];
                    Color color2 = colors[(int) floor(iteration) + 1];
//                    screen[i][j] =
                    screen[i][j] = colors[(int) floor(iteration)];
                } else {
                    screen[i][j] = Color.BLACK;
                }



            }
        }

    }



    public Color[] mandelbrotGradient() {
        int steps = 1000;
        Color[] colors = new Color[steps];

        for (int i = 0; i < 160; i++) {
            colors[i] = lerpColor(0, 160, palette[0], palette[1], i);
        }
        for (int i = 160; i < 420; i++) {
            colors[i] = lerpColor(160, 420, palette[1], palette[2], i);
        }
        for (int i = 420; i < 643; i++) {
            colors[i] = lerpColor(420, 643, palette[2], palette[3], i);
        }
        for (int i = 643; i < 858; i++) {
            colors[i] = lerpColor(643, 858, palette[3], palette[4], i);
        }
        for (int i = 858; i < 1000; i++) {
            colors[i] = lerpColor(858, 1000, palette[4], palette[0], i);
        }

        return colors;
    }

    public Color lerpColor(int x1, int x2, Color color1, Color color2, int x) {
        float newRed = lerp(x1, x2, color1.getRed(), color2.getRed(), x);
        float newGreen = lerp(x1, x2, color1.getGreen(), color2.getGreen(), x);
        float newBlue = lerp(x1, x2, color1.getBlue(), color2.getBlue(), x);
        return new Color((int) floor(newRed), (int) floor(newGreen), (int) floor(newBlue));
    }

    public float lerp(int x1, int x2, int y1, int y2, int x) {
        return y1 + (float) ((x - x1) * (y2 - y1)) / (x2 - x1);
    }


// Overriding paintComponent to draw the updated screen
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                g.setColor(screen[i][j]);
                g.fillRect(i * CELL_SIZE, j * CELL_SIZE, CELL_SIZE, CELL_SIZE); // Draw each zoomed-in pixel
            }
        }
    }

    // Main method to run the simulation
    public static void main(String[] args) {
        JFrame frame = new JFrame("Mandelbrot");
        Mandelbrot panel = new Mandelbrot();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(WIDTH * CELL_SIZE + (CELL_SIZE * 10), HEIGHT * CELL_SIZE + (CELL_SIZE * 10)); // Adjust window size for zoomed-in pixels
        frame.add(panel);  // Add the custom panel to the frame
        frame.setVisible(true); // Make the frame visible
    }
}
