#CMake最低版本号要求
cmake_minimum_required(VERSION 3.4.1)

#项目信息
project(smartupdate)

#添加bzip2子目录
add_subdirectory(bzip2)

#查找当前目录下的所有源文件，并将名称保存到DIR_SRCS变量中
aux_source_directory(. DIR_SRCS)
add_library(smartupdate SHARED ${DIR_SRCS})

#添加链接库log,bzip2
find_library(log-lib log)
target_link_libraries(smartupdate ${log-lib})
target_link_libraries(smartupdate bzip2)
