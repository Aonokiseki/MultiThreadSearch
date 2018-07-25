package exe;

import org.apache.log4j.Logger;

public class Timer implements Runnable{
	private final static Logger logger = Logger.getLogger(Timer.class);
	private WordFactory wf;
	
	public Timer(WordFactory wf){
		this.wf = wf;
	}
	
	@Override
	public void run(){
		double runTime = 0.0; long searchCount = 0; double average;
		long birthTime = this.wf.getBirthTime();
		for(;;){
			runTime = ((this.wf.getNowTime() - birthTime));
			searchCount = this.wf.getSearchCount();
			average = ((runTime) /  (searchCount+1));
			logger.debug("Task has run "+ (long)(runTime/1000) +"s, it has completed "+searchCount+" search quests, average respond time: "+(long)average+" (ms/count)");
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
