#include<iostream> //printf
#include<string.h>    //strlen
#include<string>
#include<sys/socket.h>    //socket
#include<arpa/inet.h> //inet_addr
#include "comm.pb.h"
#include<fstream>

using namespace std;
typedef vector<char> data_buffer;

//Method to serialize size in big endian format
void encode_header(data_buffer& buf, unsigned size)
{
        assert(buf.size() >= 4);
        buf[0] = static_cast<char>((size >> 24) & 0xFF); //storing first byte in position 0
        buf[1] = static_cast<char>((size >> 16) & 0xFF); //storing 2nd byte in position 1
        buf[2] = static_cast<char>((size >> 8) & 0xFF);
        buf[3] = static_cast<char>(size & 0xFF);
}


// Socket connection
int sockfactory(){
 	int sock;
    	struct sockaddr_in server;
        //Create socket
        sock = socket(AF_INET , SOCK_STREAM , 0);
        if (sock == -1)
          {
		cout <<"Could not create socket";
               // return;
          }
         server.sin_addr.s_addr = inet_addr("127.0.0.1");
         server.sin_family = AF_INET;
         server.sin_port = htons(5570);
         //Connect to remote server
         int y = connect(sock , (struct sockaddr *)&server , sizeof(server));
	 cout << "Connect : " << y << endl;
	 return sock;
}
//Upload image Request

void prepareCreateImage(){
	
	string path;
	string imagename;
	string str;

	Request r = Request::default_instance();
        Payload* p = r.mutable_body();
	//set header
	Header* h = r.mutable_header();
        h->set_routing_id(Header_Routing_JOBS);
        h->set_originator(2);
	// set photoheader
	PhotoHeader* ph = h->mutable_photoheader();
	ph->set_requesttype(PhotoHeader_RequestType_write);

	//set body
	PhotoPayload* pp = p->mutable_photopayload();
	cout << "\n\tEnter image file's absolute path: ";
	cin >> path;
	cout << "\n\tEnter file name: ";
	cin >> imagename;
        pp->set_name(imagename);
        std::ifstream ifs(path.c_str());
  	std::string content( (std::istreambuf_iterator<char>(ifs) ), (std::istreambuf_iterator<char>()) );
	
	pp->set_data(content);
	
	vector<char> buffer;        
	unsigned msg_size = r.ByteSize(); //size of the request
        buffer.resize(4 + msg_size);
        encode_header(buffer, msg_size);
        r.SerializeToArray(&buffer[4], msg_size); //The first 4 bytes contain the length of the request. 
        try
        {
            int sockt;
	    sockt = sockfactory();
            
	   // send data	

	    int x = send(sockt , &buffer[0] , (msg_size + 4) , 0); 
        	cout << "Send : " << x << endl;
	    //Code to receive
	    int numbytes = 0;
	    char buf[255];
	    memset(buf, '\0', sizeof(buf));
       	    numbytes = recv(sockt, buf, sizeof(buf), 0);


	Request resp = Request::default_instance();
        Payload* rp = resp.mutable_body();
	PhotoPayload* rpp = p->mutable_photopayload();
	cout<< rpp->release_data();
	//cout<<data1;
	    if(numbytes)
	    {
		cout << "READ " << numbytes << " bytes of response." << endl;
		cout << "Response Received... " <<endl;
		str.append(buf,255);
		for(int ind=0;ind<numbytes;ind++)
			cout<<buf[ind];
		cout<<endl;
		//string str(buf);
		//cout<< str << endl;
	    }
	    else
		cout << "Nothing returned " <<endl; 
            }
            catch (int e)
        {
            cout << "Unable to deliver message, queuing" << endl;
        }
    }

// Read Image Request

void prepareReadImage(){

	string uid;
	string str;

	Request r = Request::default_instance();
        Payload* p = r.mutable_body();
	//set header
	Header* h = r.mutable_header();
        h->set_routing_id(Header_Routing_JOBS);
        h->set_originator(2);
	// set photoheader
	PhotoHeader* ph = h->mutable_photoheader();
	ph->set_requesttype(PhotoHeader_RequestType_read);

	//set body
	PhotoPayload* pp = p->mutable_photopayload();
	cout << "\n\tEnter UUID for the file : ";
	cin >> uid;
        pp->set_uuid(uid);
        
	
	vector<char> buffer;        
	unsigned msg_size = r.ByteSize(); //size of the request
        buffer.resize(4 + msg_size);
        encode_header(buffer, msg_size);
        r.SerializeToArray(&buffer[4], msg_size); //The first 4 bytes contain the length of the request. 
        try
        {
            int sockt;
	    sockt = sockfactory();
            
	   // send data	

	    int x = send(sockt , &buffer[0] , (msg_size + 4) , 0); 
        	cout << "Send : " << x << endl;
	    //Code to receive
	    int numbytes = 0;
	    char buf[255];
	    memset(buf, '\0', sizeof(buf));
       	    numbytes = recv(sockt, buf, sizeof(buf), 0);

	Request resp = Request::default_instance();
        Payload* rp = resp.mutable_body();
	PhotoPayload* rpp = p->mutable_photopayload();
	cout<< rpp->release_name();
	//cout<<
		

	    if(numbytes)
	    {
		cout << "READ " << numbytes << " bytes of response." << endl;
		cout << "Response Received... " <<endl;
		for(int ind=0;ind<numbytes;ind++)
			str = ""+buf[ind];
		cout<<endl;
		cout<<str<<endl; 
	    } 
	    else
		cout << "Nothing returned " <<endl; 
            }
            catch (int e)
        {
            cout << "Unable to deliver message, queuing" << endl;
        }
    }

// Delete Request
/*
void prepareDeleteImage(){

	string uid;

	Request r = Request::default_instance();
        Payload* p = r.mutable_body();
	//set header
	Header* h = r.mutable_header();
        h->set_routing_id(Header_Routing_JOBS);
        h->set_originator(2);
	// set photoheader
	PhotoHeader* ph = h->mutable_photoheader();
	ph->set_requesttype(PhotoHeader_RequestType_delete);

	//set body
	PhotoPayload* pp = p->mutable_photopayload();
	cout << "\n\tEnter UUID for the file:" << endl;
	cin >> uid;
        pp->set_uuid(uid);
        
	
	vector<char> buffer;        
	unsigned msg_size = r.ByteSize(); //size of the request
        buffer.resize(4 + msg_size);
        encode_header(buffer, msg_size);
        r.SerializeToArray(&buffer[4], msg_size); //The first 4 bytes contain the length of the request. 
        try
        {
            int sockt;
	    sockt = sockfactory();
            
	   // send data	

	    int x = send(sockt , &buffer[0] , (msg_size + 4) , 0); 
        	cout << "Send : " << x << endl;
	    //Code to receive
	    int numbytes = 0;
	    char buf[255];
	    memset(buf, '\0', sizeof(buf));
       	    numbytes = recv(sockt, buf, sizeof(buf), 0);
	    if(numbytes)
	    {
		cout << "READ " << numbytes << " bytes of response." << endl;
		cout << "Response Received... " <<endl;
		for(int ind=0;ind<numbytes;ind++)
			cout<<buf[ind];
		cout<<endl;
	    } 
	    else
		cout << "Nothing returned " <<endl; 
            }
            catch (int e)
        {
            cout << "Unable to deliver message, queuing" << endl;
        }
    }

*/

int main()
{
int i;
do{
	cout << "\n\tPlease choose from the following options: \n" << endl;
	cout << "\t\t 1. Upload an image to the server." << endl;
	cout << "\t\t 2. Retrieve an image from the server." << endl;
	cout << "\t\t 3. Delete an image from the server." << endl;
	cout << "\t\t 4. Exit the client." << endl;
	cout << "\t\t Enter Your Choice: ";
	cin >> i;
	
	switch(i){
	
	case 1: cout << "Job: Store Image" << endl;
		prepareCreateImage();
		break;
	case 2: cout << "Job: Read Image" << endl;
		prepareReadImage();
		break;
	case 3: cout << "Job: Delete Image" << endl;
		//prepareDeleteImage();
		break;
	case 4: break;
	default: cout << "Wrong input" << endl;
		 break;

}
} while(i != 4);

//return 0;
}
