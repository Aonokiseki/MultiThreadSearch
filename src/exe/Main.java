package exe;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import util.FileOperator;
import util.Other;

public class Main {
	private final static Logger logger = Logger.getLogger(Main.class);
	public static void main(String[] args){
		PropertyConfigurator.configure("./log4j.properties");
		int currentThreadNumber = 0;
		Thread searchThread = null;
		try {
			/*
			 * 开启读文件线程,输入检索词到队列
			 */
			Map<String, String> readSetting = FileOperator.readConfiguration("./readConfig.ini", "UTF-8");
			List<String> fileList = FileOperator.traversal(readSetting.get("word.file.directory"), null, new HashMap<String,String>());
			WordFactory wordFactory = new WordFactory(fileList, readSetting.get("word.file.encoding"), Integer.valueOf(readSetting.get("word.list.size")));
			Thread loadWord = new Thread(new WordReader(wordFactory));
			loadWord.setName("LoadWord-"+UUID.randomUUID());
			loadWord.start();
			/*
			 * 开启计时器
			 */
			Thread timer = new Thread(new Timer(wordFactory));
			timer.setName("Timer");
			timer.start();
			/*
			 * 开启检索线程
			 */
			Setting setting = Setting.build(FileOperator.readConfiguration("./searchConfig.ini", "UTF-8"));
			int maxThreadNumber = Integer.valueOf(readSetting.get("max.search.thread"));
			for(;;){
				currentThreadNumber = Other.getThreadNumber("SearchThread");
				if(currentThreadNumber >= maxThreadNumber){
					continue;
				}
				for(int i=0; i<maxThreadNumber - currentThreadNumber; i++){
					searchThread = new Thread(Searcher.build(setting, wordFactory));
					searchThread.setName("SearchThread-"+UUID.randomUUID());
					searchThread.start();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
	}
}
