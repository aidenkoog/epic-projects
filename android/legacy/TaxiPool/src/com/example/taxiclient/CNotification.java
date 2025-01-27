package com.example.taxiclient;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class CNotification {

	
	public static void addNotification(Context context, Intent intent, int noticeId, int iconId, String ticker, String title, String msg){
		
		
		Notification noti = new Notification(iconId, ticker, System.currentTimeMillis());
		noti.flags = noti.flags | noti.FLAG_AUTO_CANCEL;
		
		PendingIntent pintent = PendingIntent.getActivity(context, 0, intent, 0);
		
		noti.setLatestEventInfo(context, title, msg, pintent);
		
		NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		nm.cancel(noticeId);
		nm.notify(noticeId, noti);
		
	}
	
	public static void removeNotification(Context context, int noticeId){
		NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		nm.cancel(noticeId);
	}
	
}
