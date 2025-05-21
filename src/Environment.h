#ifndef PLOTSCRIPT_ENVIRONMENT_H
#define PLOTSCRIPT_ENVIRONMENT_H

#include <string>
#include <vector>
#include <unordered_map>
#include <stdexcept>

// name-value table for variables
class Environment {
public:
    using Value = std::vector<double>;

    // assign or update variable
    void set(const std::string& name, const Value& value);

    // retrieve variable; throws if undefined
    const Value& get(const std::string& name) const;

    // check existence
    bool contains(const std::string& name) const;

    // remove variable if exists
    void remove(const std::string& name);

    // clear all entries
    void clear();

private:
    std::unordered_map<std::string, Value> table_;
};

#endif