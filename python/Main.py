#
#	Main class for Python client - Project 1 CMPE 275 Lifeforce project
#
#
import sys
from Client import Client

#start messages, retrieve host and port
print "\n ***** Welcome to Lifeforce backbone client. ***** \n"
host = raw_input("\tEnter host/IP: ")
port = int(raw_input("\tEnter port: "))

client = True #client start up.
while client:
	print "\tPlease choose from the following options: \n"
	print "\t\t 1. Upload an image to the server."
	print "\t\t 2. Retrieve an image from the server."
	print "\t\t 3. Delete an image from the server."
	print "\t\t 4. Exit the client."
	
	try:
		choice = int(input("\n\tYour Option: "))
	except:
		choice = 100 # fall back to default in case of newline or keyboard interrupt

	if choice == 1:
		#Sending photo create request to Server
		requestType = "photoCreateRequest"
		inputimage= raw_input("\n\tEnter image file's absolute path: ")

		#read the input image from the file system from the path.          
		Client().executeOperation(requestType, host, port, inputimage)
		  
		  
	elif choice == 2:
		#Sending photo read Request to Server
		requestType = "photoReadRequest"
		uuid = raw_input("\n\tEnter UUID for the file: ")
		Client().executeOperation(requestType, host, port, uuid)

	elif choice == 3:
		#Sending photo delete Request to Server
		requestType = "photoDeleteRequest"
		uuid = raw_input("\n\tEnter UUID for the file: ")
		Client().executeOperation(requestType, host, port, uuid)		
		  
	elif choice == 4:
		print "\nThank you for using Lifeforce. Bye!\n"
		client = False #client shut down.
		
	else:
		print "\n\t\tPlease choose a valid option."   
		   

