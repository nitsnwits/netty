#
#
#	Client Class for Python Client
#		This class will handle creating initiating requests to servers
#
#


from SocketChannel import SocketChannel, SocketChannelFactory
import comm_pb2
from comm_pb2 import Request
import struct, random, sys, os

class Client():
	def __init__(self):
		self.channelFactory = SocketChannelFactory()
  
	def executeOperation(self, option, host, port, param):
		if option == "photoCreateRequest":
			#Create a request for sending an image to server
			request = self.preparePhotoCreateRequest(param)
			print "Preparing POST /photo request for server (" + str(host) + ":" + str(port) +")"
			response = self.send(host, port, request)
			self.printPhotoCreateRequest(response)
		elif option == "photoReadRequest":
			#Create a request for retreiving an image from server
			request = self.preparePhotoReadRequest(param)
			print "Preparing GET /photo request for server (" + str(host) + ":" + str(port) + ")"
			response = self.send(host, port, request)
			self.printPhotoReadRequest(response)

	def preparePhotoCreateRequest(self, param):
		request = Request()

		# set headers for the request
		header = request.header
		header.routing_id = comm_pb2.Header.JOBS
		# TODO: get system user id to put as originator, because originator is an int (should be string), should we?
		header.originator = 1 #1 for python client, 2 for cpp, 3 for java
		#header.tag = any string
		#header.time = epoch system time
		header.photoHeader.requestType = comm_pb2.PhotoHeader.write
		#header.photoHeader.lastModified
		#header.photoHeader.contentLength

		# set body for the request
		body = request.body
		photoPayload = body.photoPayload
		photoPayload.name = os.path.basename(param)
		# parse the file into a byte array
		with open(param, 'rb') as image:
			readImage = image.read()
			readImageByteArray = bytearray(readImage)
		print "req: " + str(request)
		photoPayload.data = str(readImageByteArray)

		#finish preparing the request
		return request

	def preparePhotoReadRequest(self, param):
		request = Request()

		#set headers for the request
		header = request.header
		header.routing_id = comm_pb2.Header.JOBS
		header.originator = 1

		#set body for the request
		body = request.body
		photoPayload = body.photoPayload
		photoPayload.uuid = str(param)

		#finish preparing the request
		return request


	def printPhotoReadRequest(self, response):
		print "\n***** Response received from server *****\n"
		print "\t RoutingID \t-> " + str(response.header.routing_id)
		print "\t Originator \t->" + str(response.header.routing_id)
		print "\t Data \t->" + str(response.body.photoPayload.data)

	def printPhotoCreateRequest(self, response):
		print "\n***** Response received from server *****\n"
		print "\t RoutingID \t-> " + str(response.header.routing_id)
		print "\t Originator \t->" + str(response.header.routing_id)
		print "\t Photo UUID \t->" + str(response.body.photoPayload.uuid)		
  
  # def printPingRequest(self, resp):
	 #  print "\n==Response Received from Server==\n"
	 #  print "RoutingID - " + str(resp.header.routing_id)
	 #  print "Originator - " + str(resp.header.originator)
	 #  print "Ping Number - " + str(resp.body.ping.number)
	 #  print "Ping Tag - " + str(resp.body.ping.tag)

					  
	def send(self, host, port, request):
		self.channel = self.channelFactory.openChannel(host, port)
		while self.channel.connected:
			print "Channel Connected..."
			try:
				self.channel.write(request.SerializeToString())
				resp = Request()
				print "Resp: " + str(resp)
				resp = ParseFromString(self.channel.read())
				return resp
			except:
				print sys.exc_info()[0]
			finally:
				self.channel.close()
