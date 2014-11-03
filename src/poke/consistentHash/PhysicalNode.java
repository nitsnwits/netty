package poke.consistentHash;

import java.util.ArrayList;
import java.util.List;

public class PhysicalNode {
	private List<DataNode> owns = new ArrayList<DataNode>();
	private List<DataNode> replica = new ArrayList<DataNode>();
	private List<HandoffNode> handoff = new ArrayList<HandoffNode>();

	public void addOwned(DataNode dn) {
		owns.add(dn);
	}

	public void addReplica(DataNode dn) {
		replica.add(dn);
	}

	public void addHandoff(DataNode dn) {
		HandoffNode hn = new HandoffNode();
		hn.setNode(dn);
		handoff.add(hn);
	}

	public List<DataNode> getOwns() {
		return owns;
	}

	public void setOwns(List<DataNode> owns) {
		this.owns = owns;
	}

	public List<DataNode> getReplica() {
		return replica;
	}

	public void setReplica(List<DataNode> replica) {
		this.replica = replica;
	}

	public List<HandoffNode> getHandoff() {
		return handoff;
	}

	public void setHandoff(List<HandoffNode> handoff) {
		this.handoff = handoff;
	}

}
