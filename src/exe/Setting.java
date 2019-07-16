package exe;

import java.util.Map;
import utility.MapOperator;
import com.trs.hybase.client.params.SearchParams;


public class Setting {
	/**
	 * 检索类型<br>
	 * Select - 条件检索<br>
	 * Category - 分类统计<br>
	 * ExpressionQuery - 统计检索<br>
	 */
	public static enum SearchType{
		Select,Category,ExpressionQuery;
	};
	
	private Setting(String[] hostArray,
					  String user,
					  String password,
					  String database,
					  SearchParams sp,
					  int returnNumber,
					  boolean resultIsDisplay,
					  boolean displayNumberFound,
					  SearchType searchType,
					  String defaultSearchColumn,
					  String categoryColumn,
					  String expressionQueryExpression,
					  String expressionQueryColumn
			){
		this.hostArray = hostArray;
		this.user = user;
		this.password = password;
		this.databaseName = database;
		this.sp = sp;
		this.returnNumber = returnNumber;
		this.resultIsDisplay = resultIsDisplay;
		this.displayNumberFound = displayNumberFound;
		this.searchType = searchType;
		this.defaultSearchColumn = defaultSearchColumn;
		this.categoryColumn = categoryColumn;
		this.expressionQueryExpression = expressionQueryExpression;
		this.expressionQueryColumn = expressionQueryColumn;
	}
	
	public static Setting build(Map<String,String> config){
		String[] hostArray = new String[]{"http://127.0.0.1:5555"};
		if(MapOperator.mapHasNonNullValue(config, "host.name")){
			hostArray = config.get("host.name").split(";");
			if(hostArray.length == 0){
				hostArray = new String[]{"http://127.0.0.1:5555"};
			}
		}
		String user = "admin";
		if(MapOperator.mapHasNonNullValue(config, "user.name")){
			user = config.get("user.name");
		}
		String password = "trsadmin";
		if(MapOperator.mapHasNonNullValue(config, "user.password")){
			password = config.get("user.password");
		}
		String databaseName = "demo";
		if(MapOperator.mapHasNonNullValue(config, "database.name")){
			databaseName = config.get("database.name");
		}
		SearchParams sp = new SearchParams();
		if(MapOperator.mapHasNonNullValue(config, "search.local.prior")){
			sp.setProperty("search.local.prior", config.get("search.local.prior"));
		}
		if(MapOperator.mapHasNonNullValue(config, "search.match.max")){
			sp.setProperty("search.match.max", config.get("search.match.max"));
		}
		if(MapOperator.mapHasNonNullValue(config, "search.match.rate")){
			sp.setProperty("search.match.rate", config.get("search.match.rate"));
		}
		if(MapOperator.mapHasNonNullValue(config, "search.phrase.wildcard")){
			sp.setProperty("search.phrase.wildcard", config.get("search.phrase.wildcard"));
		}
		if(MapOperator.mapHasNonNullValue(config, "consume.expr.timeout")){
			sp.setProperty("consume.expr.timeout", config.get("consume.expr.timeout"));
		}
		if(MapOperator.mapHasNonNullValue(config, "allow.consume.expr")){
			sp.setProperty("allow.consume.expr", config.get("allow.consume.expr"));
		}
		if(MapOperator.mapHasNonNullValue(config, "result.read.column")){
			sp.setReadColumns(config.get("result.read.column"));
		}
		if(MapOperator.mapHasNonNullValue(config, "result.sort.method")){
			sp.setSortMethod(config.get("result.sort.method"));
		}
		if(MapOperator.mapHasNonNullValue(config, "search.time.out")){
			sp.setTimeOut(Long.valueOf(config.get("search.time.out")));
		}
		int returnNumber = 10;
		if(MapOperator.mapHasNonNullValue(config, "result.return.number")){
			returnNumber = Integer.valueOf(config.get("result.return.number"));
		}
		boolean resultIsDisplay = false;
		if(MapOperator.mapHasNonNullValue(config, "result.is.display")){
			resultIsDisplay = Boolean.valueOf(config.get("result.is.display"));
		}
		boolean displayNumberFound = false;
		if(MapOperator.mapHasNonNullValue(config, "display.number.found")){
			displayNumberFound = Boolean.valueOf(config.get("display.number.found"));
		}
		SearchType searchType = SearchType.Select;
		if(MapOperator.mapHasNonNullValue(config, "search.type")){
			if(config.get("search.type").equals("category")){
				searchType = SearchType.Category;
			}else if(config.get("search.type").equals("expressionQuery")){
				searchType = SearchType.ExpressionQuery;
			}else{
				searchType = SearchType.Select;
			}
		}
		String defaultSearchColumn = "";
		if(MapOperator.mapHasNonNullValue(config, "default.search.column")){
			defaultSearchColumn = config.get("default.search.column");
		}
		String categoryColumn = "";
		if(MapOperator.mapHasNonNullValue(config, "category.column")){
			categoryColumn = config.get("category.column");
		}
		if((searchType == SearchType.Category) && (defaultSearchColumn == "" || categoryColumn == "")){
			throw new NullPointerException("Can not find defaultSearchColumn or CategoryColumn!");
		}
		String expressionQueryExpression = "";
		if(MapOperator.mapHasNonNullValue(config, "expression.query.expression")){
			expressionQueryExpression = config.get("expression.query.expression");
		}
		String expressionQueryColumn = "";
		if(MapOperator.mapHasNonNullValue(config, "expression.query.return")){
			expressionQueryColumn = config.get("expression.query.return");
		}
		if((searchType == SearchType.ExpressionQuery) && (expressionQueryExpression == "" || expressionQueryColumn == "")){
			throw new NullPointerException("Can not find expressionQueryExpression or expressionQueryColumn!");
		}
		
		return new Setting(hostArray, 
							user, 
							password, 
							databaseName, 
							sp, 
							returnNumber, 
							resultIsDisplay, 
							displayNumberFound,
							searchType, 
							defaultSearchColumn,
							categoryColumn,
							expressionQueryExpression,
							expressionQueryColumn
							);
	}
	
	
	/*主机列表*/
	private String[] hostArray;
	/*用户名*/
	private String user;
	/*密码*/
	private String password;
	/*数据库名*/
	private String databaseName;
	/*检索参数*/
	private SearchParams sp;
	/*希望返回的结果集大小*/
	private int returnNumber;
	/*是否屏幕打印结果*/
	private boolean resultIsDisplay;
	/*子开关, 当屏幕打印结果时, 是否只打印命中记录数*/
	private boolean displayNumberFound;
	/*检索类型*/
	private SearchType searchType;
	/*默认检索字段*/
	private String defaultSearchColumn;
	/*分类字段*/
	private String categoryColumn;
	/*统计检索表达式*/
	private String expressionQueryExpression;
	/*统计检索取值字段*/
	private String expressionQueryColumn;
	
	public String[] getHostArray(){
		return this.hostArray;
	}
	public String getARandomHost(){
		return this.hostArray[(int)(Math.random()*hostArray.length)];
	}
	public String getUser(){
		return this.user;
	}
	public String getPassword(){
		return this.password;
	}
	public String getDatabase(){
		return this.databaseName;
	}
	public SearchParams getSearchParams(){
		return this.sp;
	}
	public int getReturnNumber(){
		return this.returnNumber;
	}
	public boolean getResultIsDisplay(){
		return this.resultIsDisplay;
	}
	public boolean displayNumberFound(){
		return this.displayNumberFound;
	}
	public SearchType getSearchType(){
		return this.searchType;
	}
	public String getDefaultSearchColumn(){
		return this.defaultSearchColumn;
	}
	public String getCategoryColumn(){
		return this.categoryColumn;
	}
	public String getExpressionQueryExpression(){
		return this.expressionQueryExpression;
	}
	public String getExpressionQueryColumn(){
		return this.expressionQueryColumn;
	}
	
}
