cmake_minimum_required(VERSION 4.1)
project(rtstructuresJsonGenerator)

set(CMAKE_CXX_STANDARD 17)

add_executable(RTStructuresJsonGenerator main.cpp)

target_link_options(RTStructuresJsonGenerator PRIVATE
        -static
        -static-libgcc
        -static-libstdc++
)