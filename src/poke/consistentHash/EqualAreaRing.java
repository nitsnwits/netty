package poke.consistentHash;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import poke.hash.Ketama;
import poke.server.conf.HashRangeMap;

public class EqualAreaRing extends DataRing {
	private TreeMap<Long, List<DataNode>> testMap = HashRangeMap.getInstance().getRangeMap();
	private List<Long> keyList = new ArrayList<Long>();
	
	public EqualAreaRing() {
	}

	@Override
	public void createNodes(int n) {
		this.numNodes = n;
		long maxN = Long.MAX_VALUE;
		long range = maxN / numNodes;

		
		
		
		for (int i = 0; i < numNodes; i++) {
			DataNode dn = new DataNode();
			dn.setId("node-" + i);

			Ketama k = new Ketama();
			dn.setHash(k);

			long keyRange = -1;
			
			if (i != 0){
				keyRange = range * (i + 1);
				dn.setHashLimit(range * (i + 1));
			}
			else{
				keyRange = maxN;
				dn.setHashLimit(maxN);
			}
			
			//add range and corresponding node to the list
			List<DataNode> dataNodeList = new ArrayList<DataNode>();
			dataNodeList.add(dn);
			addReplica();
			testMap.put(keyRange, dataNodeList);
			keyList.add(keyRange);
			nodes.add(dn);
		}
	}
	
	private void addReplica(){
		for(Map.Entry<Long, List<DataNode>> entry : testMap.entrySet()){
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
						}
					}
				}
				
			}
			
		}
	}
	
	public static void main(String args[]){
		private TreeMap<Long, List<DataNode>> testMap = HashRangeMap.getInstance().getRangeMap();
		sampleMap.put("1", );
	}
}
