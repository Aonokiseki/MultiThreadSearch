package exe;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;


public class WordFactory {
	/*
	 * encoding,wordListSize,birthTime 实际上当作常量在用, 不需要考虑线程安全
	 * 
	 * nowTime, searchCount, errorMap 选用了线程安全的类型
	 * 
	 * wordList 本身非线程安全, 但是push和pop操作都加上了同步锁
	 * 
	 * fileList 本身非线程安全, 但是只有一个线程在修改这个变量
	 */
	private List<File> fileList;
	private String encoding;
	private List<String> wordList;
	private int wordListSize;
	private LocalDateTime birthTime;
	private AtomicLong searchCount;
	private ConcurrentHashMap<String,Integer> errorMap;
	
	public LocalDateTime getBirthTime(){
		return this.birthTime;
	}
	
	public Map<String, Integer> getErrorMap(){
		return errorMap;
	}
	public void errorMapUpdateValue(int errorCode, String errorString){
		String key = errorCode + "_" + errorString;
		int errorNumber = 0;
		if(errorMap.get(key) != null)
			errorNumber = errorMap.get(key);
		errorNumber++;
		errorMap.put(key, errorNumber);
	}
	
	public void addSearchCount(){
		this.searchCount.addAndGet(1);
	}
	public long getSearchCount(){
		return this.searchCount.get();
	}
	
	/**
	 * WordFactory构造器<br>
	 * 
	 * 用于构造一个公用的检索词存储队列<br>
	 * 一方面,由文件的读线程循环从(多个)文件中获取检索词并依次插入队列尾部<br>
	 * 另一方面,每当检索线程需要检索时,从队列的头部取出一个检索词;<br>
	 * 
	 * @param fileList 检索词文件队列
	 * @param encoding 检索词文件编码
	 * @param wordListSize 限制检索词队列
	 */
	public WordFactory(List<File> fileList, String encoding, int wordListSize){
		this.wordList = new LinkedList<String>();
		this.fileList = fileList;
		this.encoding = encoding;
		this.wordListSize = wordListSize;
		this.birthTime = LocalDateTime.now();
		this.errorMap = new ConcurrentHashMap<String, Integer>();
		this.searchCount = new AtomicLong(0);
	}
	
	public void input() throws IOException, InterruptedException{
		BufferedReader br = null;
		File current = null;
		String currentLineText = "";
		for(;;){
			current = fileList.remove(0);
			br = new BufferedReader(new InputStreamReader(new FileInputStream(current.getAbsolutePath()), this.encoding));
			while((currentLineText = br.readLine())!=null){
				if(currentLineText.isEmpty() || "".equals(currentLineText.trim()))
					continue;
				push(currentLineText);
			}
			br.close();
			/*循环使用*/
			fileList.add(current);
		}
	}
	public String output() throws InterruptedException{
		return pop();
	}
	
	private synchronized void push(String currentLineText) throws InterruptedException{
		while(wordList.size() == this.wordListSize)
			this.wait();
		this.notify();
		wordList.add(currentLineText);	
	}
	private synchronized String pop() throws InterruptedException{
		while(wordList.size() == 0)
			this.wait();
		this.notify();
		return wordList.remove(0);
	}
}
