package poke.server.resources.jobHandlers;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

import poke.server.resources.ResourceUtil;
import poke.server.storage.mongo.Operations;
import poke.util.UUIDGenerator;

import com.google.protobuf.ByteString;

import eye.Comm.Payload;
import eye.Comm.PhotoPayload;
import eye.Comm.Request;
import eye.Comm.PhotoHeader.ResponseFlag;
import eye.Comm.Request.Builder;

public class CreateHandler 
{

	public Request handleRequest(Request request) 
	{

		if(request.getBody().getPhotoPayload().hasData() && request.getBody().getPhotoPayload().hasName() && request.getBody().getPhotoPayload().hasUuid())
		{
			ByteString bs=request.getBody().getPhotoPayload().getData();
			UUID photoId=UUID.fromString(request.getBody().getPhotoPayload().getUuid());
			Operations mongoOps= new Operations();
			Date lastModified=  new Date();
			
			//Saving To MongoDB
			mongoOps.savePhoto(photoId, request.getBody().getPhotoPayload().getName(), bs, lastModified.getTime());


			Request.Builder rb = Request.newBuilder();

			// Setting Headers
			rb.setHeader(ResourceUtil.buildPhotoHeader(request,ResponseFlag.success,lastModified));

			// Setting PhotoPayload
			PhotoPayload.Builder ppb= PhotoPayload.newBuilder();
			ppb.setUuid(photoId.toString());
			ppb.setName(request.getBody().getPhotoPayload().getName());

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
			return fh.handleRequest(request, "Invalid Photo Payload");
		}
	}

}
