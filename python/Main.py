#
#	Main class for Python client - Project 1 CMPE 275
#
#
import sys
from fs.osfs import OSFS #to include the native file system.
#sys.path.append.add the path where python has to search for the modules and files
from PyClient import PyClient

client = True #client up.
while client:
	print "******client running****"
	print "choose from the options"
	print "Give the corresponding number"
	print "1.Upload an image"
	print "2.retrieve an image"
	print "3.quit"
	
	
choice = int(input("\nYour Option"))
      if choice == 1:
          #Sending photoRequest to Server
          requestType = "photoCreateRequest"
          host = str(input("Enter host: "))
          port = int(input("Enter port: "))
          inputimage= int(input("give your file name path"))
          
          #read the input image from the file system from the path.          
          PyClient().chooseOperation(requestType, host, port, inputimage)
          
          
      elif choice == 2:
          #Sending ListCourses Request to Server
          requestType = "photoReadRequest"
          host = str(input("Enter host: "))
          port = int(input("Enter port: "))
          uuid = int(input("give your file name: "))
          PyClient().chooseOperation(requestType, host, port, uuid)	
          
      elif choice == 3:
          print "Bye!"
          client = False #client down.
      else:
          print "Please choose a valid option"   
           

