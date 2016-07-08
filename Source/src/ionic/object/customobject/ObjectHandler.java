package ionic.object.customobject;

import ionic.player.Client;
import ionic.player.Player;
import ionic.player.PlayerHandler;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Sanity
 */

public class ObjectHandler {

	
	public static List<Objects> globalObjects = new ArrayList<Objects>();	
	
	public ObjectHandler() 
	{ 
	}
	/**
	* Adds object to list
	**/
	public static void addObject(Objects object) {
		globalObjects.add(object);
	}
	
	/**
	* Removes object from list
	**/
	public static void removeObject(Objects object) {
		globalObjects.remove(object);
	}
	public static void setFire(Client c, int id, int x, int y) 
	{ //firemaking to appear
		Objects OBJECT = new Objects(id, x, y, 0, 0, 10, 0);
		if(id == -1) 
		{
			removeObject(OBJECT);
		} else 
		{
			addObject(OBJECT);
		}
		ObjectHandler.placeObject(OBJECT);
	}
	
	/**
	* Update objects when entering a new region or logging in
	**/
	public void updateObjects(Client c) { 
		for(Objects o : globalObjects) {
			if(c != null) {
				if(c.heightLevel == o.getObjectHeight() && o.objectTicks == 0) {
					if (c.distanceToPoint(o.getObjectX(), o.getObjectY()) <= 60) {
						c.getPA().object(o.getObjectId(), o.getObjectX(), o.getObjectY(), o.getObjectFace(), o.getObjectType());
					}
				}		
			}
		}
		if (c.distanceToPoint(2961, 3389) <= 60) {
			c.getPA().object(6552, 2961, 3389, -1, 10);		
		}
	}
	
	public static void removeAllObjects(Objects o) {
		for (Objects s : globalObjects) { 
			if (s.getObjectX() == o.objectX && s.getObjectY() == o.objectY && s.getObjectHeight() == o.getObjectHeight()) {
				globalObjects.remove(s);
				break;
			}
		}
	}
	
	
	/**
	* Creates the object for anyone who is within 60 squares of the object
	**/
	public static void placeObject(Objects o) {
		for (Player p : PlayerHandler.players){
			if(p != null) {
			Client person = (Client)p;
				if(person != null){
					removeAllObjects(o);
					globalObjects.add(o);
					if(person.heightLevel == o.getObjectHeight() && o.objectTicks == 0) {
						if (person.distanceToPoint(o.getObjectX(), o.getObjectY()) <= 60) {
							person.getPA().object(o.getObjectId(), o.getObjectX(), o.getObjectY(), o.getObjectFace(), o.getObjectType());
						}
					}		
				}
			}
		}
	}
}
