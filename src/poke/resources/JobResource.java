/*
 * copyright 2012, gash
 * 
 * Gash licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package poke.resources;

import java.io.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.h2.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.ByteString;

import poke.server.resources.Resource;
import poke.server.resources.ResourceUtil;
import eye.Comm.Payload;
import eye.Comm.JobOperation;
import eye.Comm.PhotoPayload;
import eye.Comm.Ping;
import eye.Comm.PokeStatus;
import eye.Comm.Request;

public class JobResource implements Resource {
	protected static Logger logger = LoggerFactory.getLogger("server");
	@Override
	public Request process(Request request) {
		// TODO add code to process the message/event received
		//logger.info("poke: " + request.getBody().getPhotoPayload().data);
		
		com.google.protobuf.ByteString a = request.getBody().getPhotoPayload().getData();
		byte[] b = a.toByteArray();
		String s = a.toString();
		
		
		String strFilePath = "/home/jaymit/"+request.getBody().getPhotoPayload().getName();
		
		 try
	     {
	      FileOutputStream fos = new FileOutputStream(strFilePath);
	      //	String strContent = "Write File using Java FileOutputStream example !";
	         
	      /*
	       * To write byte array to a file, use
	       * void write(byte[] bArray) method of Java FileOutputStream class.
	       *
	       * This method writes given byte array to a file.
	       */
	     
	       fos.write(b);
	     
	      /*
	       * Close FileOutputStream using,
	       * void close() method of Java FileOutputStream class.
	       *
	       */
	     
	       fos.close();
	     
	     }
	     catch(FileNotFoundException ex)
	     {
	      System.out.println("FileNotFoundException : " + ex);
	     }
		 catch(IOException ioe)
	     {
	      System.out.println("IOException : " + ioe);
	     }
		System.out.println(s);
		/*OutputStream os = new FileOutputStream("/home/jaymit/sjsu.jpg");
	    os.write(b);
	    os.close();*/
		
		logger.info("poke: " + request.getHeader());
		logger.info("poke: " + PokeStatus.SUCCESS);
		logger.info("Header: " + request.getHeader());
		//logger.info("request String: " + request.toString());
		//logger.info("request: " + request);
		 
		if(request.getBody().getPhotoPayload().hasData())
		{
			ByteString bs=request.getBody().getPhotoPayload().getData();
			byte[] buf = bs.toByteArray();
		 
			FileOutputStream fileOuputStream;
			try {
				fileOuputStream = new FileOutputStream("/Users/siddharthbhargava/Desktop/minion.jpg");
				fileOuputStream.write(buf);
				fileOuputStream.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			
			//logger.info("body:" + request.getBody());
		}

		Request.Builder rb = Request.newBuilder();

		// metadata
		rb.setHeader(ResourceUtil.buildPhotoHeader(request));
		
		// payload
		Payload.Builder pb = Payload.newBuilder();
		JobOperation.Builder fb = JobOperation.newBuilder();
		
	    //ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    //Message message = (Message) pb;
	    //byte[] name = pb.getPhotoPayload().getData().getBytes();
	    //baos.write(name);
	    //System.out.println(name);
		
		//fb.setData(request.getBody().getJobOp().getData());
		//fb.setNumber(request.getBody().getPing().getNumber());
		//pb.setPing(fb.build());
		//rb.setBody(pb.build());
		//logger.info("poke: " + request.getBody().getJobOp().getNameBytes());
		//Request reply = rb.build();
		PhotoPayload.Builder ppb= PhotoPayload.newBuilder();
		//generate uuid
		ppb.setUuid("Test1");
		ppb.setName(request.getBody().getPhotoPayload().getName());
		//ppb.setData("");
//		Payload.Builder pb=Payload.newBuilder();
		pb.setPhotoPayload(ppb.build());
		
		rb.setBody(pb.build());

		Request reply = rb.build();

		return null;
	}

}
