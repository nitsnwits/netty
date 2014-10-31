#!/bin/bash
#
# creates the python classes for our .proto
#

protoc_home="/usr/local/Cellar/protobuf/2.5.0"
project_base="/Users/neerajsharma/Downloads/core-netty-4.2"

rm ${project_base}/python/comm_pb2.py

protoc -I=${project_base}/resources --python_out=. ${project_base}/resources/comm.proto 
