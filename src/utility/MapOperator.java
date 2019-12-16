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
	/**
	 * 安全获取map中的value
	 * @param map
	 * @param key
	 * @param resultWhenICannotGetValue 当获取指定value失败时的返回值, 此值由使用者决定
	 * @return T
	 */
	public static <T> T safetyGet(Map<String, T> map, String key, T resultWhenICannotGetValue){
		if(map == null || map.isEmpty() || map.size() == 0 || key == null || "".equals(key.trim()))
			return resultWhenICannotGetValue;
		if(!map.containsKey(key))
			return resultWhenICannotGetValue;
		return map.get(key);
	}
}
