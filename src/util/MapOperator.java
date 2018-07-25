package util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public final class MapOperator {
	private MapOperator(){}
	public enum UniqueValue{
		skip,override,error
	};
	
	/**
	 * 对一个Map<String, Double>对象做按值自然排序
	 * @param map - 待排map
	 * @return map - 排序后的map
	 */
	public static Map<String, Double> sort(Map<String, Double> map){
		ArrayList<Map.Entry<String, Double>> list = new ArrayList<Map.Entry<String, Double>>(map.entrySet());
		Collections.sort(
				list, 
				new Comparator<Map.Entry<String, Double>>(){
					@Override
					public int compare(Entry<String, Double>arg0, Entry<String, Double>arg1){
						return (int)Math.floor((arg0.getValue() - arg1.getValue()));
					}
				}
		);
		Map<String, Double> newMap = new LinkedHashMap<String, Double>();
		for (int i = 0; i < list.size(); i++) {  
            newMap.put(list.get(i).getKey(), list.get(i).getValue());  
        }  
        return newMap;  
	}
	
	/**
	 * 合并两个map
	 * @param map1
	 * @param map2
	 * @param uniqueValue 遇到重复的key则:skip-跳过|override-覆盖|error-抛出异常
	 * @return Map<String,String> 合并后的map
	 * @throws Exception - 当设置uniqueValue = error, 且有重复的key时
	 */
	public static Map<String,String> merge(Map<String,String>map1, Map<String,String>map2, UniqueValue uniqueValue) throws Exception{
		for(String s:map2.keySet()){
			if(map1.containsKey(s)){
				if(uniqueValue == UniqueValue.override){
					map1.remove(s, map1.get(s));
					map1.put(s, map2.get(s));
				}else if(uniqueValue == UniqueValue.error){
					throw new Exception("Program finds duplicate value!");
				}
			}else{
				map1.put(s, map2.get(s));
			}
		}
		return map1;
	}
	
	/**
	 * map查询指定key时是否存在非空value
	 * 
	 * @param map
	 * @param key
	 * @return boolean 结果
	 */
	public static boolean mapHasNonNullValue(Map<String,String>map, String key){
		if(map == null || map.isEmpty() || map.size() == 0 || key == null || "".equals(key.trim()) || key.isEmpty()){
			return false;
		}
		if(!map.containsKey(key)){
			return false;
		}
		if(map.get(key) == null || "".equals(map.get(key).trim()) || map.get(key).length() == 0){
			return false;
		}
		return true;
	}
}
