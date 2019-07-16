package exe;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

public class Timer implements Runnable{
	private final static Logger logger = Logger.getLogger(Timer.class);
	private WordFactory wf;
	
	public Timer(WordFactory wf){
		this.wf = wf;
	}
	
	@Override
	public void run(){
		int cycleCount = 0;
		Map<String, Integer> errorMap = null;
		StringBuilder sb = new StringBuilder();
		double runTime = 0.0; 
		long searchCount = 0; 
		double average;
		long birthTime = this.wf.getBirthTime();
		for(;;){
			cycleCount++;
			runTime = this.wf.getNowTime() - birthTime;
			searchCount = this.wf.getSearchCount();
			if(searchCount == 0)
				average = Double.NaN;
			else
				average = runTime / (searchCount);
			logger.debug("Task has run "+ (long)(runTime/1000) +"s, " +"it has completed "+searchCount+" search quests, average respond time: "+(long)average+" (ms/count)");
			if(cycleCount % 360 == 0){
				cycleCount = 1;
				errorMap = wf.getErrorMap();
				sb.delete(0, sb.length());
				for(Entry<String, Integer> e : errorMap.entrySet())
					sb.append(e.getKey() + " : " + e.getValue() + System.lineSeparator());
				logger.debug("Exceptions in total: "+System.lineSeparator() + sb.toString());
			}
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
