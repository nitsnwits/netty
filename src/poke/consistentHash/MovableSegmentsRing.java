package poke.consistentHash;

import java.util.TreeMap;

import poke.hash.MurmurHash128;
import algorithms.BST;

/**
 * WORK IN PROGRESS
 * 
 * This abstraction will allow the segmented hash space to be re-assigned to
 * different nodes, effectively creating balanced storage.
 * 
 * @author gash
 * 
 */
public class MovableSegmentsRing extends DataRing {
	public static final long sMaxN = (long) Math.pow(2, 128);
	// public static final long sMaxN = 9999999999l; // dependent on key length.

	protected TreeMap<Long, Integer> hash2List = new TreeMap<Long, Integer>();
	protected BST<Long, DataNode> btree = new BST<Long, DataNode>();

	public MovableSegmentsRing() {
	}

	@Override
	public void createNodes(int n) {
		System.out.println("building segments...");
		long st = System.currentTimeMillis();
		this.nodes.clear();
		this.numNodes = n;

		long range = sMaxN / numNodes;

		for (int i = 0; i < numNodes; i++) {
			DataNode dn = new DataNode();
			dn.setId("node-" + i);

			// Ketama k = new Ketama();
			MurmurHash128 m = new MurmurHash128();
			dn.setHash(m);

			if (i != 0)
				dn.setHashLimit(range * (i + 1));
			else
				dn.setHashLimit(sMaxN);

			nodes.add(dn);
			// hash2List.put(dn.getHashLimit(), i);
			btree.put(dn.getHashLimit(), dn);
		}

		long et = System.currentTimeMillis();
		System.out.println("Segments built in " + (et - st) + " msec");
	}
}
