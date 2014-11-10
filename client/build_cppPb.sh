#!/bin/bash
#
# build file for C++ Client proto
#

project_base="/home/jaymit/netty"

# which protoc that you built
protoc_home="/home/jaymit/Downloads/protobuf-2.5.0"

#rm -r ${project_base}/client/comm.pb.h

${protoc_home}/src/protoc -I=${project_base}/resources --cpp_out=.  ${project_base}/resources/comm.proto
