package utility;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.filechooser.FileSystemView;

public final class Other {
	private Other(){}
	
	public enum SizeUnit{
		B("B"),KB("KB"),MB("MB"),GB("GB"),TB("TB");
		
		private String unit;
		private SizeUnit(String unit){
			this.unit = unit;
		}
		@Override
		public String toString(){
			return this.unit;
		}
	}
	    
	private final static double ONE_KB = 1024.0;
	private final static double ONE_MB = 1048576.0;
	private final static double ONE_GB = 1073741824.0;
	private final static double ONE_TB = 1099511627776.0;
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
     * 交换数组中两个元素的位置
     * @param array
     * @param index1
     * @param index2
     */
    public static  void exchangeTwoElementOfArray(int[] array, int index1, int index2){
    	if(index1 < 0 || index1 > (array.length - 1) || index2 < 0 || index2 > (array.length - 1))
    		return;
    	int temp = array[index1];
    	array[index1] = array[index2];
    	array[index2] = temp;
    }
    /**
     * 交换数组中两个元素的位置
     * @param array
     * @param index1
     * @param index2
     */
    public static void exchangeTwoElementOfArray(double[] array, int index1, int index2){
    	if(index1 < 0 || index1 > (array.length - 1) || index2 < 0 || index2 > (array.length - 1))
    		return;
    	double temp = array[index1];
    	array[index1] = array[index2];
    	array[index2] = temp;
    }
    /**
     * 返回堆栈字符串
     * 
     * @param throwable
     * @return String 堆栈信息
     */
    public static String stackTraceToString(Throwable throwable){
    	StringWriter sw = new StringWriter();
 	    throwable.printStackTrace(new PrintWriter(sw, true));
 	    return sw.getBuffer().toString();
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
     * 交换列表中两个元素
     * @param list 目标列表
     * @param index1 第一个交换元素的位置
     * @param index2 第二个交换元素的位置
     */
    public static <T> void exchangeTwoElementOfList(List<T> list, int index1, int index2){
    	if(index1 < 0 || (index1 > list.size() - 1) || index2 < 0 || (index2 > list.size() - 1))
    		return;
    	list.add(index1, list.get(index2));
		list.add(index2 + 1, list.get(index1));
		list.remove(index1);
		list.remove(index2);
    }
    /**
     * 获取磁盘空间信息
     * @param disks 目标磁盘列表,为空时获取全部磁盘
     * @param sizeUnit 空间大小返回值的单位, 可选B|KB|MB|GB|TB
     * @return <code>Map&ltString, Map&ltString,String&gt&gt</code> <br/>
     * 外层键值对表示每个盘符和其对应的信息;<br>
     * 内层键值对表示单个盘符不同信息的结果,取值如下:<br>
     * <code>TotalSpace</code> 总空间<br>
     * <code>FreeSpace</code> 剩余空间<br>
     * <code>Free/Total</code> 二者的比值<br>
     */
    public static Map<String,Map<String,String>> diskSpaceInfo(List<String> disks, SizeUnit sizeUnit){
    	Map<String,Map<String,String>> result = new HashMap<String,Map<String,String>>();
    	Map<String,String> eachDisk = null;
    	FileSystemView fileSystemView = FileSystemView.getFileSystemView();
    	File[] files = File.listRoots();
    	for(int i=0; i<files.length; i++){
    		if(disks == null || disks.isEmpty());
    		else if(!disks.equals(files[i].getName()))
    			continue;
    		eachDisk = new HashMap<String,String>();
    		eachDisk.put("TotalSpace", FormatFileSize(files[i].getTotalSpace(), sizeUnit));
    		eachDisk.put("FreeSpace",  FormatFileSize(files[i].getFreeSpace(), sizeUnit));
    		eachDisk.put("Free/Total", new DecimalFormat("#0.00").format((double)files[i].getFreeSpace()/(double)files[i].getTotalSpace()));
    		result.put(fileSystemView.getSystemDisplayName(files[i]), eachDisk);
    	}
    	return result;
    }
    /**
     * 格式化显示磁盘空间大小, 保留2位小数
     * @param fileSize 磁盘空间
     * @param sizeUnit 空间大小返回值的单位, 可选B|KB|MB|GB|TB
     * @return
     */
    public static String FormatFileSize(double fileSize, SizeUnit sizeUnit){
    	 DecimalFormat df = new DecimalFormat("#.00");
         if(sizeUnit.equals(SizeUnit.B))
        	 return df.format(fileSize);
         if(sizeUnit.equals(SizeUnit.KB))
        	 return df.format(fileSize / ONE_KB);
         if(sizeUnit.equals(SizeUnit.MB))
        	 return df.format(fileSize/ ONE_MB);
         if(sizeUnit.equals(SizeUnit.GB))
        	 return df.format(fileSize / ONE_GB);
         return df.format(fileSize / ONE_TB);
    }
}
