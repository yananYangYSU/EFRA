package dataTransform;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FileOperation {
	private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss", Locale.ENGLISH);

	/**
	 * 单纯读取log原始文件
	 * @param filePath
	 * @return 返回list数组
	 * @throws IOException
	 */
	public List<Long> readLogFile(String filePath) throws IOException {  
		List<Long> timeList=new ArrayList<Long>();

		File file = new File(filePath); 
		BufferedReader reader = null;
		try {
			FileInputStream fileInputStream = new FileInputStream(file);
			InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
			reader = new BufferedReader(inputStreamReader);
			String line = ""; 
			/*
			 * 跳过前1行
			 */ 
			int start=0;
			while ((line = reader.readLine()) != null) { 
				start=line.indexOf("[")+1;
				
				try{
					timeList.add(dateFormat.parse(line.substring(start,start+20)).getTime());
				}catch(Exception e){
					continue;
				}
			}

			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return timeList;
	}
	/**
	 * 读取double型的文件
	 * 文件的一列都是数字
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	public List<Double> readDoubleFile(String filePath) throws IOException {  
		List<Double> timeList=new ArrayList<Double>();

		File file = new File(filePath); 
		BufferedReader reader = null;
		try {
			FileInputStream fileInputStream = new FileInputStream(file);
			InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
			reader = new BufferedReader(inputStreamReader);
			String line = ""; 
		 
			while ((line = reader.readLine()) != null) { 
				timeList.add(Double.parseDouble(line));
			}

			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return timeList;
	}
	/**
	 * 读取int型的文件
	 * 文件的一列都是数字
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	public List<Integer> readIntFile(String filePath) throws IOException {  
		List<Integer> timeList=new ArrayList<Integer>();

		File file = new File(filePath); 
		BufferedReader reader = null;
		try {
			FileInputStream fileInputStream = new FileInputStream(file);
			InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
			reader = new BufferedReader(inputStreamReader);
			String line = ""; 
		 
			while ((line = reader.readLine()) != null) { 
				timeList.add(Integer.parseInt(line));
			}

			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return timeList;
	}
	/**
	 * 写入文件
	 * @param resList 要写入文件的数组
	 * @param filePath 文件路径+文件名称
	 * @param type row:按行写   col:按列
	 */
	public <T> void writeResFile(List<T> resList,String filePath,String type){
		try{
			FileWriter writer = new FileWriter(filePath);
			int size=resList.size();
			if(type!=null&&type.equals("row")){
				for(int i=0;i<size;i++){
					writer.write(resList.get(i)+"\n");
					if(i%1000==0)
						writer.flush();
				}
			}else{
				for(int i=0;i<size;i++){
					writer.write(resList.get(i).toString());
					if(i%1000==0)
						writer.flush();
				}
			}
			
			writer.flush();
			writer.close();
			System.out.println("写入完毕");
		} catch (IOException e1) {
			e1.printStackTrace();
		} 
	}
	
}

