#
# Provide socket functionalities to client class
#
#

import socket
import struct

class SocketChannelFactory():
	def openChannel(self, host, port):
		sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
		sock.connect((host, port))
		return SocketChannel(sock)

class SocketChannel():
  
	def __init__(self, sock):
		self.sock = sock
		self.connected = True
  
	def write(self, byteStream):
	
		streamLen = struct.pack('>L', len(byteStream))
		framedStream = streamLen + byteStream
		try:
			self.sock.sendall(framedStream)
		except socket.error:
			self.close()
			raise Exception("Socked send failed. Closing.")
	  
	def read(self):
		print "reading.."
		lenField = self.readnbytes(4)
		print "lenField: " + str(lenField)
		length = struct.unpack('>L', lenField)[0]
		print "length: " + str(length)
		byteStream = self.readnbytes(length)
		return byteStream
  
	def readnbytes(self, n):
		print "called readnbytes.."
		buf = ''
		while n > 0:
			print "in the loop: "
			data = self.sock.recv(n)
			print "Data: " + str(data)
			if data == '':
				raise Exception("socket broken or connection closed")
			buf += data
			n -= len(data)
		return buf

	def close(self):
		self.sock.close()
		self.connected = False