package poke.server.resources;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;

import javax.imageio.ImageIO;

import org.junit.Test;

import com.google.protobuf.ByteString;





//import com.vividsolutions.jts.util.Assert;
import org.junit.*;

import eye.Comm;
import eye.Comm.Header;
import eye.Comm.Payload;
import eye.Comm.PhotoHeader;
import eye.Comm.PhotoPayload;
import eye.Comm.Request;

public class JobResourceTest {
	private static String img = "test/poke/server/resources/minionbby.jpg";
	private Request createReq = null;
	private Request readReq = null;
	private Request deleteReq = null;
	

	
	@Test
	public void testCreateRequest() throws Exception {
		
		Request.Builder rbc = Request.newBuilder();
		Header.Builder bldrc = Header.newBuilder();
		bldrc.setOriginator(1);
		bldrc.setRoutingId(Comm.Header.Routing.JOBS);
		PhotoHeader.Builder phb= PhotoHeader.newBuilder();
		phb.setRequestType(Comm.PhotoHeader.RequestType.write);
		bldrc.setPhotoHeader(phb);
		rbc.setHeader(bldrc.build());
		PhotoPayload.Builder ppbc= PhotoPayload.newBuilder();
		File fnew=new File(img);
		BufferedImage originalImage=ImageIO.read(fnew);
		ByteArrayOutputStream baos=new ByteArrayOutputStream();
		ImageIO.write(originalImage, "jpg", baos );
		byte[] image=baos.toByteArray();
        ByteString data=ByteString.copyFrom(image);
        ppbc.setName("minionbby.jpg");
        ppbc.setData(data);
        Payload.Builder pbc=Payload.newBuilder();
        pbc.setPhotoPayload(ppbc.build());
        rbc.setBody(pbc.build());
        createReq = rbc.build();
		
		Resource rsc = ResourceFactory.getInstance().resourceInstance(createReq.getHeader());
		Request reply = rsc.process(createReq);

		Assert.assertEquals(0, reply.getHeader().getPhotoHeader().getResponseFlag());
		Assert.assertNotNull(reply.getBody().getPhotoPayload().getUuid());
	}
	
	@Test
	public void testReadRequest() throws Exception {
		
		Request.Builder rbc = Request.newBuilder();
		Header.Builder bldrc = Header.newBuilder();
		bldrc.setOriginator(1);
		bldrc.setRoutingId(Comm.Header.Routing.JOBS);
		PhotoHeader.Builder phb= PhotoHeader.newBuilder();
		phb.setRequestType(Comm.PhotoHeader.RequestType.read);
		bldrc.setPhotoHeader(phb);
		rbc.setHeader(bldrc.build());
		PhotoPayload.Builder ppbc= PhotoPayload.newBuilder();
		ppbc.setUuid("e2d66c81-6480-4dfe-b12f-7f9d0441796d");
        Payload.Builder pbc=Payload.newBuilder();
        pbc.setPhotoPayload(ppbc.build());
        rbc.setBody(pbc.build());
        readReq = rbc.build();

        
		Resource rsc = ResourceFactory.getInstance().resourceInstance(readReq.getHeader());
		Request reply = rsc.process(readReq);

		Assert.assertEquals(0, reply.getHeader().getPhotoHeader().getResponseFlag());
		Assert.assertNotNull(reply.getBody().getPhotoPayload().getData());
	}
	
	@Test
	public void testDeleteRequest() throws Exception {
		Request.Builder rbc = Request.newBuilder();
		Header.Builder bldrc = Header.newBuilder();
		bldrc.setOriginator(1);
		bldrc.setRoutingId(Comm.Header.Routing.JOBS);
		PhotoHeader.Builder phb= PhotoHeader.newBuilder();
		phb.setRequestType(Comm.PhotoHeader.RequestType.delete);
		bldrc.setPhotoHeader(phb);
		rbc.setHeader(bldrc.build());
		PhotoPayload.Builder ppbc= PhotoPayload.newBuilder();
		ppbc.setUuid("e2d66c81-6480-4dfe-b12f-7f9d0441796d");
        Payload.Builder pbc=Payload.newBuilder();
        pbc.setPhotoPayload(ppbc.build());
        rbc.setBody(pbc.build());
        deleteReq = rbc.build();
		
		Resource rsc = ResourceFactory.getInstance().resourceInstance(deleteReq.getHeader());
		Request reply = rsc.process(deleteReq);

		Assert.assertEquals(0, reply.getHeader().getPhotoHeader().getResponseFlag());
	}
}
