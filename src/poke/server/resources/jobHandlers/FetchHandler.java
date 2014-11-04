package poke.server.resources.jobHandlers;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

import com.google.protobuf.ByteString;

import poke.server.resources.ResourceUtil;
import poke.server.storage.mongo.Operations;
import eye.Comm.Payload;
import eye.Comm.PhotoPayload;
import eye.Comm.Request;
import eye.Comm.PhotoHeader.ResponseFlag;

public class FetchHandler {

	public Request handleRequest(Request request) {
		// TODO Auto-generated method stub
		if(request.getBody().getPhotoPayload().hasUuid())
		{
			UUID uuid = UUID.fromString(request.getBody().getPhotoPayload().getUuid());
			Operations mongoOps= new Operations();
			
			
			//Fetching To MongoDB
			Map result=mongoOps.fetchPhoto(uuid);
			String name =(String) result.get("name");
			byte[] img= (byte[]) result.get("data");
			ByteString data=ByteString.copyFrom(img);
			long lastModified=(Long) result.get("lastModified");
			System.out.println("Name of retrieved photo: " + name);
			System.out.println("Last Modified on: " + lastModified);
			Date lastMod=new Date(lastModified);

			Request.Builder rb = Request.newBuilder();

			// Setting Headers
			rb.setHeader(ResourceUtil.buildPhotoHeader(request,ResponseFlag.success,lastMod));

			// Setting PhotoPayload
			PhotoPayload.Builder ppb= PhotoPayload.newBuilder();
			ppb.setUuid(request.getBody().getPhotoPayload().getUuid());
			//ppb.setData(null);
			ppb.setName(name);
			ppb.setData(data);

			
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
