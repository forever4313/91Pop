package com.dante.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.text.format.Formatter;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.model.FileDownloadStatus;
import com.dante.MyApplication;
import com.dante.R;
import com.dante.data.DataManager;
import com.dante.data.model.UnLimit91PornItem;
import com.dante.di.component.DaggerServiceComponent;
import com.dante.ui.download.DownloadActivity;
import com.dante.utils.DownloadManager;
import com.dante.utils.constants.Constants;

import java.util.List;

import javax.inject.Inject;

/**
 * @author flymegoc
 */
public class DownloadVideoService extends Service implements DownloadManager.DownloadStatusUpdater {

    @Inject
    protected DataManager dataManager;
    private int notificationChannelId = Constants.VIDEO_DOWNLOAD_NOTIFICATION_ID;
    private String notificationChannelName = "channelName";

    public DownloadVideoService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //创建NotificationChannel
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            NotificationChannel channel = new NotificationChannel(notificationChannelId+"", notificationChannelName, NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }
        DaggerServiceComponent.builder().applicationComponent(((MyApplication) getApplication()).getApplicationComponent()).build().inject(this);
        DownloadManager.getImpl().addUpdater(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return START_NOT_STICKY;
    }

    private void startNotification(String videoName, int progress, String fileSize, int speed) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, String.valueOf(notificationChannelId));
        builder.setContentTitle("正在下载");
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setProgress(100, progress, false);
        builder.setContentText(fileSize + "--" + speed + "KB/s");
        builder.setContentInfo(videoName);
        Intent intent = new Intent(this, DownloadActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        Notification notification = builder.build();

        startForeground(notificationChannelId, notification);
    }

    @Override
    public void onDestroy() {
        DownloadManager.getImpl().removeUpdater(this);
        stopForeground(true);
        super.onDestroy();
    }

    @Override
    public void complete(BaseDownloadTask task) {
        updateNotification(task, task.getSmallFileSoFarBytes(), task.getSmallFileTotalBytes());
    }

    @Override
    public void update(BaseDownloadTask task) {
        updateNotification(task, task.getSmallFileSoFarBytes(), task.getSmallFileTotalBytes());
    }

    private void updateNotification(BaseDownloadTask task, int soFarBytes, int totalBytes) {
        int progress = (int) (((float) soFarBytes / totalBytes) * 100);
        String fileSize = Formatter.formatFileSize(DownloadVideoService.this, soFarBytes).replace("MB", "") + "/ " + Formatter.formatFileSize(DownloadVideoService.this, totalBytes);
        UnLimit91PornItem unLimit91PornItem = dataManager.findUnLimit91PornItemByDownloadId(task.getId());
        if (unLimit91PornItem != null) {
            if (task.getStatus() == FileDownloadStatus.completed) {
                List<UnLimit91PornItem> unLimit91PornItemList = dataManager.findUnLimit91PornItemsByDownloadStatus(FileDownloadStatus.progress);
                if (unLimit91PornItemList.size() == 0) {
                    stopForeground(true);
                }
            } else {
                startNotification(unLimit91PornItem.getTitle(), progress, fileSize, task.getSpeed());
            }
        } else {
            List<UnLimit91PornItem> unLimit91PornItemList = dataManager.loadDownloadingData();
            if (unLimit91PornItemList.size() == 0) {
                stopForeground(true);
            }
        }
    }
}
