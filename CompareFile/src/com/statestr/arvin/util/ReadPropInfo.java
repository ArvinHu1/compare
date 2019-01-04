package com.statestr.arvin.util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

public class ReadPropInfo {

	private static Logger logger = Logger.getLogger(ReadPropInfo.class);
	
	public List<Map<String, String>> getPropInfo() {
		
//		Path path = Paths.get("config/config.properties");
//		List<String> lines;
//		StringBuffer excludestr = new StringBuffer();
//		String first = "";
//		String second = "";
//		Map<String, String> map = new HashMap<String, String>();
//		try {
//			lines = Files.readAllLines(path, StandardCharsets.UTF_8);
//			for(String str : lines) {
//				if(str.contains("excludeFile")) {
//					excludestr.append(str.trim().substring(str.indexOf("=") + 1)).append(",");
//				}
//				if(str.contains("firstFolder")) {
//					first = str.trim().substring(str.indexOf("=")+1);
//				}
//				if(str.contains("secondFolder")) {
//					second = str.trim().substring(str.indexOf("=")+1);
//				}
//			}
//			String excludes = excludestr.toString().substring(0, excludestr.length()-1);
//			
//			map.put("firstFolder", first);
//			map.put("secondFolder", second);
//			map.put("excludeFiles", excludes);
//		} catch (IOException e) {
//			e.printStackTrace();
//			logger.error("read config.properties failed");
//		}
//		return map;
		
		List list = Dom4JUtil.readXml();
		List compareList = (List) list.get(0);
		return compareList;
		
	}

	public static void main(String[] args) {
		ReadPropInfo rx = new ReadPropInfo();
		rx.getPropInfo();
	}
}
