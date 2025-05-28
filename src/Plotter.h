#ifndef PLOTTER_H
#define PLOTTER_H

#include <vector>
#include <string>
#include <utility>

// Simple RGB color representation (24-bit)
struct Color {
    unsigned char r, g, b;
};

// Plotter: renders 2D data series with grid, axes, labels, and exports as BMP
class Plotter {
public:
    // Constructor: set image dimensions (pixels)
    Plotter(int width = 800, int height = 600);

    // Optionally override automatic range detection
    void setRange(double x0, double x1, double y0, double y1);

    // Add a series of (x, y) points; connect=true draws lines between consecutive points
    void addSeries(const std::vector<std::pair<double, double>>& pts, bool connect = true);

    // Save the rendered plot to a BMP file (filename should end in ".bmp")
    void save(const std::string& filename);

private:
    // Internal struct for a data series
    struct Series {
        std::vector<std::pair<double, double>> pts;
        bool connect;
        Color color;
    };

    int imgW, imgH;                // Image width and height in pixels
    int marginL, marginR, marginT, marginB;  // Margins around the plot area

    double xmin, xmax, ymin, ymax; // Data range for axes
    bool hasRange;                 // True if range set manually
    double dataMinX, dataMaxX;     // Min/max data values (auto-range)
    double dataMinY, dataMaxY;

    std::vector<Series> seriesList;                // All added data series
    std::vector<std::vector<Color>> pixels;        // Pixel buffer [row][col]

    // Initialize pixel buffer to white background
    void clearPixels();

    // Low-level drawing primitives
    void setPixel(int x, int y, Color c);
    void drawLine(int x0, int y0, int x1, int y1, Color c);
    void drawPoint(int x, int y, Color c);

    // Coordinate transforms: data -> pixel
    int toPxX(double x) const;
    int toPxY(double y) const;

    // Compute "nice" step size for grid lines
    double niceStep(double range) const;

    // Render grid lines and axes
    void drawGrid();
    void drawAxes();

    // Render all series into pixel buffer
    // Draw each series; connected series use Catmull-Rom spline interpolation for smooth curves
    void drawSeries();

    // Draw single character and string (using 5x7 bitmap font)
    void drawChar(int x, int y, char c, Color col);
    void drawString(int x, int y, const std::string &s, Color col);

    // Draw numeric labels along X and Y axes
    void drawLabels();

    // Write pixel buffer to BMP file on disk
    void writeBMP(const std::string& fname);
};

#endif // PLOTTER_H

