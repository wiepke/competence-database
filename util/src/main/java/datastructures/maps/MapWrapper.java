package datastructures.maps;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.HashMap;

@XmlRootElement
public class MapWrapper<KEY, VALUE> {
	private HashMap<KEY, VALUE> map;

	public MapWrapper() {
	}

	public MapWrapper(HashMap<KEY, VALUE> userCourseListItemDoubleHashMap) {
		this.map = userCourseListItemDoubleHashMap;
	}

	public HashMap<KEY, VALUE> getMap() {
		return map;
	}

	public void setMap(HashMap<KEY, VALUE> map) {
		this.map = map;
	}
}
