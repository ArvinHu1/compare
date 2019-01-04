package com.statestr.arvin.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.statestr.arvin.util.GitUtil;
import com.statestr.arvin.util.MD5Util;
import com.statestr.arvin.util.MailUtils;

public class CompareIO {

	private static Logger logger = Logger.getLogger(CompareIO.class);

	private static List<String> fileList = new ArrayList<String>();
	private static List<String> differentFiles = null;

	private static List<String> onlyfirst = null;
	private static List<String> onlysecond = null;

	private static List<String> excludeFiles = null;
	
	public static <T> void printFiles(Map folderMap, Map mailMap, Map gitMap) {
		String username = (String) gitMap.get("username");
		String pass = (String) gitMap.get("password");
		String loaclCodeDir	= (String) gitMap.get("localCodeDir");
		logger.info(Thread.currentThread().getName()+"--git pull");
		boolean flag = GitUtil.pullCodeToLocal(loaclCodeDir, username, pass);
		if(flag) {
			String first = (String) folderMap.get("gitFolder");
			String second = (String) folderMap.get("shareFolder");
			List<T> exfls = (List<T>) folderMap.get("excludeFiles");
			
			excludeFiles = new ArrayList<String>();
			excludeFiles = (List<String>) exfls;
			Map<String, List<String>> map = new HashMap<String, List<String>>();
			logger.info(Thread.currentThread().getName()+"compare begin...");
			map = compareFolder(first, second, excludeFiles);
			
			if (map != null) {
				FileWriter fw;
				try {
					fw = new FileWriter("result\\result.txt");
					//get absolute path
					File file = new File("result\\result.txt");
//System.out.println(fw+"---"+fw.toString()+"==="+file.getAbsolutePath());
					System.out.println("result output folder relative path : " + "result/result.txt");
					BufferedWriter buffer = new BufferedWriter(fw);
					
					buffer.write("git: " + first);
					buffer.newLine();
					buffer.write("share: " + second);
					buffer.newLine();
					buffer.write("excludeFiles: " + exfls);
					buffer.newLine();
					
					buffer.write("*****DifferentFiles*****");
					buffer.newLine();
					for (String str : map.get("diff")) {
						buffer.write(str);
						buffer.newLine();
					}
					buffer.newLine();
					buffer.write("*****Files only in git folder*****");
					buffer.newLine();
					for (String str : map.get("onlyFirst")) {
						buffer.write(str);
						buffer.newLine();
					}
					buffer.newLine();
					buffer.write("*****Files only in share folder*****");
					buffer.newLine();
					for (String str : map.get("onlySecond")) {
						buffer.write(str);
						buffer.newLine();
					}
					buffer.newLine();
					buffer.flush();
					fw.flush();
					buffer.close();
					fw.close();
					
					logger.info(Thread.currentThread().getName()+"--send mail to user");
					String sender = (String) mailMap.get("sender");
					List receivers = (List) mailMap.get("receivers");
					String subject = (String) mailMap.get("subject");
					String password = (String) mailMap.get("password");
					MailUtils.sendWithAttachment(sender, receivers, subject, password, file);
				} catch (IOException e) {
					e.printStackTrace();
					logger.error(Thread.currentThread().getName()+"IO Exception!");
				} catch (Exception e) {
					e.printStackTrace();
					logger.error(Thread.currentThread().getName()+"SendMail Exception!");
				}
				
			}
			
		} else {
			logger.error(Thread.currentThread().getName()+"git pull failed");
		}

	}

	public static void main(String[] args) throws IOException {


	}

	public static Map<String, List<String>> compareFolder(String firstFolder, String secondFolder,
			List<String> excludeFiles) {
		
//		System.out.println("***************************");
		List<String> singleFiles = new ArrayList<String>();
		List<String> somelikeFiles = new ArrayList<String>();
		List<String> suffixFiles = new ArrayList<String>();
		for(String str : excludeFiles) {
			if(str.contains("\\")) {
				singleFiles.add(str);
			} else if(str.charAt(0) == '.') {
				suffixFiles.add(str);
			} else {
				somelikeFiles.add(str);
			}
		}
//		System.out.println("singleFiles:"+singleFiles);
//		System.out.println("somelikeFiles:"+somelikeFiles);
//		System.out.println("suffixFiles:"+suffixFiles);
//		System.out.println("***************************");
		
		
		differentFiles = new ArrayList<String>();
		// System.out.println(excludeFiles);
		List<String> firstlist = new ArrayList<String>();
		List<String> secondlist = new ArrayList<String>();

		onlyfirst = new ArrayList<String>();
		onlysecond = new ArrayList<String>();
		boolean flag = false;

		fileList = new ArrayList<String>();
		logger.info(Thread.currentThread().getName()+"get files from git folder");
		firstlist = getFiles(firstFolder);
		fileList = new ArrayList<String>();
		
		String reg = "";
		
		if (firstlist != null && firstlist.size() > 0) {
			logger.info(Thread.currentThread().getName()+"get files from share folder");
			secondlist = getFiles(secondFolder);
			
			logger.info(Thread.currentThread().getName()+"comparing...");
			if (secondlist != null & secondlist.size() > 0) {
				try {
					for (String str1 : firstlist) {
//						System.out.println("somelike:"+str1.substring(str1.lastIndexOf("\\")+1));
//						System.out.println("suffix:"+str1.substring(str1.lastIndexOf(".")));
						if(somelikeFiles != null && somelikeFiles.contains(str1.substring(str1.lastIndexOf("\\")+1))) {
							continue;
						}
						if(suffixFiles != null && suffixFiles.contains(str1.substring(str1.lastIndexOf(".")))) {
							continue;
						}
						for (String str2 : secondlist) {
							if (singleFiles != null && (singleFiles.contains(str1) || singleFiles.contains(str2))) {
								
							} else if (getFileRelativePath(str1, firstFolder)
									.equals(getFileRelativePath(str2, secondFolder))) {
//								System.out.println(getFileRelativePath(str1, firstFolder)+"----------"+getFileRelativePath(str2, secondFolder));
								flag = compareFile(str1, str2);
								if (!flag) {
									differentFiles.add(str1);
								}
							}

						}
					}
					// files only in firstFolder
					// //待讨论--如果excludefiles中有不一定是两个folder中共同存在的文件,则要加判断条件(一般指定了,就是都存在)
					for (String str1 : firstlist) {
						if (singleFiles != null && singleFiles.contains(str1)) {
							continue;
						}
						if(somelikeFiles != null && somelikeFiles.contains(str1.substring(str1.lastIndexOf("\\")+1))) {
							continue;
						}
						if(suffixFiles != null && suffixFiles.contains(str1.substring(str1.lastIndexOf(".")))) {
							continue;
						}
						if (!getFileRelativePath(secondlist, secondFolder)
								.contains(getFileRelativePath(str1, firstFolder))) {
							onlyfirst.add(str1);
						}
					}
					// files only in secondFolder
					for (String str2 : secondlist) {
						if (singleFiles != null && singleFiles.contains(str2)) {
							continue;
						}
						if(somelikeFiles != null && somelikeFiles.contains(str2.substring(str2.lastIndexOf("\\")+1))) {
							continue;
						}
						if(suffixFiles != null && suffixFiles.contains(str2.substring(str2.lastIndexOf(".")))) {
							continue;
						}
						if (!getFileRelativePath(firstlist, firstFolder)
								.contains(getFileRelativePath(str2, secondFolder))) {
							onlysecond.add(str2);
						}
					}

					Map<String, List<String>> map = new HashMap<String, List<String>>();
					map.put("diff", differentFiles);
					map.put("onlyFirst", onlyfirst);
					map.put("onlySecond", onlysecond);
					logger.info(Thread.currentThread().getName()+"compare finished!");
					return map;

				} catch (IOException e) {
					logger.error(Thread.currentThread().getName()+"IO Exception!");
				}
			}
		}
		return null;
	}

	public static String getFileRelativePath(String filepath, String folder) {
		return filepath.substring(filepath.indexOf(folder) + folder.length() + 1);
	}

	public static List<String> getFileRelativePath(List<String> files, String folder) {
		List<String> list = new ArrayList<String>();
		for (String str : files) {
			String st = str.substring(str.indexOf(folder) + folder.length() + 1);
			// System.out.println(str+"--"+folder+"##########"+st);
			list.add(st);
		}
		return list;
	}

	public static List<String> getFiles(String folderPath) {
		File root = new File(folderPath);
		File[] files;
		if (root.isDirectory()) {
			files = root.listFiles();
			for (File file : files) {
				if (file.isFile()) {
					fileList.add(file.getAbsolutePath());
				} else {
					getFiles(file.getAbsolutePath());
				}
			}
		} else {
			System.out.println("folder not exist!");
			logger.error("folder not exist!");
		}
		return fileList;
	}

	public static boolean compareFile(String file1, String file2) throws IOException {
		boolean flag = false;
		// System.out.println(file1);
		// long start = System.currentTimeMillis();
		flag = MD5Util.getFileMD5String(new File(file1)).equals(MD5Util.getFileMD5String(new File(file2)));
		// long end = System.currentTimeMillis();
		// System.out.println("时间：:" + (end-start) + " " +
		// flag+"#######"+file1+"||"+file2);
		return flag;
	}

}
