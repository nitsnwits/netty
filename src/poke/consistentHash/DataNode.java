package poke.consistentHash;

import poke.hash.HashAlgo;

/**
 * the data node represents a storage location where data would be located
 * 
 * @author gash
 * 
 */
public class DataNode {
	private String id;

	private long hashLimit;
	private HashAlgo hash;

	// simulate data by counting keys that fall within this nodes mgmt
	private int count;

	public boolean isCoordinator(String key) {
		if (hash.hash(key) < hashLimit)
			return true;
		else
			return false;
	}

	public boolean isCoordinator(long key) {
		if (key < hashLimit)
			return true;
		else
			return false;
	}

	public boolean addIfCoordinator(String key) {
		if (isCoordinator(key)) {
			count++;
			// if (count > 4)
			// System.out.println("node " + id + " accepts another " + key +
			// " (" + hash.hash(key) + ")");
			return true;
		} else
			return false;
	}

	public boolean addIfCoordinator(long key) {
		if (isCoordinator(key)) {
			count++;
			// if (count > 4)
			// System.out.println("node " + id + " accepts another " + key +
			// " (" + hash.hash(key) + ")");
			return true;
		} else
			return false;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public long getHashLimit() {
		return hashLimit;
	}

	public void setHashLimit(long hashLimit) {
		this.hashLimit = hashLimit;
	}

	public HashAlgo getHash() {
		return hash;
	}

	public void setHash(HashAlgo hash) {
		this.hash = hash;
	}

}
