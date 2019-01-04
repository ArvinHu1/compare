package com.test;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class test {

	
	public static void main(String[] arg) throws Exception {
		/*
		FileInputStream in = new FileInputStream("config/config.properties");
		FileInputStream in2 = new FileInputStream("src/resources/log4j.properties");
		Properties prop = new Properties();
		Properties prop2 = new Properties();
		prop.load(in);
		prop2.load(in2);
		String logpath = prop.getProperty("logpath");
		in.close();
		in2.close();
		FileOutputStream out = new FileOutputStream("src/resources/log4j.properties");
		prop2.setProperty("log4j.appender.C.File", logpath);
		prop2.store(out, null);
		out.close();
		*/
		
//		Path path = Paths.get("config/config.properties");
//		List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
//		List<String> excludes = new ArrayList<String>();
//		for(String str : lines) {
//			if(str.contains("excludeFile")) {
//				excludes.add(str.trim().substring(str.indexOf("=") + 1));
//			}
//		}
//		
//		for(String str : excludes) {
//			System.out.println(str);
//		}
		
		try {
			Runtime.getRuntime().exec("cmd /c start  C:\\Users\\a805370\\Desktop\\aaa.bat");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
