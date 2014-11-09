package poke.server.conf;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import poke.consistentHash.DataNode;

/**
 * This class will hold the key range and list of 
 * corresponding nodes for that key range.
 * 
 * @author dhananjay
 *
 */
public class HashRangeMap {
	private static HashRangeMap instance = null;
	private int inactiveStatus = 0;
	
	//private TreeMap<Long, List<DataNode>> rangeMap = new TreeMap<Long, List<DataNode>>();
	private Map<Integer, NodeStatus> rangeMap = new HashMap<Integer, NodeStatus>();
	
	public static HashRangeMap getInstance(){
		if(instance == null){
			instance = new HashRangeMap();
		}
		return instance;
	}
	
	public Map<Integer, NodeStatus> getRangeMap() {
		return rangeMap;
	}

	public void setRangeMap(Map<Integer, NodeStatus> rangeMap) {
		this.rangeMap = rangeMap;
	}
	
	//Class for Node value and its status
	public class NodeStatus {
		private int physicalNodeId;
		private int status;
		
		public int getNodeId() {
			return physicalNodeId;
		}
		public void setNodeId(int nodeId) {
			this.physicalNodeId = nodeId;
		}
		public int getStatus() {
			return status;
		}
		public void setStatus(int status) {
			this.status = status;
		}
	}
	
	// update hash map with inactive node status
	public void setNodeInactive(int physicalNodeId) {
		// find instances of physical node everywhere in the hash and set active status to 0
		for (Map.Entry<Integer, NodeStatus> entry : rangeMap.entrySet()) {
			if (entry.getValue().getNodeId() == physicalNodeId) {
				entry.getValue().setStatus(inactiveStatus);
			}
		}
	}

}
