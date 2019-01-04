package com.statestr.arvin.util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class Dom4JUtil {

	public static List readXml() {
		SAXReader reader = new SAXReader();
		List compareList = new ArrayList();
		Map map0 = null;
		Map map1 = null;
		Map map2 = null;
		Map map3 = new HashMap();
		List list0 = new ArrayList();
		List excls = null;
		List recs = null;
		try {
			Document doc = reader.read(new File("config/config.xml"));
			Element root = doc.getRootElement();
			Iterator it = root.elementIterator();
			while(it.hasNext()) {
				Element root1 = (Element) it.next();
//				System.out.println(root1.getName());
				if(root1.getName().equals("compare")) {
					map0 = new HashMap();
					Iterator it1 = root1.elementIterator();
					while(it1.hasNext()) {
						Element root2 = (Element) it1.next();
						if(root2.getName().equals("gitFolder")) {
							map0.put("gitFolder", root2.getStringValue());
						}
						if(root2.getName().equals("shareFolder")) {
							map0.put("shareFolder", root2.getStringValue());
						}
						if(root2.getName().equals("excludeFiles")) {
							Iterator excludes = root2.elementIterator();
							excls = new ArrayList();
							while(excludes.hasNext()) {
								Element exclude = (Element) excludes.next();
								excls.add(exclude.getStringValue());
							}
							map0.put("excludeFiles", excls);
						}
						if(root2.getName().equals("gitProps")) {
							Iterator git = root2.elementIterator();
							map1 = new HashMap();
							while(git.hasNext()) {
								Element gitProp = (Element) git.next();
								if(gitProp.getName().equals("localCodeDir")) {
									map1.put("localCodeDir", gitProp.getStringValue());
								}
								if(gitProp.getName().equals("remoteRepoGitDir")) {
									map1.put("remoteRepoGitDir", gitProp.getStringValue());
								}
								if(gitProp.getName().equals("username")) {
									map1.put("username", gitProp.getStringValue());
								}
								if(gitProp.getName().equals("password")) {
									map1.put("password", gitProp.getStringValue());
								}
							}
							map0.put("gitProps", map1);
						}
						if(root2.getName().equals("mailProps")) {
							Iterator mail = root2.elementIterator();
							map2 = new HashMap();
							while(mail.hasNext()) {
								Element m = (Element) mail.next();
								if(m.getName().equals("sender")) {
									map2.put("sender", m.getStringValue());
								}
								if(m.getName().equals("password")) {
									map2.put("password", m.getStringValue());
								}
								if(m.getName().equals("subject")) {
									map2.put("subject", m.getStringValue());
								}
								if(m.getName().equals("receivers")) {
									Iterator receis = m.elementIterator();
									recs = new ArrayList();
									while(receis.hasNext()) {
										Element rec = (Element) receis.next();
										recs.add(rec.getStringValue());
									}
									map2.put("receivers", recs);
								}
							}
							map0.put("mailProps", map2);
						}
					}
					compareList.add(map0);
					map0 = null;
				}
				
				if(root1.getName().equals("schedule")) {
					Iterator schedule = root1.elementIterator();
					while(schedule.hasNext()) {
						Element sche = (Element) schedule.next();
						if(sche.getName().equals("starttime")) {
							map3.put("starttime", sche.getStringValue());
						}
						if(sche.getName().equals("period")) {
							map3.put("period", sche.getStringValue());
						}
					}
				}
				
			}
			
			list0.add(compareList);
			list0.add(map3);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return list0;
	}
	
	public static void main(String[] args) {
//		readXml();
		List list = readXml();
		System.out.println(list);
	}
	
}
