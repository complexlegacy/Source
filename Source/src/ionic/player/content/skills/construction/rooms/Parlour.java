package ionic.player.content.skills.construction.rooms;

import ionic.player.content.skills.construction.Room;
import ionic.player.content.skills.construction.RoomObject;

import java.util.ArrayList;
import java.util.List;

public class Parlour extends Room {

	public Parlour() {
		super(1856, 5112);
		setName("Parlour");
		
		List<RoomObject> objects = new ArrayList<RoomObject>();
		objects.add(new RoomObject(15416, 0, 1, 0, "Bookcase space"));
		objects.add(new RoomObject(15416, 7, 1, 2, "Bookcase space"));
		objects.add(new RoomObject(15416, 7, 1, 2, "Bookcase space"));
		objects.add(new RoomObject(15410, 2, 4, 2, 11, "Chair space"));
		objects.add(new RoomObject(15411, 5, 4, 1, 11, "Chair space"));
		objects.add(new RoomObject(15412, 4, 3, 2, "Chair space"));
		objects.add(new RoomObject(15418, 3, 7, 1, "Fireplace space"));
		
		objects.add(new RoomObject(15313, 0, 4, 0, 0, "Door hotspot"));
		objects.add(new RoomObject(15314, 0, 3, 0, 0, "Door hotspot"));
		objects.add(new RoomObject(15313, 7, 4, 2, 0, "Door hotspot"));
		objects.add(new RoomObject(15314, 7, 3, 2, 0, "Door hotspot"));
		objects.add(new RoomObject(15313, 4, 0, 3, 0, "Door hotspot"));
		objects.add(new RoomObject(15314, 3, 0, 3, 0, "Door hotspot"));
		
		objects.add(new RoomObject(15263, 2, 0, 0, 5, "Curtain"));
		objects.add(new RoomObject(15263, 5, 0, 0, 5, "Curtain"));
		objects.add(new RoomObject(15263, 2, 7, 0, 5, "Curtain"));
		objects.add(new RoomObject(15263, 5, 7, 0, 5, "Curtain"));
		objects.add(new RoomObject(15263, 7, 2, 0, 5, "Curtain"));
		objects.add(new RoomObject(15263, 7, 5, 0, 5, "Curtain"));
		objects.add(new RoomObject(15263, 0, 2, 0, 5, "Curtain"));
		objects.add(new RoomObject(15263, 0, 5, 0, 5, "Curtain"));
		
		/*for (int x = 0; x < 6; x++) {
			for (int y = 0; y < 4; y++) {
				objects.add(new RoomObject(15264, 1 + x, 1 + y, 0, 22, "Rug space"));
			}
		}*/
		setObjects(objects);
		
		List<RoomObject> globalReplace = new ArrayList<RoomObject>();
		globalReplace.add(new RoomObject(13099, 2, 0, 3, 0, "Window"));
		globalReplace.add(new RoomObject(13099, 5, 0, 3, 0, "Window"));
		globalReplace.add(new RoomObject(13099, 2, 7, 1, 0, "Window"));
		globalReplace.add(new RoomObject(13099, 5, 7, 1, 0, "Window"));
		globalReplace.add(new RoomObject(13099, 7, 2, 2, 0, "Window"));
		globalReplace.add(new RoomObject(13099, 7, 5, 2, 0, "Window"));
		globalReplace.add(new RoomObject(13099, 0, 2, 0, 0, "Window"));
		globalReplace.add(new RoomObject(13099, 0, 5, 0, 0, "Window"));
		globalReplace.add(new RoomObject(-1, 2, 0, 0, 5, "Curtain"));
		globalReplace.add(new RoomObject(-1, 5, 0, 0, 5, "Curtain"));
		globalReplace.add(new RoomObject(-1, 2, 7, 0, 5, "Curtain"));
		globalReplace.add(new RoomObject(-1, 5, 7, 0, 5, "Curtain"));
		globalReplace.add(new RoomObject(-1, 7, 2, 0, 5, "Curtain"));
		globalReplace.add(new RoomObject(-1, 7, 5, 0, 5, "Curtain"));
		globalReplace.add(new RoomObject(-1, 0, 2, 0, 5, "Curtain"));
		globalReplace.add(new RoomObject(-1, 0, 5, 0, 5, "Curtain"));
		setGlobalReplacementObjects(globalReplace);
	}
	
}
