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
package poke.server.resources;

import java.beans.Beans;
import java.util.concurrent.atomic.AtomicReference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import poke.client.ClientCommand;
import poke.server.conf.NodeDesc;
import poke.server.conf.ServerConf;
import poke.server.conf.ServerConf.ResourceConf;
import poke.server.managers.HeartbeatData;
import eye.Comm.Header;

/**
 * Resource factory provides how the server manages resource creation. We hide
 * the creation of resources to be able to change how instances are managed
 * (created) as different strategies will affect memory and thread isolation. A
 * couple of options are:
 * <p>
 * <ol>
 * <li>instance-per-request - best isolation, worst object reuse and control
 * <li>pool w/ dynamic growth - best object reuse, better isolation (drawback,
 * instances can be dirty), poor resource control
 * <li>fixed pool - favor resource control over throughput (in this case failure
 * due to no space must be handled)
 * </ol>
 * 
 * @author gash
 * 
 */
public class ResourceFactory {
	protected static Logger logger = LoggerFactory.getLogger("server");

	private static ServerConf cfg;
	private static AtomicReference<ResourceFactory> factory = new AtomicReference<ResourceFactory>();

	public static void initialize(ServerConf cfg) {
		try {
			ResourceFactory.cfg = cfg;
			factory.compareAndSet(null, new ResourceFactory());
		} catch (Exception e) {
			logger.error("failed to initialize ResourceFactory", e);
		}
	}

	public static ResourceFactory getInstance() {
		ResourceFactory rf = factory.get();
		if (rf == null)
			throw new RuntimeException("Server not intialized");

		return rf;
	}

	private ResourceFactory() {
	}

	/**
	 * Obtain a resource
	 * 
	 * @param route
	 * @return
	 */
	
	public Resource resourceInstance(Header header) 
	{
		// is the message for this server?
		ResourceConf rc = cfg.findById(header.getRoutingId().getNumber());
		if (rc == null)
			return null;

		try {
			// strategy: instance-per-request
			Resource rsc = (Resource) Beans.instantiate(this.getClass().getClassLoader(), rc.getClazz());
			return rsc;
		} catch (Exception e) {
			logger.error("unable to create resource " + rc.getClazz());
			return null;
		}
	}

	public static ServerConf getCfg() {
		return cfg;
	}

	public static void setCfg(ServerConf cfg) {
		ResourceFactory.cfg = cfg;
	}

	public boolean needToRoute(Header header) {
		int determinedNode=-1;
		int destHop=-1;
		if (!header.hasToNode()) {
			// Determine node with hashing
			// Uncomment next line to enable pinging to determined node.
			determinedNode =1;
		}					
		if (header.hasToNode())
			destHop=header.getToNode();
		else
			destHop=determinedNode;
		if (cfg.getNodeId() != destHop)
			return true;
		else
			return false;
	}

	public NodeDesc getDeterminedNode(Header header) 
	{
		int determinedNode=-1;
		int destHop=-1;
		if (!header.hasToNode()) 
		{
			// Determine node with hashing
			// Uncomment next line to enable pinging to determined node.
			determinedNode =1;
		}					
		if (header.hasToNode())
			destHop=header.getToNode();
		else
			destHop=determinedNode;
		
		for (NodeDesc nn : cfg.getAdjacent().getAdjacentNodes().values()) 
		{
			if(nn.getNodeId()==destHop)
			{
				return nn;
			}
		}
		return null;
	}

}
