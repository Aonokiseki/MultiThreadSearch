package utility;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public final class FileOperator {
	
	private static final int BUFFER_SIZE = 1048576;
	private static final String IS_APPEND_CONTENTS = "is.append.contents";
	private static final String FILE_SEPARATOR = "file.separator";
	private static final String CUT_PARENT_PATH = "cut.parent.path";
	/*
	 * 防止实例化
	 */
	private FileOperator(){}
	/**
	 * 以UTF-8的编码方式读文件
	 * 
	 * @param sourceFileAbsolutePath 源文件的绝对路径
	 * @return String 文件中的字符串
	 * @throws IOException 源文件不在指定路径下
	 */
	public static String read(String sourceFileAbsolutePath)throws IOException{
		return read(sourceFileAbsolutePath, "UTF-8", new HashMap<String,String>());
	}
	/**
	 * 以指定的编码方式读文件
	 * 
	 * @param sourceFileAbsolutePath 源文件的绝对路径
	 * @param encoding 以该参数保存的编码读文件
	 * @return String 文件中的字符串
	 * @throws IOException 源文件不在指定路径下
	 */
	public static String read(String sourceFileAbsolutePath, String encoding)throws IOException{
		return read(sourceFileAbsolutePath, encoding, new HashMap<String,String>());
	}
	/**
	 * 以指定的编码方式读文件
	 * 
	 * @param sourceFileAbsolutePath 源文件的绝对路径
	 * @param encoding 以该参数保存的编码读文件
	 * @param otherParameters 保留参数,待后续添加
	 * @return String 文件中的字符串
	 * @throws IOException 源文件不在指定路径下
	 */
	public static String read(String sourceFileAbsolutePath, String encoding, Map<String,String>otherParameters) throws IOException{
		FileInputStream fileInputStream = new FileInputStream(sourceFileAbsolutePath);
		InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, encoding);
		BufferedReader bufferedReader = new BufferedReader(inputStreamReader, BUFFER_SIZE);
		StringBuilder stringBuilder = new StringBuilder();
		String currentLineText ="";
		while((currentLineText = bufferedReader.readLine()) != null){
			stringBuilder.append(currentLineText + System.lineSeparator());
		}
		bufferedReader.close();
		return stringBuilder.toString();
	}
	/**
	 * 以指定编码, 按照列表方式读文件
	 * 
	 * @param sourceFileAbsolutePath 源文件绝对路径
	 * @param encoding 以该编码读取文件
	 * @param otherParameters 保留参数,待后续添加
	 * @return <code>List&ltString&gt</code>, 列表方式保存的文件
	 * @throws IOException 源文件不在指定路径下
	 */
	public static List<String> readAsList(String sourceFileAbsolutePath, String encoding, Map<String,String>otherParameters)throws IOException{
		FileInputStream fileInputStream = new FileInputStream(sourceFileAbsolutePath);
		InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, encoding);
		BufferedReader bufferedReader = new BufferedReader(inputStreamReader, BUFFER_SIZE);
		List<String> contentList = new ArrayList<String>();
		String currentLineText = "";
		while((currentLineText = bufferedReader.readLine()) != null){
			if(currentLineText.isEmpty() || "".equals(currentLineText.trim()) || currentLineText.length() == 0)
				continue;
			contentList.add(currentLineText);
		}
		bufferedReader.close();
		return contentList;
	}
	/**
	 * 以二进制方式读取文件
	 * 
	 * @param sourceFileAbsolutePath 源文件的绝对路径
	 * @return <code>List&ltByte&gt</code> 每个元素是此文件的一个字节
	 * @throws IOException
	 */
	public static List<Byte> toBinaryArray(String sourceFileAbsolutePath) throws IOException{
		File fPointer = new File(sourceFileAbsolutePath);
		if(!fPointer.exists())
			throw new NullPointerException("Program cannot find ["+fPointer.getName()+"].");
		if(fPointer.isDirectory())
			throw new IOException("["+fPointer.getName()+"] is not a File.");
		List<Byte> bytes = new ArrayList<Byte>();
		InputStream in = new FileInputStream(sourceFileAbsolutePath);
		int tempByte = Integer.MIN_VALUE;
		while((tempByte = in.read())!= -1)
			bytes.add((byte)tempByte);
		in.close();
		return bytes;
	}
	/**
	 * 以UTF-8编码方式写文件
	 * 
	 * @param targetSavingPath 文件的输出绝对路径
	 * @param text 输出文本
	 * @throws IOException 路径不存在
	 * 
	 */
	public static void write(String targetSavingPath, String text) throws IOException{
		write(targetSavingPath, text, "UTF-8", new HashMap<String,String>());
	}
	/**
	 * 以指定编码方式写文件
	 * 
	 * @param targetSavingPath 文件的输出绝对路径
	 * @param text 输出文本
	 * @param encoding 编码方式
	 * @throws IOException 路径不存在
	 * 
	 */
	public static void write(String targetSavingPath, String text, String encoding) throws IOException{
		write(targetSavingPath, text, encoding, new HashMap<String,String>());
	}
	/**
	 * 以指定编码方式写文件
	 * 
	 * @param targetSavingPath 文件的输出绝对路径
	 * @param text 输出文本
	 * @param encoding 编码方式
	 * @param otherParameters 其它参数, 目前:<br>
	 * <code>is.append.contents</code> 是否追加内容到末尾,true/false,默认false,不追加;空值视为false.<br>
	 * 注意追加文本需要自行调整文本格式.
	 * @throws IOException 路径不存在
	 * 
	 */
	public static void write(String targetSavingPath, String text, String encoding, Map<String,String>otherParameters) throws IOException{
		boolean isAppend = false;
		if(otherParameters != null && !otherParameters.isEmpty() && otherParameters.get(IS_APPEND_CONTENTS) != null){
			isAppend = Boolean.valueOf(otherParameters.get(IS_APPEND_CONTENTS).trim());
		}
		FileOutputStream fileOutputStream = new FileOutputStream(targetSavingPath, isAppend);
		OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, encoding);
		BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter, BUFFER_SIZE);
		bufferedWriter.write(text);
		bufferedWriter.flush();
		bufferedWriter.close();
	}
	/**
	 * 读取ini配置文件的指定项
	 * 
	 * @param inputFilePath 配置文件的绝对路径
	 * @param encoding 配置文件的编码方式
	 * @return Map key是item, value是property
	 * @throws IOException 配置文件路径不正确时 
	 */
	public static Map<String,String> readConfiguration(String inputFilePath, String encoding) throws IOException{
		Map<String,String> properties = new HashMap<String,String>();
		Properties property = new Properties();
		FileInputStream fileInputStream = new FileInputStream(inputFilePath);
		InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, encoding);
		property.load(inputStreamReader);
		for(Entry<Object,Object> entry:property.entrySet()){
			properties.put(entry.getKey().toString(), entry.getValue().toString());
		}
		inputStreamReader.close();
		fileInputStream.close();
		return properties;
	}
	
	private static final String IS_CONTAINS_DIRECTORY = "is.contains.directory";
	/**
	 * 遍历指定拓展名的文件
	 * 
	 * @param inputFilePath 搜索起点,可以是文件也可以是目录;如果是目录则会遍历该目录下的所有子文件
	 * @param suffix 目标拓展名,为空则输出所有文件
	 * @param options 选项<br>
	 *         <code>is.contains.directory</code> - 遍历时保存文件夹路径, 默认false, 即不保存
	 * @return <code>List&ltFile&gt</code> 每个目标文件指针组成的表
	 * @throws IOException 当搜索起点不存在时
	 */
	public static List<File> traversal(String inputFilePath, final String suffix, Map<String,String>options) throws IOException{
		File filePointer = new File(inputFilePath);
		if(!filePointer.exists())
			throw new IOException(inputFilePath + " is not exist! Please check your path.");
		boolean isContainsDirectory = false;
		if(MapOperator.mapHasNonNullValue(options, IS_CONTAINS_DIRECTORY))
			isContainsDirectory = Boolean.valueOf(options.get(IS_CONTAINS_DIRECTORY));
		List<File> files = new ArrayList<File>();
		List<File> fileQueue = new LinkedList<File>();
		fileQueue.add(filePointer);
		File currentFile = null;
		File[] childsOfFile = null;
		while(!fileQueue.isEmpty()){
			currentFile = fileQueue.remove(0);
			if(currentFile.isFile()){
				if(suffix == null || suffix.trim().equals("") || currentFile.getName().endsWith(suffix))
					files.add(currentFile);
			}else{
				childsOfFile = currentFile.listFiles(new FilenameFilter(){
					@Override
					public boolean accept(File filePointer, String fileName){
						if(suffix == null || suffix.trim().equals(""))
							return true;
						File current = new File(filePointer.getAbsolutePath()+System.getProperty(FILE_SEPARATOR)+fileName);
						if(current.isDirectory())
							return true;
						if(current.isFile() && current.getName().endsWith(suffix.trim()))
							return true;
						return false;
					}
				});
				if(isContainsDirectory)
					files.add(currentFile);
				if(childsOfFile == null || childsOfFile.length == 0)
					continue;
				for(File f:childsOfFile)
					fileQueue.add(f);
			}
		}
		return files;
	}
	/**
	 * 压缩文件(夹)
	 * @param targetDirectory 压缩操作的目标文件(夹)
	 * @param outputDirectory 输出目录,必须填写目录,压缩包的名称由被压缩的文件决定
	 * @param option 其它参数<br>
	 * <code>cut.parent.path</code> - 绝对路径裁剪时去掉当前文件(目录)的父级目录,仅当设置为true时生效。空值为默认值false。
	 * @return String 压缩包的绝对路径
	 * @throws IOException 当targetDirectory不存在或outputDirectory下已存在压缩包时
	 */
	public static String fileToZip(String targetDirectory, String outputDirectory, Map<String,String>option) throws IOException{
		String fileSeparator = System.getProperty(FILE_SEPARATOR);
		//======================================================//
		File filePointer = new File(targetDirectory);
		if(!filePointer.exists()){
			throw new IOException(targetDirectory+" is not exist! please check your path.");
		}
		File zipPointer = new File(outputDirectory+fileSeparator+filePointer.getName()+".zip");
		if(zipPointer.exists()){
			throw new IOException(zipPointer.getAbsolutePath()+" already existed!");
		}
		boolean cutParentPath = false;
		if(option != null && option.get(CUT_PARENT_PATH) != null && "true".equals(option.get(CUT_PARENT_PATH).trim())){
			cutParentPath = true;
		}
		//======================================================//
		FileOutputStream fileOutputStream = null;
		BufferedOutputStream bufferedOutputStream = null;
		ZipOutputStream zipOutputStream = null;
		FileInputStream fileInputStream = null;
		BufferedInputStream bufferedInputStream = null;
		ZipEntry zipEntry = null;
		byte[] buffer = new byte[BUFFER_SIZE];
		int read = 0;
		fileOutputStream = new FileOutputStream(zipPointer);
		bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
		zipOutputStream = new ZipOutputStream(bufferedOutputStream);
		//======================================================//
		/*
		 * inputDirectory这个变量记录着targetDirectory的父级目录,用于裁剪绝对路径
		 * 
		 * 这样一来可以使用相对路径做压缩。
		 * 
		 * relativePath是裁剪后的根路径, 可真正用于压缩中
		 */
		//======================================================//
		String inputDirectory = "";
		if(cutParentPath){
			inputDirectory = filePointer.getAbsolutePath() + fileSeparator;
		}else{
			inputDirectory = filePointer.getParent() + fileSeparator;
		}
		String relativePath = null;
		//======================================================//
		/*
		 * 广度优先遍历文件(夹)
		 * 
		 * 将targetDirectory放入队列中,每次取出第一个文件
		 * 
		 * 如果是文件, 使用裁剪后的路径直接做压缩操作
		 * 
		 * 如果是目录, 先在压缩包内创建一个目录,然后将该目录的所有子文件(夹)放入队列中
		 * 
		 * 重复以上操作直至队列为空,即压缩完成
		 */
		//======================================================//
		List<File> fileQueue = new LinkedList<File>();
		fileQueue.add(filePointer);
		File fPointer = null;
		while(!fileQueue.isEmpty()){
			fPointer = fileQueue.remove(0);
			if(fPointer.isDirectory()){
				fPointer.mkdirs();
				for(File f:fPointer.listFiles()){
					fileQueue.add(f);
				}
			}else{
				relativePath = fPointer.getAbsolutePath().replace(inputDirectory, "");
				zipEntry = new ZipEntry(relativePath);
				zipOutputStream.putNextEntry(zipEntry);
				fileInputStream = new FileInputStream(fPointer);
				bufferedInputStream = new BufferedInputStream(fileInputStream, BUFFER_SIZE);
				while((read=bufferedInputStream.read(buffer, 0, BUFFER_SIZE)) != -1){
					zipOutputStream.write(buffer,0,read);
		        }
			}
		}
		//======================================================//
		if(null != bufferedInputStream){
			bufferedInputStream.close();
		}
		if(null != zipOutputStream){
			zipOutputStream.close();
		}
		return zipPointer.getAbsolutePath();
	}
	/**
	 * 解压缩文件
	 * @param zipAbsolutePath 压缩包绝对路径
	 * @param targetDirectory 解压的目标目录
	 * @param option 其它参数，目前没有使用
	 * @throws IOException 压缩包的绝对路径不存在或错误，或者目标目录是个已存在的文件时
	 */
	public static void zipToFile(String zipAbsolutePath, String targetDirectory, Map<String,String> option) throws IOException{
		String fileSeparator = System.getProperty(FILE_SEPARATOR);
		//======================================================//
		File sourcePointer = new File(zipAbsolutePath);
		if(!sourcePointer.exists()){
			throw new IOException(zipAbsolutePath + " not exists, please check your parameter.");
		}if(!sourcePointer.isFile()){
			throw new IOException(zipAbsolutePath + " is not a file.");
		}if(!sourcePointer.getName().endsWith(".zip")){
			throw new IOException(zipAbsolutePath + " is not a zip.");
		}
		File targetPointer = new File(targetDirectory);
		if(!targetPointer.exists()){
			targetPointer.mkdirs();
		}else{
			if(!targetPointer.isDirectory()){
				throw new IOException(targetDirectory + " is not a directory");
			}else{}
		}
		//======================================================//
		ZipEntry zipEntry = null;
		String entryFilePath = "";
		File entryFilePointer = null;
		File entryFileParent = null;
		BufferedInputStream bufferedInputStream = null;
        BufferedOutputStream bufferedOutputStream = null;
        byte[] buffer = new byte[BUFFER_SIZE];
        int count = 0;
        //======================================================//
        /*
         * 遍历压缩包下的所有实体
         * 
         * 情况1:
         * 
         * 获得的实体是个目录 -> 拼接到目标目录后方,成为新目录 -> 1.不存在 -> 创建目录
         * 													   |
         * 													   -> 2.已存在 -> 什么都不做
         * 
         * 情况2:
         * 
         * 获得的实体是个文件 -> 获得这个文件的父级目录,然后拼接到目标目录后方,成为新目录 -> 1.不存在 -> 创建目录
         * 													                              |	
         * 													                              -> 2.已存在 -> 什么都不做
         * 
         * 然后, 解压这个实体
         */
        //======================================================//
		ZipFile zipFile = new ZipFile(sourcePointer);
		Enumeration<? extends ZipEntry> entries = zipFile.entries();
		while(entries.hasMoreElements()){
			zipEntry = entries.nextElement();
			entryFilePath = targetDirectory + fileSeparator + zipEntry.getName();
			entryFilePointer = new File(entryFilePath);
			if(zipEntry.isDirectory()){
				if(!entryFilePointer.exists()){
					entryFilePointer.mkdirs();
				}else{}
			}else{
				entryFileParent = entryFilePointer.getParentFile();
				if(!entryFileParent.exists()){
					entryFileParent.mkdirs();
				}
				bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(entryFilePointer));
				bufferedInputStream = new BufferedInputStream(zipFile.getInputStream(zipEntry));
				while((count = bufferedInputStream.read(buffer, 0, BUFFER_SIZE))!= -1){
					bufferedOutputStream.write(buffer, 0, count);
				}
				bufferedOutputStream.flush();
				bufferedOutputStream.close();
				bufferedInputStream.close();
			}
		}
		zipFile.close();
	}
	/**
	 * 复制文件
	 * @param from
	 * @param to
	 * @throws IOException
	 */
	private static void fileCopy(File from, File to) throws IOException{
		FileInputStream fileInputStream = new FileInputStream(from);
		FileOutputStream fileOutputStream = new FileOutputStream(to);
		FileChannel inputChannel = fileInputStream.getChannel();
		FileChannel outputChannel = fileOutputStream.getChannel();
		outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
		inputChannel.close();
		fileInputStream.close();
		outputChannel.close();
		fileOutputStream.close();
	}
	/**
	 * 复制文件(夹)
	 * @param source 源文件(夹), 不存在时抛出异常
	 * @param destination 目标文件夹,如果是已存在的单个文件则抛出异常
	 * @throws IOException
	 */
	public static void filesCopy(String source, String destination) throws IOException{
		File from = new File(source);
		if(!from.exists())
			throw new NullPointerException("File ["+source+"] is not exist.");
		File to = new File(destination);
		if(to.exists() && to.isFile())
			throw new IllegalArgumentException("File ["+destination+"] is a file.");
		String relativePath = null;
		File current = null;
		File newFilePointer = null;
		File[] childsOfCurrent = null;
		List<File> files = new LinkedList<File>();
		files.add(from);
		/*
		 * 注意裁剪相对路径, 如source = C:/Game/MineCraft/, 要注意裁减掉 C:/Game/
		 * 
		 * 这样MineCraft目录可以作为相对路径的"根",拼接到destination后边形成新的路径
		 */
		while(!files.isEmpty()){
			current = files.remove(0);
			/* getParentFile()就是为了保证裁减掉 C:/Game/, 只保留MineCraft目录 */
			relativePath = current.getAbsolutePath().replace(from.getParentFile().getAbsolutePath(), "");
			newFilePointer = new File(to.getAbsolutePath() + System.getProperty(FILE_SEPARATOR) + relativePath);
			if(current.isFile()){
				fileCopy(current, newFilePointer);
				continue;
			}
			newFilePointer.mkdirs();
			childsOfCurrent = current.listFiles();
			Arrays.sort(childsOfCurrent);
			for(File fPointer : childsOfCurrent)
				files.add(fPointer);
		}
	}
}
