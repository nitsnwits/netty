package poke.consistentHash;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import poke.hash.Ketama;
import poke.server.conf.HashRangeMap;
import poke.server.conf.ServerConf;

public class EqualAreaRing extends DataRing {
	private TreeMap<Long, List<DataNode>> rangeMap = HashRangeMap.getInstance().getRangeMap();//new TreeMap<Long, List<DataNode>>();
	private List<Long> keyList = new ArrayList<Long>();
	private ServerConf conf;
	private ArrayList<PhysicalNode> pnodes = new ArrayList<PhysicalNode>();
	private int numOfReplicas = 2; // for each data node, so 6 for a physical node of heterogeneity 3
	
	// crap java, somehow this is not imported :(
	//protected static Logger logger = LoggerFactory.getLogger("Equal Area Ring");
	
	public EqualAreaRing() {
	}

	@Override
	public void createNodes(int n) {
		this.numNodes = n;
		long maxN = Long.MAX_VALUE;
		long range = maxN / numNodes;
		int dataNodeId = 0;

		for (int i = 0; i < numNodes; i++) {
			// assume numNodes is physical nodes (we'll create data nodes based on heterogeneity)
			// Create physical node first, who will own this data node
			PhysicalNode pn = new PhysicalNode();
			pn.setId(i);
			//pn.setHeterogeneity(conf.findById(i).getHeterogeneity());
			pn.setHeterogeneity(3); //hard code for testing
			
			//create data nodes equal to the physical node's heterogeneity
			for (int dnNum = 0; dnNum < pn.getHeterogeneity(); dnNum++) {
				
				//create a data node
				DataNode dn = new DataNode();
				dn.setId("node-" + dataNodeId);
				
				//let this physical node own this data node, making sure of its heterogeneity
				pn.addOwned(dn);
				
				//set ketama as hash algo for data node
				Ketama ketama = new Ketama();
				dn.setHash(ketama);
				
				long keyRange = -1;
				// check if this is not end of the ring
				if (dnNum != 0) {
					keyRange = range * (dataNodeId + 1);
					dn.setHashLimit(keyRange);
				} else { // circle back the ring, if it reaches 0
					keyRange = maxN;
					dn.setHashLimit(keyRange);
				}
				
				// dont know why i did this
				keyList.add(keyRange);
				nodes.add(dn);
				
				// finally, the data node is added
				dataNodeId++;				
				
			}
			//so that the physical nodes created are not lost
			pnodes.add(pn);
		
		}
		// Yes, replica's should be added, it's a separate method, coz DJ said it's cool
		addReplica();
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
				
				// checks if the loop iteration has reached the data node size of next physical node
				// if reached, then moves to next physical node
				if(pnNextOwnIndex > pnodes.get(pnNext).getOwns().size() - 1) {
					pnNext++;
				}
				
				// add replicas to the node
				pNode.addReplica(pnodes.get(pnNext).getOwns().get(pnNextOwnIndex));
				if (pnNextOwnIndex == pnodes.get(pnNext).getOwns().size() - 1) {
					pnNextOwnIndex = 0;
				} else {
					pnNextOwnIndex++;
				}
				
			}
			
		}
		
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
		} catch (Exception e) {
		}
	}
	
	/*private void addReplica(TreeMap<Long, List<DataNode>> sampleMap){
		for(Map.Entry<Long, List<DataNode>> entry : sampleMap.entrySet()){
			Long key = entry.getKey();
			List<DataNode> retrievedList = entry.getValue();
			//read number of replica nodes from properties or config file
			int numOfReplicas = 3;
			for(DataNode tempNode : retrievedList){
				for(int i=0; i<nodes.size();i++){
					int j = i;
					DataNode orgNode = nodes.get(i);
					if(tempNode.getId().equals(orgNode.getId())){
						if(i==nodes.size()-1){
							j=-1;
						}
						for(int k=0;k<numOfReplicas;k++){
							j++;
							retrievedList.add(nodes.get(j));
							System.out.println("Node Id added to list is-->"+nodes.get(j).getId());
						}
					}
				}
				
			}
			
		}
	}*/
	
	public static void main(String args[]){
		
		EqualAreaRing eqr = new EqualAreaRing();
		eqr.createNodes(5);
		eqr.printNodeRanges();
	}
}
