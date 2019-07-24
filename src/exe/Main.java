package exe;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import utility.FileOperator;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class Main {
	
	private final static Logger logger = Logger.getLogger(Main.class);
	private final static String READ_CONFIG_PATH = "./config/readConfig.ini";
	private final static String SEARCH_CONFIG_PATH = "./config/searchConfig.ini";
	private final static String LOG4J_PROPERTY_PATH = "./config/log4j.properties";
	private final static String CHARACTOR_WORD_FILE_DIRECTORY = "word.file.directory";
	private final static String CHARACTOR_WORD_FILE_ENCODING = "word.file.encoding";
	private final static String CHARACOTR_MAX_SEARCH_THREAD = "max.search.thread";
	private final static String CHARACTOR_WORD_LIST_SIZE = "word.list.size";
	private final static String DEFAULT_ENCODING = "UTF-8";
	
	public static void main(String[] args){
		PropertyConfigurator.configure(LOG4J_PROPERTY_PATH);
		try {
			/*读取配置文件, */
			Map<String, String> readSetting = FileOperator.readConfiguration(READ_CONFIG_PATH, DEFAULT_ENCODING);
			Setting setting = Setting.build(FileOperator.readConfiguration(SEARCH_CONFIG_PATH, DEFAULT_ENCODING));
			/*构造检索词工厂*/
			List<File> fileList = FileOperator.traversal(readSetting.get(CHARACTOR_WORD_FILE_DIRECTORY), null, new HashMap<String,String>());
			WordFactory wordFactory = new WordFactory(fileList, readSetting.get(CHARACTOR_WORD_FILE_ENCODING), Integer.valueOf(readSetting.get(CHARACTOR_WORD_LIST_SIZE)));
			/* 开启读文件线程,输入检索词到队列 */
			new Thread(new WordReader(wordFactory)).start();
			/* 开启计时器线程 */
			new Thread(new Timer(wordFactory)).start();
			/* 开启检索线程池 */
			int maxThreadNumber = Integer.valueOf(readSetting.get(CHARACOTR_MAX_SEARCH_THREAD));
			ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(maxThreadNumber, maxThreadNumber, 0, TimeUnit.MICROSECONDS, new LinkedBlockingQueue<Runnable>());
			while(true){
				threadPoolExecutor.submit(Searcher.build(setting, wordFactory));
				waitOneMillSecond();
			}	
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
	}
	
	public static void waitOneMillSecond(){
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
