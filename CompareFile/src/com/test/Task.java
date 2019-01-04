package com.test;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class Task {

	
	public static void main(String[] args) {
		Timer t = new Timer();
		
		t.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
				try {
					Thread.sleep(15000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
		}, 10000, 10000);
		
//		t.schedule(new TimerTask() {
//
//			@Override
//			public void run() {
//				System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
//				try {
//					Thread.sleep(15000);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//			}
//			
//		}, 10000, 10000);
		
	}
}
