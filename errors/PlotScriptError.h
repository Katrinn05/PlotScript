#pragma once
#include <stdexcept>
#include <sstream>
#include <string>

class PlotScriptError : public std::runtime_error {
    std::string file_;
    int line_, column_;
    mutable std::string cached_;
public:
    PlotScriptError(std::string  msg,
                    std::string  file = "",
                    int          line = -1,
                    int          col  = -1)
        : std::runtime_error(std::move(msg)),
          file_(std::move(file)), line_(line), column_(col) {}

    const char* what() const noexcept override {
        if (cached_.empty()) {
            std::ostringstream os;
            if (!file_.empty())     os << file_;
            if (line_   >= 0)       os << ':' << line_;
            if (column_ >= 0)       os << ':' << column_;
            if (!file_.empty() || line_ >= 0) os << ": ";
            os << std::runtime_error::what();
            cached_ = os.str();
        }
        return cached_.c_str();
    }
};
