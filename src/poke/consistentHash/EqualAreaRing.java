package poke.consistentHash;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import poke.hash.MurmurHash128;
import poke.server.conf.HashRangeMap;
import poke.server.conf.HashRangeMap.NodeStatus;
import poke.server.conf.ServerConf;
import poke.util.UUIDGenerator;

public class EqualAreaRing extends DataRing {
	private List<Long> keyList = new ArrayList<Long>();
	private static ServerConf conf;
	private ArrayList<PhysicalNode> pnodes = new ArrayList<PhysicalNode>();
	private int numOfReplicas = 2; // for each data node, so 6 for a physical node of heterogeneity 3
	//private static Map<Integer, NodeStatus> rangeMap = HashRangeMap.getInstance().getRangeMap();
	private static EqualAreaRing instance = null;
	private int status = 0; // fall back to active for now
	
	private EqualAreaRing() {
	}
	
	public static EqualAreaRing getInstance() {
		if(instance == null) {
			instance = new EqualAreaRing();
		}
		return instance;
	}
	
	public void initializeConf(ServerConf conf) {
		EqualAreaRing.getInstance().conf = conf;
		createNodes(conf.getAdjacent().getAdjacentNodes().size() + 1);
	}

	@Override
	public void createNodes(int n) {
		this.numNodes = n;
		//this.numNodes = getNumOfDataNodes(n);
		long maxN = Long.MAX_VALUE;
		//long maxN = (long) 15;
		long range = maxN / getNumOfDataNodes(n);
		int dataNodeId = 0;
		//System.out.println("rannge: " + range);

		for (int i = 0; i < n; i++) {
			// assume numNodes is physical nodes (we'll create data nodes based on heterogeneity)
			// Create physical node first, who will own this data node
			PhysicalNode pn = new PhysicalNode();
			pn.setId(i);
			//Uncomment this when running a whole server, this should work then
			if (i == conf.getNodeId()) {
				// if current running node is this node
				pn.setHeterogeneity(conf.getHeterogeneity());
			} else {
				pn.setHeterogeneity(conf.getAdjacent().getAdjacentNodes().get(i).getHeterogeneity());
			}
			
			//pn.setHeterogeneity(3); //hard code for testing
			
			//create data nodes equal to the physical node's heterogeneity
			for (int dnNum = 0; dnNum < pn.getHeterogeneity(); dnNum++) {
				
				//create a data node
				DataNode dn = new DataNode();
				dn.setId("node-" + dataNodeId);
				
				//add the mappings to a hashmap, assume all nodes are UP
				NodeStatus nodeStatus = HashRangeMap.getInstance().new NodeStatus();
				nodeStatus.setNodeId(i);
				nodeStatus.setStatus(status);
				// Now node list value will be [0, 0]
				HashRangeMap.getInstance().getRangeMap().put(dataNodeId, nodeStatus);
				
				//set ketama as hash algo for data node
				//Ketama ketama = new Ketama();
				MurmurHash128 murmur128 = new MurmurHash128();
				dn.setHash(murmur128);
				
				long keyRange = -1;
				// check if this is not end of the ring
				if (dataNodeId != 0) {
					keyRange = range * (dataNodeId + 1);
					System.out.println("keyrange: " + keyRange + " range: " + range + " dataNodeId " + dataNodeId);
					dn.setHashLimit(keyRange);
				} else { // circle back the ring, if it reaches 0
					keyRange = maxN;
					dn.setHashLimit(keyRange);
				}
				
				// dont know why i did this
				keyList.add(keyRange);
				nodes.add(dn);
				
				//let this physical node own this data node, making sure of its heterogeneity
				pn.addOwned(dn);				
				
				// finally, the data node is added
				dataNodeId++;				
				
			}
			//so that the physical nodes created are not lost
			pnodes.add(pn);
		
		}
		// Yes, replica's should be added, it's a separate method, coz DJ said it's cool
		addReplica();
	}
	
	private int getNumOfDataNodes(int numOfPhysicalNodes) {
		int numOfDataNodes = 0;
		int heterogeneity = 0;
		for (int i = 0; i < numOfPhysicalNodes; i++) {
			if (i == conf.getNodeId()) {
				// if current running node is this node
				heterogeneity = conf.getHeterogeneity();
			} else {
				heterogeneity = conf.getAdjacent().getAdjacentNodes().get(i).getHeterogeneity();
			}
			for (int dnNum = 0; dnNum < heterogeneity; dnNum++) {
				numOfDataNodes++;
			}
		}
		System.out.println("num" + numOfDataNodes);
		return numOfDataNodes;
	}
	
	private void addReplica() {
		// we have list of physical nodes, list of data nodes, and info of which physical nodes own which data node
		// and heterogeneity ofcourse, everything that's needed to create replicas is there
	
		int pnReplicas = 0;
		int pnNext = 0;
		PhysicalNode pNode;
		
		for (int pnNum = 0; pnNum < numNodes; pnNum++) {
			pNode = pnodes.get(pnNum);
			pnReplicas = pNode.getOwns().size() * numOfReplicas;
			
			// add the set number of replicas
			pnNext = pnNum + 1;
			int pnNextOwnIndex = 0;
			for (int replica = 0; replica < pnReplicas; replica++) {
				// immediately next is the next physical node, circled back to first is last
				if (pnNext == numNodes) {
					pnNext = 0;
				}
				
				// add replicas to the node
				pNode.addReplica(pnodes.get(pnNext).getOwns().get(pnNextOwnIndex));
				pnNextOwnIndex++;
				
				// checks if the loop iteration has reached the data node size of next physical node
				// if reached, then moves to next physical node
				if(pnNextOwnIndex > pnodes.get(pnNext).getOwns().size() - 1) {
					pnNext++;
					pnNextOwnIndex = 0;
				}				
				
			}
			
		}
		printNodeRanges();
		
	}
	
	//get the physical node id based on the key
	public int getPhysicalNode(String key){
		int dnId  = getDataNodeId(key);
		//int pnId = nodeMap.get(dnId);
		NodeStatus ns = HashRangeMap.getInstance().getRangeMap().get(dnId);
		int pnId = ns.getNodeId();
		
		System.out.println("Key is--->>"+key + "  dnID-->" + dnId+ " pnId-->"+ pnId + " status-->"+ ns.getStatus());
		return pnId;
	}
	
	//get the physical nodes including replicas based on the key
		public List<Integer> getPhysicalNodes(String key){
			System.out.println("----------- getPhysicalNodes --------------");
			int dnId  = getDataNodeId(key);
			NodeStatus ns = HashRangeMap.getInstance().getRangeMap().get(dnId);
			int pnId = ns.getNodeId();
			
			List<Integer> phyNodeList = new ArrayList<Integer>();
			
			if(HashRangeMap.getInstance().isActivePhysicalNode(pnId))
				phyNodeList.add(pnId);
			
			//get the replicas for this datanode
			for(PhysicalNode phyNode : pnodes){
				if(phyNode.isDataNodePresent(dnId) && 
						HashRangeMap.getInstance().isActivePhysicalNode(phyNode.getId())){
					phyNodeList.add(phyNode.getId());
				}
			}
			
			//System.out.println("Key is--->>"+key + "  dnID-->" + dnId+ " pnId-->"+ pnId + " status-->"+ ns.getStatus());
			return phyNodeList;
		}	
		
	public void printNodeRanges() {
		System.out.println("Hash range: 0 - " + Long.MAX_VALUE);
		try {
			for (DataNode n : nodes) {
				System.out.println("Nodes: " + n.getId() + "  " + n.getHashLimit() + " " + n.getCount() + "\n");
			}
			for (PhysicalNode pn : pnodes) {
				System.out.println("PNodes: " + pn.getId() + " " );
			}
			for (Long key : keyList){
				System.out.println("Key Range is-->"+key);
			}
	
			System.out.println("----------------------");
			getPhysicalNode(UUIDGenerator.get().toString());

		} catch (Exception e) {
		}
	}
	
	public static void main(String args[]) {
		
		EqualAreaRing eqr = EqualAreaRing.getInstance();
		//eqr.createNodes(5);
		//eqr.printNodeRanges();
		for (int i=0; i<100; i++) {
			eqr.getPhysicalNode(UUIDGenerator.get().toString());
		}
	}
}
