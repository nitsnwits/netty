package poke.consistentHash;

import java.util.ArrayList;
import java.util.List;

public class HandoffNode {
	private DataNode node;

	// TOOD placeholder for actual data
	private List<String> data = new ArrayList<String>();

	public List<String> getData() {
		return data;
	}

	public void setData(List<String> data) {
		this.data = data;
	}

	public DataNode getNode() {
		return node;
	}

	public void setNode(DataNode node) {
		this.node = node;
	}

}
