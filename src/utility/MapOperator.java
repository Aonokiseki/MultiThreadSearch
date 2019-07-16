package utility;

import java.util.Map;

public final class MapOperator {
	private MapOperator(){}
	/**
	 * map是否有指定key的非空value
	 * 
	 * @param map
	 * @param key
	 * @return boolean
	 */
	public static boolean mapHasNonNullValue(Map<String,String> map, String key){
		if(map == null || map.isEmpty() || map.size() == 0 || key == null || "".equals(key.trim()))
			return false;
		if(!map.containsKey(key))
			return false;
		if(map.get(key) == null || "".equals(map.get(key).trim()))
			return false;
		return true;
	}
}
