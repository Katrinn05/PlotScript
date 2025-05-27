#include "Plotter.h"
#include <fstream>
#include <cmath>
#include <algorithm>

Plotter::Plotter(int width, int height)
    : imgW(width), imgH(height), hasRange(false) {
    marginL = 50; marginR = 20;
    marginT = 20; marginB = 50;
    clearPixels();
}

void Plotter::clearPixels() {
    pixels.assign(imgH, std::vector<Color>(imgW, {255,255,255}));
}

void Plotter::setRange(double x0, double x1, double y0, double y1) {
    xmin = x0; xmax = x1; ymin = y0; ymax = y1;
    hasRange = true;
}

void Plotter::addSeries(const std::vector<std::pair<double,double>>& pts, bool connect) {
    Series s; s.pts = pts; s.connect = connect;
    static const Color pal[] = {{255,0,0},{0,128,0},{0,0,255},{255,165,0},{255,0,255},{0,255,255},{128,0,128},{0,0,0}};
    s.color = pal[seriesList.size() % (sizeof(pal)/sizeof(pal[0]))];
    for (size_t i = 0; i < pts.size(); ++i) {
        auto &p = pts[i];
        if (seriesList.empty() && i == 0) {
            dataMinX = dataMaxX = p.first;
            dataMinY = dataMaxY = p.second;
        } else {
            dataMinX = std::min(dataMinX, p.first);
            dataMaxX = std::max(dataMaxX, p.first);
            dataMinY = std::min(dataMinY, p.second);
            dataMaxY = std::max(dataMaxY, p.second);
        }
    }
    seriesList.push_back(s);
}

int Plotter::toPxX(double x) const {
    double w = imgW - marginL - marginR;
    return int(std::round(marginL + (x - xmin) / (xmax - xmin) * w));
}
int Plotter::toPxY(double y) const {
    double h = imgH - marginT - marginB;
    return int(std::round(marginT + (1.0 - (y - ymin) / (ymax - ymin)) * h));
}

double Plotter::niceStep(double range) const {
    double raw = range / 10.0;
    double expv = std::pow(10.0, std::floor(std::log10(raw)));
    double f = raw / expv;
    double nice;
    if (f < 1.5) nice = 1;
    else if (f < 3) nice = 2;
    else if (f < 7) nice = 5;
    else nice = 10;
    return nice * expv;
}

void Plotter::setPixel(int x, int y, Color c) {
    if (x >= 0 && x < imgW && y >= 0 && y < imgH)
        pixels[y][x] = c;
}

void Plotter::drawLine(int x0, int y0, int x1, int y1, Color c) {
    int dx = std::abs(x1 - x0), sx = x0 < x1 ? 1 : -1;
    int dy = -std::abs(y1 - y0), sy = y0 < y1 ? 1 : -1;
    int err = dx + dy;
    while (true) {
        setPixel(x0, y0, c);
        if (x0 == x1 && y0 == y1) break;
        int e2 = 2 * err;
        if (e2 >= dy) { err += dy; x0 += sx; }
        if (e2 <= dx) { err += dx; y0 += sy; }
    }
}

void Plotter::drawPoint(int x, int y, Color c) {
    for (int dy = -1; dy <= 1; ++dy) for (int dx = -1; dx <= 1; ++dx) setPixel(x+dx, y+dy, c);
}

void Plotter::drawGrid() {
    Color gc{200,200,200};
    double xr = xmax - xmin, yr = ymax - ymin;
    double xs = niceStep(xr), ys = niceStep(yr);
    double fx = std::ceil(xmin/xs)*xs;
    for (double xv = fx; xv <= xmax; xv += xs) {
        int px = toPxX(xv);
        drawLine(px, marginT, px, imgH - marginB, gc);
    }
    double fy = std::ceil(ymin/ys)*ys;
    for (double yv = fy; yv <= ymax; yv += ys) {
        int py = toPxY(yv);
        drawLine(marginL, py, imgW - marginR, py, gc);
    }
}

void Plotter::drawAxes() {
    Color ac{0,0,0};
    if (ymin <= 0 && ymax >= 0) {
        int py = toPxY(0);
        drawLine(marginL, py, imgW - marginR, py, ac);
    }
    if (xmin <= 0 && xmax >= 0) {
        int px = toPxX(0);
        drawLine(px, marginT, px, imgH - marginB, ac);
    }
}

void Plotter::drawSeries() {
    for (auto &s : seriesList) {
        for (size_t i = 0; i < s.pts.size(); ++i) {
            int px = toPxX(s.pts[i].first), py = toPxY(s.pts[i].second);
            if (!s.connect) drawPoint(px, py, s.color);
            else if (i > 0) {
                int px0 = toPxX(s.pts[i-1].first), py0 = toPxY(s.pts[i-1].second);
                drawLine(px0, py0, px, py, s.color);
            }
        }
    }
}

static const uint8_t font5x7[][7] = {
    {0x0E,0x11,0x11,0x11,0x0E,0x00,0x00}, // 0
    {0x04,0x0C,0x04,0x04,0x0E,0x00,0x00}, // 1
    {0x0E,0x11,0x02,0x04,0x1F,0x00,0x00}, // 2
    {0x1F,0x02,0x04,0x02,0x1F,0x00,0x00}, // 3
    {0x02,0x06,0x0A,0x1F,0x02,0x00,0x00}, // 4
    {0x1F,0x10,0x1E,0x01,0x1E,0x00,0x00}, // 5
    {0x0E,0x10,0x1E,0x11,0x0E,0x00,0x00}, // 6
    {0x1F,0x01,0x02,0x04,0x08,0x00,0x00}, // 7
    {0x0E,0x11,0x0E,0x11,0x0E,0x00,0x00}, // 8
    {0x0E,0x11,0x0F,0x01,0x0E,0x00,0x00}, // 9
    {0x00,0x00,0x0E,0x00,0x00,0x00,0x00}, // -
    {0x00,0x00,0x00,0x00,0x06,0x06,0x00}  // .
};

void Plotter::drawChar(int x, int y, char c, Color col) {
    int idx = -1;
    if (c >= '0' && c <= '9') idx = c - '0';
    else if (c == '-') idx = 10;
    else if (c == '.') idx = 11;
    if (idx < 0) return;
    for (int row = 0; row < 7; ++row) {
        for (int colb = 0; colb < 5; ++colb) {
            if (font5x7[idx][row] & (1 << (4 - colb))) {
                setPixel(x + colb, y + row, col);
            }
        }
    }
}

void Plotter::drawString(int x, int y, const std::string &s, Color col) {
    int px = x;
    for (char c : s) {
        drawChar(px, y, c, col);
        px += 6;
    }
}

void Plotter::drawLabels() {
    Color tc{0,0,0};
    double xr = xmax - xmin;
    double xs = niceStep(xr);
    double fx = std::ceil(xmin/xs)*xs;
    for (double xv = fx; xv <= xmax; xv += xs) {
        int px = toPxX(xv);
        std::string t = std::to_string(xv);
        t.erase(t.find_last_not_of('0') + 1, std::string::npos);
        if (t.back() == '.') t.pop_back();
        drawString(px - int(t.size()*6/2), imgH - marginB + 2, t, tc);
    }
    double yr = ymax - ymin;
    double ys = niceStep(yr);
    double fy = std::ceil(ymin/ys)*ys;
    for (double yv = fy; yv <= ymax; yv += ys) {
        int py = toPxY(yv);
        std::string t = std::to_string(yv);
        t.erase(t.find_last_not_of('0') + 1, std::string::npos);
        if (t.back() == '.') t.pop_back();
        drawString(marginL - int(t.size()*6) - 2, py - 3, t, tc);
    }
}

#pragma pack(push,1)
struct BMPFileHeader { uint16_t bfType{0x4D42}; uint32_t bfSize; uint16_t bfReserved1{0}, bfReserved2{0}; uint32_t bfOffBits{54}; };
struct BMPInfoHeader { uint32_t biSize{40}; int32_t biWidth; int32_t biHeight; uint16_t biPlanes{1}; uint16_t biBitCount{24}; uint32_t biCompression{0}; uint32_t biSizeImage; int32_t biXPelsPerMeter{0}, biYPelsPerMeter{0}; uint32_t biClrUsed{0}, biClrImportant{0}; };
#pragma pack(pop)

void Plotter::writeBMP(const std::string& fname) {
    int rowBytes = imgW*3;
    int padding = (4 - rowBytes%4)%4;
    int imgSize = (rowBytes + padding)*imgH;
    BMPFileHeader fh; fh.bfSize = fh.bfOffBits + imgSize;
    BMPInfoHeader ih; ih.biWidth = imgW; ih.biHeight = imgH; ih.biSizeImage = imgSize;
    std::ofstream ofs(fname, std::ios::binary);
    ofs.write((char*)&fh, sizeof(fh)); ofs.write((char*)&ih, sizeof(ih));
    for (int y = imgH-1; y >= 0; --y) {
        for (int x = 0; x < imgW; ++x) {
            Color &c = pixels[y][x]; ofs.put(c.b).put(c.g).put(c.r);
        }
        for (int p = 0; p < padding; ++p) ofs.put(0);
    }
}

void Plotter::save(const std::string& filename) {
    if (!hasRange) {
        if (seriesList.empty()) { xmin=-1; xmax=1; ymin=-1; ymax=1; }
        else { xmin=dataMinX; xmax=dataMaxX; ymin=dataMinY; ymax=dataMaxY; }
    }
    double dx = xmax-xmin, dy = ymax-ymin;
    xmin -= 0.1*dx; xmax += 0.1*dx; ymin -= 0.1*dy; ymax += 0.1*dy;
    clearPixels(); drawGrid(); drawAxes(); drawSeries(); drawLabels();
    writeBMP(filename);
}

