# fcmfix(Android 10-15 · 欧加系机型)

[![Android CI](https://github.com/kooritea/fcmfix/workflows/Android%20CI/badge.svg)](https://github.com/kooritea/fcmfix/actions)

让fcm/gcm唤醒未启动的应用进行发送通知  

测试环境有限，本分支只保留 OPPO/OnePlus/Realme（ColorOS/OxygenOS）相关的适配，已移除 MIUI/HyperOS 专用的 Hook。

### 附加功能

- 阻止Android系统在应用停止时自动移除通知栏的通知
- 在OxygenOS15/ColorOS15上动态解除来自fcm的自启动限制、解除进程冻结
- 没有预期唤醒目标应用时发送提示通知

### 关于fcm

fcm是在Android中由google维护的一条介于google服务器与gms应用之间用于推送通知的长链接。  
一般的工作流程为应用服务器将消息发送到google服务器，google服务器将消息推送给gms应用，gms应用通过广播传递给应用，应用通过接收到的fcm消息决定是否发送通知和通知内容。  
其中gms通过fcm广播通知应用时，如果应用处于非运行状态，就会出现`Failed to broadcast to stopped app`，fcmfix主要就是解决这个问题。

### 已知问题

- 非OxygenOS15/ColorOS15系统可能需要给予目标应用类似允许自启动的权限，以及电池选项设置为不优化。
