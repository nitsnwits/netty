/**
 * test class to test consistent hashing for uuid
 */

package poke.hash;

import java.util.UUID;
import java.util.logging.Logger;

import poke.util.UUIDGenerator;

public class TestKetama {
	
	public static void main(String[] args) {
		Ketama k = new Ketama();
		UUID uuid = UUIDGenerator.get();
		String id = uuid.toString();
		
		//hash the key and print values
		Long key = k.hash(id);
		System.out.println("id: " + id + " key: " + key);
		//hash it again
		Long key2 = k.hash(id);
		System.out.println("id: " + id + " key: " + key);
		if (key2.longValue() == key.longValue()) {
			System.out.println("Both keys are same.");
		}
		for (int i=0; i<10; i++) {
			Long loopKey = k.hash(id);
			if (key.longValue() == loopKey.longValue()) {
				System.out.println("Looped key is same.");
			} else {
				System.out.println("blah, non-similar keys on a uuid.");
			}
		}
	}

}
