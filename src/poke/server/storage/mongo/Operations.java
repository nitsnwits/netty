package poke.server.storage.mongo;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

import com.google.protobuf.ByteString;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

public class Operations {
	
	public void savePhoto(UUID uuid, String name, ByteString img, long lastModified)
	{
		Connection mongo= new Connection();
		DBCollection imgStorage = mongo.getConnection();
		byte[] image=img.toByteArray();
		BasicDBObject doc = new BasicDBObject("uuid", uuid)
        .append("name", name)
        .append("data", image)
        .append("lastModified", lastModified);
		imgStorage.insert(doc);
		//imgStorage.save(doc);
		mongo.closeConnection();
	}
	
	public Map fetchPhoto(UUID uuid)
	{
		Connection mongo= new Connection();
		DBCollection imgStorage = mongo.getConnection();
		BasicDBObject query = new BasicDBObject("uuid", uuid);
		DBCursor cursor = imgStorage.find(query);
		Map result=null;
		try {
		   while(cursor.hasNext()) {
		       result=cursor.next().toMap();
		   }
		} finally {
		   cursor.close();
		}
		mongo.closeConnection();
		return result;
	}
	
	public void deletePhoto(UUID uuid)
	{
		Connection mongo= new Connection();
		DBCollection imgStorage = mongo.getConnection();
		BasicDBObject query = new BasicDBObject("uuid", uuid);
		imgStorage.remove(query);
		mongo.closeConnection();
	}

}
