package poke.consistentHash;

import poke.hash.Ketama;
import poke.consistentHash.DataNode;

public class BasicRing extends DataRing {
	public BasicRing() {
	}

	@Override
	public void createNodes(int n) {
		this.numNodes = n;
		long maxN = Long.MAX_VALUE;
		// long range = maxN / numNodes;

		for (int i = 0; i < numNodes; i++) {
			DataNode dn = new DataNode();
			dn.setId("node-" + i); // will not create sufficient separation

			Ketama k = new Ketama();
			dn.setHash(k);

			if (i != 0)
				dn.setHashLimit(k.hash(dn.getId() + dn.hashCode()));
			else
				dn.setHashLimit(maxN);

			nodes.add(dn);
		}
	}

}
