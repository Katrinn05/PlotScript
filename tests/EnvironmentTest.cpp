#include "Environment.h"
#include <gtest/gtest.h>

TEST(EnvironmentTests, SetGetContains) {
    Environment env;
    EXPECT_FALSE(env.contains("foo"));

    std::vector<double> v1 = {1.23, 4.56};
    env.set("foo", v1);

    EXPECT_TRUE(env.contains("foo"));
    const auto& got = env.get("foo");
    ASSERT_EQ(got.size(), 2u);
    EXPECT_DOUBLE_EQ(got[0], 1.23);
    EXPECT_DOUBLE_EQ(got[1], 4.56);
}

TEST(EnvironmentTests, GetUndefinedThrows) {
    Environment env;
    EXPECT_FALSE(env.contains("missing"));
    EXPECT_THROW(env.get("missing"), std::runtime_error);
}

TEST(EnvironmentTests, RemoveVariable) {
    Environment env;
    env.set("x", {7.0});
    EXPECT_TRUE(env.contains("x"));

    env.remove("x");
    EXPECT_FALSE(env.contains("x"));
    EXPECT_THROW(env.get("x"), std::runtime_error);
}

TEST(EnvironmentTests, ClearAll) {
    Environment env;
    env.set("a", {1.0});
    env.set("b", {2.0});
    EXPECT_TRUE(env.contains("a"));
    EXPECT_TRUE(env.contains("b"));

    env.clear();
    EXPECT_FALSE(env.contains("a"));
    EXPECT_FALSE(env.contains("b"));
    EXPECT_THROW(env.get("a"), std::runtime_error);
    EXPECT_THROW(env.get("b"), std::runtime_error);
}
