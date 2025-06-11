#include "Environment.h"
#include "Errors.h"

void Environment::set(const std::string& name, const Value& value) {
    table_[name] = value;
}

const Environment::Value& Environment::get(const std::string& name) const {
    auto it = table_.find(name);
    if (it == table_.end()) {
        throw SemanticError("Undefined variable: " + name);
    }
    return it->second;
}

bool Environment::contains(const std::string& name) const {
    return table_.find(name) != table_.end();
}

void Environment::remove(const std::string& name) {
    table_.erase(name);
}

void Environment::clear() {
    table_.clear();
}
