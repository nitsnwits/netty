package poke.server.resources.jobHandlers;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import poke.server.resources.ResourceUtil;
import poke.util.UUIDGenerator;

import com.google.protobuf.ByteString;

import eye.Comm.Payload;
import eye.Comm.PhotoPayload;
import eye.Comm.Request;
import eye.Comm.PhotoHeader.ResponseFlag;

public class DeleteHandler 
{

	public Request handleRequest(Request request) 
	{

		if(request.getBody().getPhotoPayload().hasUuid())
		{
			UUID uuid = UUID.fromString(request.getBody().getPhotoPayload().getUuid());
//			//byte[] buf = bs.toByteArray();
//
//			FileOutputStream fileOuputStream;
//			try 
//			{
//				fileOuputStream = new FileOutputStream("/Users/siddharthbhargava/Desktop/minion.jpg");
//				fileOuputStream.write(buf);
//				fileOuputStream.close();
//			} catch (FileNotFoundException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} 

			//logger.info("body:" + request.getBody());



			Request.Builder rb = Request.newBuilder();

			// Setting Headers
			rb.setHeader(ResourceUtil.buildPhotoHeader(request,ResponseFlag.success,null));

			// Setting PhotoPayload
			PhotoPayload.Builder ppb= PhotoPayload.newBuilder();
			ppb.setUuid(request.getBody().getPhotoPayload().getUuid());
			
			//Setting Payload
			Payload.Builder pb=Payload.newBuilder();
			pb.setPhotoPayload(ppb.build());
			rb.setBody(pb.build());

			Request reply = rb.build();

			return reply;
		}
		else
		{
			FailureHandler fh=new FailureHandler();
			return fh.handleRequest(request, "Invalid Photo UUID");
		}
	}
}
