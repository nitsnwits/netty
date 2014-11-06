package poke.server.conf;

import java.util.List;
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
	
	private TreeMap<Long, List<DataNode>> rangeMap = new TreeMap<Long, List<DataNode>>();
	
	public static HashRangeMap getInstance(){
		if(instance == null){
			instance = new HashRangeMap();
		}
		return instance;
	}
	
	public TreeMap<Long, List<DataNode>> getRangeMap() {
		return rangeMap;
	}

	public void setRangeMap(TreeMap<Long, List<DataNode>> rangeMap) {
		this.rangeMap = rangeMap;
	}

}
