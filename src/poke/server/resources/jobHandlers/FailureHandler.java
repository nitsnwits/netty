package poke.server.resources.jobHandlers;

import java.util.UUID;

import com.google.protobuf.ByteString;

import poke.server.resources.ResourceUtil;
import eye.Comm.Payload;
import eye.Comm.PhotoPayload;
import eye.Comm.Request;
import eye.Comm.PhotoHeader.ResponseFlag;

public class FailureHandler 
{

	public Request handleRequest(Request request, String failureMessage) 
	{


			Request.Builder rb = Request.newBuilder();

			// Setting Headers
			rb.setHeader(ResourceUtil.buildPhotoHeader(request,ResponseFlag.failure,null));

			// Setting PhotoPayload
			PhotoPayload.Builder ppb= PhotoPayload.newBuilder();
			if(request.getBody().getPhotoPayload().hasUuid())
				ppb.setUuid(request.getBody().getPhotoPayload().getUuid());
			ppb.setData(ByteString.copyFromUtf8(failureMessage));

			
			//Setting Payload
			Payload.Builder pb=Payload.newBuilder();
			pb.setPhotoPayload(ppb.build());
			rb.setBody(pb.build());

			Request reply = rb.build();

			return reply;
	}

}
