package exe;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;


public class WordFactory {
	private List<String> fileList;
	private String encoding;
	private List<String> wordList;
	private int wordListSize;
	private long birthTime;
	private long nowTime;
	private long searchCount;
	
	public long getBirthTime(){
		return this.birthTime;
	}
	public void updateNowTime(){
		this.nowTime = System.currentTimeMillis();
	}
	public long getNowTime(){
		return this.nowTime;
	}
	
	public void addSearchCount(){
		this.searchCount++;
	}
	public long getSearchCount(){
		return this.searchCount;
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
	public WordFactory(List<String> fileList, String encoding, int wordListSize){
		this.wordList = new LinkedList<String>();
		this.fileList = fileList;
		this.encoding = encoding;
		this.wordListSize = wordListSize;
		this.birthTime = System.currentTimeMillis();
		this.nowTime = System.currentTimeMillis();
	}
	
	public void input() throws IOException, InterruptedException{
		BufferedReader br = null;
		String fileAbsolutePath = "";
		String currentLineText = "";
		for(;;){
			fileAbsolutePath = fileList.remove(0);
			br = new BufferedReader(new InputStreamReader(new FileInputStream(fileAbsolutePath), this.encoding));
			while((currentLineText = br.readLine())!=null){
				if(currentLineText.isEmpty() || "".equals(currentLineText.trim())){
					continue;
				}
				push(currentLineText);
			}
			br.close();
			/*循环使用*/
			fileList.add(fileAbsolutePath);
		}
	}
	public String output() throws InterruptedException{
		return pop();
	}
	
	private synchronized void push(String currentLineText) throws InterruptedException{
		while(wordList.size() == this.wordListSize){
			this.wait();
		}
		this.notify();
		this.nowTime = System.currentTimeMillis();
		wordList.add(currentLineText);	
	}
	private synchronized String pop() throws InterruptedException{
		while(wordList.size() == 0){
			this.wait();
		}
		this.notify();
		this.nowTime = System.currentTimeMillis();
		return wordList.remove(0);
	}
}
