package exe;


import org.apache.log4j.Logger;

import util.Other;

import com.trs.hybase.client.TRSConnection;
import com.trs.hybase.client.TRSException;
import com.trs.hybase.client.TRSRecord;
import com.trs.hybase.client.TRSResultSet;
import com.trs.hybase.client.params.ConnectParams;

import exe.Setting.SearchType;

public class Searcher implements Runnable{
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
	/*
	 * 构造器私有
	 */
	private Searcher(){}
	
	public static Searcher build(Setting setting, WordFactory wordFactory){
		Searcher searcher = new Searcher();
		searcher.wordFactory = wordFactory;
		searcher.setting = setting;
		searcher.conn = new TRSConnection(setting.getARandomHost(), setting.getUser(), setting.getPassword(), new ConnectParams());
		return searcher;
	}
	
	@Override
	public void run(){
		TRSResultSet resultSet = null;
		try{
			String query = "\""+this.wordFactory.output()+"\"";
			if(this.setting.getSearchType() == SearchType.Category){
				 resultSet = this.conn.categoryQuery(this.setting.getDatabase(), query, this.setting.getDefaultSearchColumn(), this.setting.getCategoryColumn(), this.setting.getReturnNumber());
				 this.wordFactory.addSearchCount();
				 this.wordFactory.updateNowTime();
				 if(this.setting.getResultIsDisplay()){
					 logger.debug(resultSet.getCategoryMap());
				 }
			}else if(this.setting.getSearchType() == SearchType.ExpressionQuery){
				 resultSet = this.conn.expressionQuery(this.setting.getDatabase(), query, this.setting.getExpressionQueryExpression(), this.setting.getSearchParams());
				 this.wordFactory.addSearchCount();
				 this.wordFactory.updateNowTime();
				 if(this.setting.getResultIsDisplay()){
					 logger.debug(resultSet.getExpressionResult(this.setting.getExpressionQueryColumn()));
				 }
			}else{
				 resultSet = this.conn.executeSelect(this.setting.getDatabase(),  query, 0, this.setting.getReturnNumber(), this.setting.getSearchParams());
				 this.wordFactory.addSearchCount();
				 this.wordFactory.updateNowTime();
				 logger.debug(System.lineSeparator()+
						 "<SearchExperession>= "+query+System.lineSeparator()+
						 "<TRSResultSet.getNumFound>= "+resultSet.getNumFound()+System.lineSeparator()+
						 "<TRSResultSet.size>= "+resultSet.size());
				 if(this.setting.getResultIsDisplay()){
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
		}catch(TRSException ex){
			ex.printStackTrace();
			logger.error(ex.getErrorCode()+" : "+ex.getErrorString()+System.lineSeparator()+Other.exceptionToStackTrace(ex));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}finally{
			this.conn.close();
		}
	}
}
