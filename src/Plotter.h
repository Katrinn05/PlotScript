#ifndef PLOTTER_H
#define PLOTTER_H

#include <vector>
#include <string>
#include <utility>

struct Color { unsigned char r, g, b; };

class Plotter {
public:
    
    Plotter(int width = 800, int height = 600);

    
    void setRange(double xmin, double xmax, double ymin, double ymax);

    
    void addSeries(const std::vector<std::pair<double,double>>& pts, bool connect = true);

    
    void save(const std::string& filename);

private:
    struct Series {
        std::vector<std::pair<double,double>> pts;
        bool connect;
        Color color;
    };

    int imgW, imgH;
    int marginL, marginR, marginT, marginB;
    std::vector<Series> seriesList;

    double xmin, xmax, ymin, ymax;
    bool hasRange;
    double dataMinX, dataMaxX, dataMinY, dataMaxY;

    std::vector<std::vector<Color>> pixels;

    
    void clearPixels();

    
    void setPixel(int x, int y, Color c);
    void drawLine(int x0, int y0, int x1, int y1, Color c);
    void drawPoint(int x, int y, Color c);

   
    void drawGrid();
    void drawAxes();
    void drawSeries();
    
    int toPxX(double x) const;
    int toPxY(double y) const;
    double niceStep(double range) const;

    
    void writeBMP(const std::string& fname);
};

#endif // PLOTTER_H
