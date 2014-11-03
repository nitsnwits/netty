package poke.consistentHash;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public abstract class DataRing {

	protected int numNodes;
	protected ArrayList<DataNode> nodes = new ArrayList<DataNode>();

	public DataRing() {
		super();
	}

	public abstract void createNodes(int n);

	public void addData(String key) {
		if (nodes.size() == 0)
			throw new RuntimeException("ring not initialized, no nodes found.");

		// sequential sucks so bubble-search
		long kh = nodes.get(0).getHash().hash(key);
		if (kh > 0l) {
			int half = nodes.size() / 2;
			DataNode dn = nodes.get(half);
			if (dn.getHashLimit() >= kh) {
				for (int i = 1, I = half + 1; i < I; i++) {
					if (nodes.get(i).addIfCoordinator(kh))
						return;
				}
			} else {
				//System.out.println("upper half");
				for (int i = half, I = nodes.size(); i < I; i++) {
					if (nodes.get(i).addIfCoordinator(key))
						return;
				}
			}
		} else {
			for (int i = 1, I = nodes.size(); i < I; i++) {
				if (nodes.get(i).addIfCoordinator(kh))
					return;
			}
		}

		// must be the zeroth node?
		if (!nodes.get(0).addIfCoordinator(key)) {
			System.out.println("Error: " + key + " not accepted by ring");
		}
	}

	public void printNodeRanges(File out) {
		System.out.println("Hash range: 0 - " + Integer.MAX_VALUE);
		BufferedWriter bos = null;
		try {
			bos = new BufferedWriter(new FileWriter(out));
			for (DataNode n : nodes) {
				bos.write(n.getId() + "  " + n.getHashLimit() + " " + n.getCount() + "\n");
			}
		} catch (Exception e) {
		} finally {
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e) {
				}
			}
		}
	}

	public int getNumNodes() {
		return numNodes;
	}

	public void setNumNodes(int numNodes) {
		this.numNodes = numNodes;
	}

	public ArrayList<DataNode> getNodes() {
		return nodes;
	}

}