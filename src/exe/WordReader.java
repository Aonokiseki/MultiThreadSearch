package exe;

import java.io.IOException;

public class WordReader implements Runnable{
	WordFactory wordFactory = null;
	public WordReader(WordFactory wordFactory){
		this.wordFactory = wordFactory;
	}
	@Override
	public void run(){
		try {
			this.wordFactory.input();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
