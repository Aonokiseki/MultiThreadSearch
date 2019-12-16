package exe;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import utility.ChronoOperator;

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
		long runTime = 0; 
		long searchCount = 0; 
		double average;
		LocalDateTime birth = this.wf.getBirthTime();
		for(;;){
			cycleCount++;
			runTime = ChronoOperator.timeDifference(birth, LocalDateTime.now()).getSeconds();
			searchCount = this.wf.getSearchCount();
			if(searchCount > 0){
				average = ((double)(runTime))/((double)(searchCount));
				logger.debug("Total task: "+searchCount+", total time(s): "+runTime+", average respond time(s): "+average);
			}else{
				logger.debug("Total task: 0, total time(s): "+runTime);
			}
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
