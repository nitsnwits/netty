#!/bin/bash
#
# creates the python classes for our .proto
#

#project_base="/Users/siddharthbhargava/Downloads/core-netty-4.2"
project_base="/Users/siddharthbhargava/Desktop/sid/SJSU/SJSU_academics/Semester2/275_DISTRI_Gash/275_workspace/netty"

rm ${project_base}/python/comm_pb2.py
PROTOC_HOME=/usr/local/Cellar/protobuf241/2.4.1
$PROTOC_HOME/bin/protoc -I=${project_base}/resources --python_out=. ${project_base}/resources/comm.proto 
