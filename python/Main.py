#
#	Main class for Python client - Project 1 CMPE 275
#
#
import sys
from Client import Client

client = True #client start up.
while client:
	print "\n ***** Welcome to Lifeforce MOOC backbone client. ***** \n"
	host = "127.0.0.1" #raw_input("\tEnter host/IP: ")
	port = 5570 #int(raw_input("\tEnter port: "))
	print "\tPlease choose from the following options: \n"
	print "\t\t 1. Upload an image to the server."
	print "\t\t 2. Retrieve an image from the server."
	print "\t\t 3. Exit the client."
	
	
	choice = int(input("\n\tYour Option: "))
	if choice == 1:
		#Sending photoRequest to Server
		requestType = "photoCreateRequest"
		# host = str(input("Enter host: "))
		# port = int(input("Enter port: "))
		inputimage= raw_input("\n\t\t\tEnter image file's absolute path: ")

		#read the input image from the file system from the path.          
		Client().executeOperation(requestType, host, port, inputimage)
		  
		  
	elif choice == 2:
		#Sending ListCourses Request to Server
		requestType = "photoReadRequest"
		# host = str(input("Enter host: "))
		# port = int(input("Enter port: "))
		uuid = raw_input("\n\t\tEnter UUID for the file: ")
		Client().executeOperation(requestType, host, port, uuid)	
		  
	elif choice == 3:
		  print "Bye!"
		  client = False #client shut down.
	else:
		  print "\t\tPlease choose a valid option."   
		   

