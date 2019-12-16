package exe;

import java.util.concurrent.atomic.AtomicLong;
import org.apache.log4j.Logger;
import utility.Other;
import com.trs.hybase.client.TRSConnection;
import com.trs.hybase.client.TRSException;
import com.trs.hybase.client.TRSRecord;
import com.trs.hybase.client.TRSResultSet;
import com.trs.hybase.client.params.ConnectParams;


public class Searcher implements Runnable{
	private static AtomicLong count = new AtomicLong(0);
	private final long id = count.getAndAdd(1);
	
	private static Logger logger = Logger.getLogger(Searcher.class);
	/* TRSConnection */
	private TRSConnection conn = null;
	public TRSConnection getTRSConnection(){
		return this.conn;
	}
	/* 检索词的中转站 */
	private WordFactory wordFactory = null;
	public WordFactory getWordFactory(){
		return this.wordFactory;
	}
	/* 配置文件设置 */
	private Setting setting = null;
	public Setting getSetting(){
		return this.setting;
	}
	public long id(){
		return this.id;
	}
	/*
	 * 构造器私有
	 */
	private Searcher(){}
	
	public static Searcher build(Setting setting, WordFactory wordFactory){
		Searcher searcher = new Searcher();
		searcher.wordFactory = wordFactory;
		searcher.setting = setting;
		searcher.conn = new TRSConnection(setting.getHosts(), setting.getUser(), setting.getPassword(), new ConnectParams());
		return searcher;
	}
	
	@Override
	public void run(){
		String query = "*:*";
		try{
			String output = this.wordFactory.output();
			if(output == null || "".equals(output))
				logger.warn("id=="+this.id + ", wordFactory.output() == null || wordFactory.output().isEmpty()");
			else
				query = output;
			switch(setting.getSearchType()){
				case Category: category(query); break;
				case ExpressionQuery: expressionQuery(query); break;
				case Select: executeSelect(query); break;
				default: executeSelect(query); break;
			}
		}catch(TRSException ex){
			ex.printStackTrace();
			wordFactory.errorMapUpdateValue(ex.getErrorCode(), ex.getErrorString());
			logger.error(
					"id=="+this.id+"searchExpression=="+query+", errorCode=="+ex.getErrorCode()+", errorMsg=="+ex.getErrorString()+System.lineSeparator()+
					Other.stackTraceToString(ex)+System.lineSeparator());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}finally{
			this.conn.close();
			Thread.yield();
		}
	}
	
	/*分类统计*/
	public void category(String query) throws TRSException{
		 TRSResultSet resultSet = this.conn.categoryQuery(this.setting.getDatabase(), query, this.setting.getDefaultSearchColumn(), this.setting.getCategoryColumn(), this.setting.getReturnNumber());
		 this.wordFactory.addSearchCount();
		 if(this.setting.getResultIsDisplay()){
			 logger.debug("id=="+this.id + ", resultSet.getCategoryMap()==" + resultSet.getCategoryMap());
		 }
	}
	/*统计检索*/
	public void expressionQuery(String query) throws TRSException{
		 TRSResultSet resultSet = this.conn.expressionQuery(this.setting.getDatabase(), query, this.setting.getExpressionQueryExpression(), this.setting.getSearchParams());
		 this.wordFactory.addSearchCount();
		 if(this.setting.getResultIsDisplay()){
			 logger.debug("id=="+this.id+", resultSet.getExpressionResult()=="+resultSet.getExpressionResult(this.setting.getExpressionQueryColumn()));
		 }
	}
	/*普通检索*/
	public void executeSelect(String query) throws TRSException{
		 TRSResultSet resultSet = this.conn.executeSelect(this.setting.getDatabase(),  query, 0, this.setting.getReturnNumber(), this.setting.getSearchParams());
		 this.wordFactory.addSearchCount();
		 if(this.setting.getResultIsDisplay()){
			 if(this.setting.displayNumberFound()){
				 logger.debug("id=="+this.id+", searchExpression=="+query+", resultSet.getNumFound()=="+resultSet.getNumFound());
				 return;
			 }
			 printPerRecord(resultSet);
		 }
	}
	/*普通检索-打印结果集*/
	private void printPerRecord(TRSResultSet resultSet) throws TRSException{
		TRSRecord record = null;
		 String[] columns = null;
		 StringBuilder sb = new StringBuilder();
		 while(resultSet.moveNext()){
			record = resultSet.get();
			columns = record.getColumnNames();
			sb.append(System.lineSeparator()+"================"+System.lineSeparator());
			for(int i=0; i<columns.length; i++){
				sb.append(columns[i]+" : "+record.getString(columns[i])+System.lineSeparator());
			}
			sb.append(System.lineSeparator());
		}
		logger.debug(sb.toString());
	}
}
