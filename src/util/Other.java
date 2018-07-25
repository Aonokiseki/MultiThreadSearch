package util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public final class Other {
	private Other(){}
	 /**
     * 获得当前正在执行的方法名称
     */
    public static String getMethodName(){
    	StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();  
	    StackTraceElement e = stacktrace[2];  
	    String methodName = e.getMethodName();  
	    return methodName;
    }
    /**
     * 序列倒置
     * @param array 待处理序列
     */
    public static <T> void sequenceInversion(T[] array){
    	int i = -1;
    	int j = array.length;
    	T temp;
    	while(i++<j--){
    		temp = array[i];
    		array[i] = array[j];
    		array[j] = temp;
    	}
    }
    
    /**
     * 指定矩形范围内获得一个随机经纬度
     * 
     * @param maxLongitude 经度上限
     * @param minLongitude 经度下限
     * @param maxLatitude 纬度上限
     * @param minLatitude 纬度下限
     * 
     * @return Double[] 某点的经纬度, Double[0]是经度, Double[1]是纬度
     * */
    public static Double[] getRandomLongitudeAndlLatitude(double maxLongitude, 
    														   double minLongitude, 
    														   double maxLatitude,
    														   double minLatitude){
    	Double[] longitudeTwoSide = checkValue(minLongitude, maxLongitude, -180.0, 180.0);
    	Double[] latitudeTwoSide = checkValue(minLatitude, maxLatitude, -90.0, 90.0);
    	BigDecimal randomLongitude = new BigDecimal(Math.random() * (longitudeTwoSide[1] - longitudeTwoSide[0]) + longitudeTwoSide[0]);
    	BigDecimal randomLatitude = new BigDecimal(Math.random() * (latitudeTwoSide[1] - latitudeTwoSide[0]) + latitudeTwoSide[0]);
    	Double[] result = new Double[2];
    	result[0] = new Double(randomLongitude.doubleValue());
    	result[1] = new Double(randomLatitude.doubleValue());
    	return result;
    }
    
    private static Double[] checkValue(double min, double max, double minLimit, double maxLimit){
    	if(max > maxLimit || max < minLimit){
    		max = maxLimit;
    	}
    	if(min < minLimit || min > maxLimit){
    		min = minLimit;
    	}
    	if(min > max){
    		double temp = min;
    		min = max;
    		max = temp;
    	}
    	Double[] result = new Double[2];
    	result[0] = new Double(min);
    	result[1] = new Double(max);
    	return result;
    }
    
    /**
     * 返回堆栈字符串
     * 
     * @param throwable
     * @return String 堆栈信息
     */
    public static String exceptionToStackTrace(Throwable throwable){
    	StringBuilder stringBuilder = new StringBuilder();
    	String lineSeparator = System.lineSeparator();
    	StackTraceElement[] traces = throwable.getStackTrace();
    	for(StackTraceElement ste : traces){
    		stringBuilder.append(ste.toString()+lineSeparator);
    	}
    	return stringBuilder.toString();
    }
    
    /**
     * 获取当前正在运行的线程列表
     * 
     * @return Thread[]
     */
    public static Thread[] getListThreads(){
		Thread[] lstThreads;
		ThreadGroup currentGroup = Thread.currentThread().getThreadGroup();
		int noThreads = currentGroup.activeCount();
	    lstThreads = new Thread[noThreads];
	    currentGroup.enumerate(lstThreads);
	    return lstThreads;
	}
    
    /**
     * 将数组转换为List
     * 
     * @param array 待转换的数组
     * @return List(T) 转换后的List
     */
    public static <T> List<T> arrayToList(T[] array){
    	List<T> list = new ArrayList<T>();
    	for(int i=0; i<array.length; i++){
    		list.add(array[i]);
    	}
    	return list;
    }
    
    public static int getThreadNumber(String threadName){
		 Thread[] threadList = Other.getListThreads();
		 int count = 0;
		 for(int i=0; i<threadList.length; i++){
			 if(threadList[i] == null || threadList[i].getName()== null){
				 continue;
			 }
			 if(threadList[i].getName().contains(threadName)){
				 count++;
			 }
		 }
		 return count;
	 }
}
