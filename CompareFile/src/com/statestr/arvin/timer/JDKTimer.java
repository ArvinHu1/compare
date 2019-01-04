package com.statestr.arvin.timer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.statestr.arvin.io.CompareIO;
import com.statestr.arvin.util.Dom4JUtil;

public class JDKTimer {

	private static Logger logger = Logger.getLogger(JDKTimer.class);

	private static Timer timer = null;
	private final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private final static ExecutorService executor = Executors.newSingleThreadExecutor();

	public static void main(String[] args) {

		// BasicConfigurator.configure();
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		PropertyConfigurator.configure(classLoader.getResource("resources/log4j.properties"));
		logger.info(Thread.currentThread().getName() + "task start...");

		logger.info(Thread.currentThread().getName() + "read schedule info from config.xml");

		JDKTimer jt = new JDKTimer();
		List list = Dom4JUtil.readXml();
		Map<String, String> timerMap = (Map<String, String>) list.get(list.size() - 1);

		Date time = null;
		try {
			time = sdf.parse(timerMap.get("starttime"));
		} catch (ParseException e1) {
			e1.printStackTrace();
			// logger.error("schedule argument starttime error, please check('yyyy-MM-dd
			// HH:mm:ss' for example)");
		}
		String period0 = timerMap.get("period");

		List compareList = (List) list.get(0);
		for (int i = 0; i < compareList.size(); i++) {
			Map map = (Map) compareList.get(i);
			myTask(time, period0, map);

		}

	}

	public static Future myTask(Date time, String period, Map map) {
		return executor.submit(new Runnable() {

			@Override
			public void run() {
				String gitFolder = (String) map.get("gitFolder");
				String shareFolder = (String) map.get("shareFolder");
				List list = (List) map.get("excludeFiles");
				Map folderMap = new HashMap();
				folderMap.put("gitFolder", gitFolder);
				folderMap.put("shareFolder", shareFolder);
				folderMap.put("excludeFiles", list);
				Map gitMap = (Map) map.get("gitProps");
				Map mailMap = (Map) map.get("mailProps");
				invokeSchedule(time, period, folderMap, mailMap, gitMap);
			}

		});

	}

	public static void invokeSchedule(Date time, String period0, Map folderMap, Map mailMap, Map gitMap) {

		int index = -1;
		int number = 0;
		String tag = "";
		long interval = -1;
		if (time != null) {

			if (period0 != null && period0.length() > 1) {
				for (int i = 1; i < period0.length(); i++) {
					if (i < period0.length()) {
						if (!Character.isDigit(period0.charAt(i))) {
							index = i;
							break;
						}
					}
				}
				if (index > 0) {
					number = Integer.parseInt(period0.substring(0, index));
					// System.out.println("period0:"+period0+"----index:"+index+"----number:" +
					// number);
					tag = period0.substring(index);
					if ("M".equals(tag) || "m".equals(tag)) {
						interval = number * 60 * 1000;
					} else if ("H".equals(tag) || "h".equals(tag)) {
						interval = number * 60 * 60 * 1000;
					}
					if (interval > 0) {
						System.out.println("schedule props: " + "starttime--" + time + "  period--" + period0);
						logger.info(Thread.currentThread().getName() + "schedule props: " + "starttime--" + time
								+ "  period--" + period0);
						System.out.println("task will begin at " + time);
						logger.info(Thread.currentThread().getName() + "This task will begin at " + time);
						schedule(time, interval, folderMap, mailMap, gitMap);

					} else {
						logger.error(Thread.currentThread().getName() + "schedule argument period error, please check");
					}
				} else {
					logger.error(Thread.currentThread().getName() + "schedule argument period error, please check");
				}

			} else {
				logger.error(Thread.currentThread().getName() + "schedule argument period error, please check");
			}

		} else {
			logger.error(Thread.currentThread().getName()
					+ "schedule argument starttime error, please check('yyyy-MM-dd HH:mm:ss' for example)");
		}

	}

	private static void schedule(Date time, long period, Map folderMap, Map mailMap, Map gitMap) {
		timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				CompareIO.printFiles(folderMap, mailMap, gitMap);
			}
		}, time, period);
	}

	public static Map<String, String> getTimerInfo() {
		List list = Dom4JUtil.readXml();
		Map<String, String> map = (Map<String, String>) list.get(list.size() - 1);
		return map;
	}
}
