package poke.server.storage.mongo;

import java.net.UnknownHostException;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;


public class Connection {
	MongoClient client;
	public DBCollection getConnection()
	{
		try {
			client = new MongoClient(new ServerAddress("localhost", 27017));
			DB database = client.getDB("lifeforce");
			DBCollection collection = database.getCollection("images");
			return collection;
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public void closeConnection()
	{
		client.close();
	}
}
