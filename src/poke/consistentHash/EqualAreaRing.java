package poke.consistentHash;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import poke.hash.Ketama;
import poke.server.conf.HashRangeMap;

public class EqualAreaRing extends DataRing {
	private TreeMap<Long, List<DataNode>> rangeMap = HashRangeMap.getInstance().getRangeMap();//new TreeMap<Long, List<DataNode>>();
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
			//dn.setId("node-" + i);
			dn.setId("" + i);

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
			//addReplica();
			rangeMap.put(keyRange, dataNodeList);
			/*if(i>numOfReplica){ 
				addReplica(dataNodeList);
			}else if(i > (numNodes - numOfReplica)){
				addReplica(dataNodeList);
			}*/
			keyList.add(keyRange);
			nodes.add(dn);
		}
		addReplica();
	}
	
	private void addReplica(){
		/*for(int i=0;i<numOfReplica;i++){
			if(i==sampleList.size()){
				break;
			}
			int nodeId = Integer.parseInt(sampleList.get(i).getId());
			if(nodeId==0){
				nodeId = numNodes;
			}
			nodes.get(--nodeId);
			System.out.println("Node ID added is -->"+nodeId);
		}*/
		
		//for(long keyRange : keyList){
		
		int j=0;
		for(int i=0; i<numNodes; i++){
			System.out.println("--------------------");
			if(i==numNodes-1){
				j=-1;
			}else{
				j = i;	
			}
			long keyRange = keyList.get(i);
			List<DataNode> nodeList = rangeMap.get(keyRange);
			j++;
			nodeList.add(nodes.get(j));
			System.out.println("Node Id is one-->"+j);
			j++;
			nodeList.add(nodes.get(j));
			System.out.println("Node Id is two-->"+j);
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
		TreeMap<Long, List<DataNode>> sampleMap = new TreeMap<Long, List<DataNode>>();
		
		DataNode dnObj1 = new DataNode();
		dnObj1.setId("1");
		
		DataNode dnObj2 = new DataNode();
		dnObj2.setId("2");
		
		DataNode dnObj3 = new DataNode();
		dnObj3.setId("3");
		
		DataNode dnObj4 = new DataNode();
		dnObj4.setId("4");
		
		DataNode dnObj5 = new DataNode();
		dnObj5.setId("5");
		
		List<DataNode> testList1 = new ArrayList<DataNode>();
		testList1.add(dnObj1);
		//testList1.add(dnObj2);
		//testList1.add(dnObj3);
		
		List<DataNode> testList2 = new ArrayList<DataNode>();
		testList2.add(dnObj2);
		//testList2.add(dnObj3);
		//testList2.add(dnObj4);
		
		List<DataNode> testList3 = new ArrayList<DataNode>();
		testList3.add(dnObj3);
		//testList3.add(dnObj4);
		//testList3.add(dnObj5);
		
		long keyVal = 0;
		
		sampleMap.put(++keyVal, testList1);
		sampleMap.put(++keyVal, testList2);
		sampleMap.put(++keyVal, testList3);
		
		EqualAreaRing eqr = new EqualAreaRing();

		eqr.getNodes().add(dnObj1);
		eqr.getNodes().add(dnObj2);
		eqr.getNodes().add(dnObj3);
		eqr.getNodes().add(dnObj4);
		eqr.getNodes().add(dnObj5);
		
		//eqr.addReplica(sampleMap);
		
		eqr.createNodes(5);
	}
}
