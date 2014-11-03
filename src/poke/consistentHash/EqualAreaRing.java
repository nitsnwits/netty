package poke.consistentHash;

import poke.hash.Ketama;

public class EqualAreaRing extends DataRing {
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

			if (i != 0)
				dn.setHashLimit(range * (i + 1));
			else
				dn.setHashLimit(maxN);

			nodes.add(dn);
		}
	}
}
