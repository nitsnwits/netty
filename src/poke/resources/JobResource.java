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
import poke.server.resources.jobHandlers.CreateHandler;
import poke.server.resources.jobHandlers.DeleteHandler;
import poke.server.resources.jobHandlers.FailureHandler;
import poke.server.resources.jobHandlers.FetchHandler;
import eye.Comm.Payload;
import eye.Comm.PhotoHeader.RequestType;
import eye.Comm.PhotoPayload;
import eye.Comm.Ping;
import eye.Comm.PokeStatus;
import eye.Comm.Request;

public class JobResource implements Resource {
	protected static Logger logger = LoggerFactory.getLogger("server");
	@Override
	public Request process(Request request) {
		// TODO add code to process the message/event received
		logger.info("Header: " + request.getHeader());
		//logger.info("request String: " + request.toString());
		//logger.info("request: " + request);
		Request reply;
		if(request.getHeader().getPhotoHeader().getRequestType()==RequestType.write)
		{
			CreateHandler ch=new CreateHandler();
			reply=ch.handleRequest(request);
		}
		else if(request.getHeader().getPhotoHeader().getRequestType()==RequestType.read)
		{
			FetchHandler fh=new FetchHandler();
			reply=fh.handleRequest(request);
		}
		else if(request.getHeader().getPhotoHeader().getRequestType()==RequestType.delete)
		{
			DeleteHandler dh= new DeleteHandler();
			reply=dh.handleRequest(request);
		}
		else
		{
			FailureHandler fh=new FailureHandler();
			reply=fh.handleRequest(request,"Invalid Job Requested, Cannot be Served");
		}
		return reply;
	}

}
