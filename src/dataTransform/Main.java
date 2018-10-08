package dataTransform;

import java.io.IOException; 
/**
 * 数据转换类
 * 将实际的web访问log数据解析提取，统计出请求数
 * @author yanan
 *
 */
public class Main {

	public static void main(String[] args) throws IOException {
	 
	 	new EventTran().transData();//将原始网站log日志 转换为requestPerDay数据
	}

}
