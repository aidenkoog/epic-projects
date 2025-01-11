package io.github.aidenkoog.navigation

import android.app.NotificationManager
import android.service.notification.StatusBarNotification

@Suppress("SpellCheckingInspection")
object NotificationBarControlUtils {

    private const val NOTIFICATION_LIMIT = 20

    fun precondition(manager: NotificationManager) {
        val myAppNotifications = getMyAppNotifications(getActiveNotifications(manager))
        if (hasExceededNotification(myAppNotifications)) {
            removeOldestMyAppNotification(manager, getOldestNotification(myAppNotifications))
        }
    }

    private fun removeOldestMyAppNotification(
        manager: NotificationManager, notification: StatusBarNotification,
    ) = manager.cancel(notification.id)

    private fun hasExceededNotification(list: MutableList<StatusBarNotification>): Boolean =
        list.size > NOTIFICATION_LIMIT - 1

    private fun getActiveNotifications(manager: NotificationManager): Array<StatusBarNotification> =
        manager.activeNotifications

    private fun getOldestNotification(list: MutableList<StatusBarNotification>): StatusBarNotification {
        lateinit var oldestItem: StatusBarNotification
        for ((index, item) in list.withIndex()) {
            if (index == 0) {
                oldestItem = item
                continue
            }
            // 노티피케이션 포스팅 시간으로 비교하여 가장 과거의 노티피케이션 아이템 탐색
            if (oldestItem.postTime > item.postTime) {
                oldestItem = item
            }
        }
        return oldestItem
    }

    private fun getMyAppNotifications(notifications: Array<StatusBarNotification>): MutableList<StatusBarNotification> {
        val list = mutableListOf<StatusBarNotification>()
        for (notification in notifications) {
            if (notification.packageName != "") continue
            if (notification.tag != null) continue  // 노티피케이션 바 아이템 중 그룹 바인 경우는 제외
            list.add(notification)
        }
        return list
    }
}