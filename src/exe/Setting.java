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
		
		public static SearchType build(String name){
			if("category".equals(name))
				return SearchType.Category;
			if("expressionQuery".equals(name))
				return SearchType.ExpressionQuery;
			return SearchType.Select;
		}
	};
	
	private final static Setting SETTING = new Setting();
	
	private Setting(){}
	
	public static Setting build(Map<String,String> config){
		String hosts = MapOperator.safetyGet(config, "host.name", "http://127.0.0.1:5555");
		String user = MapOperator.safetyGet(config, "user.name", "admin");
		String password = MapOperator.safetyGet(config, "user.password", "trsadmin");
		String databaseName = MapOperator.safetyGet(config, "database.name", "");
		SearchParams sp = new SearchParams();
		sp.setProperty("search.local.prior", MapOperator.safetyGet(config, "search.local.prior", ""));
		sp.setProperty("search.match.max", MapOperator.safetyGet(config, "search.match.max", ""));
		sp.setProperty("search.match.rate", MapOperator.safetyGet(config, "search.match.rate", ""));
		sp.setProperty("search.phrase.wildcard", MapOperator.safetyGet(config, "search.phrase.wildcard", ""));
		sp.setProperty("consume.expr.timeout", MapOperator.safetyGet(config, "consume.expr.timeout", ""));
		sp.setProperty("allow.consume.expr", MapOperator.safetyGet(config, "allow.consume.expr", ""));
		sp.setProperty("search.read.prior", MapOperator.safetyGet(config, "search.read.prior", ""));
		if(MapOperator.mapHasNonNullValue(config, "result.read.column"))   
			sp.setReadColumns(config.get("result.read.column"));
		if(MapOperator.mapHasNonNullValue(config, "result.sort.method"))
			sp.setSortMethod(config.get("result.sort.method"));
		if(MapOperator.mapHasNonNullValue(config, "search.time.out"))
			sp.setTimeOut(Long.valueOf(config.get("search.time.out")));
		int returnNumber = Integer.valueOf(MapOperator.safetyGet(config, "result.return.number", "10"));
		boolean resultIsDisplay = Boolean.parseBoolean(MapOperator.safetyGet(config, "result.is.display", "false"));
		boolean displayNumberFound = Boolean.parseBoolean(MapOperator.safetyGet(config, "display.number.found", "false"));
		SearchType searchType = SearchType.build(MapOperator.safetyGet(config, "search.type", "select"));
		String defaultSearchColumn = MapOperator.safetyGet(config, "default.search.column", "");
		String categoryColumn = MapOperator.safetyGet(config, "category.column", "");
		if((searchType == SearchType.Category) && ("".equals(defaultSearchColumn) || "".equals(categoryColumn)))
			throw new NullPointerException("Can not find defaultSearchColumn or CategoryColumn!");
		String expressionQueryExpression = MapOperator.safetyGet(config, "expression.query.expression", "");
		String expressionQueryColumn = MapOperator.safetyGet(config, "expression.query.return", "");
		if((searchType == SearchType.ExpressionQuery) && ("".equals(expressionQueryExpression) || "".equals(expressionQueryColumn)))
			throw new NullPointerException("Can not find expressionQueryExpression or expressionQueryColumn!");	
		
		return SETTING.setHosts(hosts)
					   .setUser(user)
					   .setPassword(password)
					   .setDatabase(databaseName)
					   .setSearchParams(sp)
					   .setReturnNumber(returnNumber)
					   .setResultIsDisplay(resultIsDisplay)
					   .setDisplayNumberFound(displayNumberFound)
					   .setSearchType(searchType)
					   .setDefaultSearchColumn(defaultSearchColumn)
					   .setCategoryColumn(categoryColumn)
					   .setExpressionQueryExpressions(expressionQueryExpression)
					   .setExpressionQueryColumn(expressionQueryColumn);
	}
	
	
	/*主机列表*/
	private String hosts;
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
	
	public String getHosts(){
		return this.hosts;
	}
	public Setting setHosts(String hosts){
		this.hosts = hosts;
		return this;
	}
	public String getUser(){
		return this.user;
	}
	public Setting setUser(String user){
		this.user = user;
		return this;
	}
	public String getPassword(){
		return this.password;
	}
	public Setting setPassword(String password){
		this.password = password;
		return this;
	}
	public String getDatabase(){
		return this.databaseName;
	}
	public Setting setDatabase(String databaseName){
		this.databaseName = databaseName;
		return this;
	}
	public SearchParams getSearchParams(){
		return this.sp;
	}
	public Setting setSearchParams(SearchParams sp){
		this.sp = sp;
		return this;
	}
	public int getReturnNumber(){
		return this.returnNumber;
	}
	public Setting setReturnNumber(int returnNumber){
		this.returnNumber = returnNumber;
		return this;
	}
	public boolean getResultIsDisplay(){
		return this.resultIsDisplay;
	}
	public Setting setResultIsDisplay(boolean resultIsDisplay){
		this.resultIsDisplay = resultIsDisplay;
		return this;
	}
	public boolean displayNumberFound(){
		return this.displayNumberFound;
	}
	public Setting setDisplayNumberFound(boolean displayNumberFound){
		this.displayNumberFound = displayNumberFound;
		return this;
	}
	public SearchType getSearchType(){
		return this.searchType;
	}
	public Setting setSearchType(SearchType searchType){
		this.searchType = searchType;
		return this;
	}
	public String getDefaultSearchColumn(){
		return this.defaultSearchColumn;
	}
	public Setting setDefaultSearchColumn(String defaultSearchColumn){
		this.defaultSearchColumn = defaultSearchColumn;
		return this;
	}
	public String getCategoryColumn(){
		return this.categoryColumn;
	}
	public Setting setCategoryColumn(String categoryColumn){
		this.categoryColumn = categoryColumn;
		return this;
	}
	public String getExpressionQueryExpression(){
		return this.expressionQueryExpression;
	}
	public Setting setExpressionQueryExpressions(String expressionQueryExpression){
		this.expressionQueryExpression = expressionQueryExpression;
		return this;
	}
	public String getExpressionQueryColumn(){
		return this.expressionQueryColumn;
	}
	public Setting setExpressionQueryColumn(String expressionQueryColumn){
		this.expressionQueryColumn = expressionQueryColumn;
		return this;
	}
}
