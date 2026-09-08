package com.kooritea.fcmfix;

import com.kooritea.fcmfix.libxposed.XposedBridge;
import com.kooritea.fcmfix.xposed.AutoStartFix;
import com.kooritea.fcmfix.xposed.BroadcastFix;
import com.kooritea.fcmfix.xposed.KeepNotification;
import com.kooritea.fcmfix.xposed.OplusProxyFix;
import com.kooritea.fcmfix.xposed.ReconnectManagerFix;
import com.kooritea.fcmfix.xposed.XposedModule;

import io.github.libxposed.api.XposedModuleInterface;

public class XposedMain extends io.github.libxposed.api.XposedModule {

    @Override
    public void onSystemServerStarting(SystemServerStartingParam param) {
        XposedBridge.init(this);
        XposedModule.setSelfPackageName("android");

        ClassLoader classLoader = param.getClassLoader();
        XposedBridge.log("[fcmfix] start hook com.android.server.am.ActivityManagerService/com.android.server.am.BroadcastController");
        new BroadcastFix(classLoader);

        XposedBridge.log("[fcmfix] start hook com.android.server.am.OplusAppStartupManager.shouldPreventSendReceiverReal");
        new AutoStartFix(classLoader);

        XposedBridge.log("[fcmfix] com.android.server.notification.NotificationManagerService");
        new KeepNotification(classLoader);

        XposedBridge.log("[fcmfix] start hook com.android.server.power.OplusProxyWakeLock");
        new OplusProxyFix(classLoader);
    }

    @Override
    public void onPackageReady(XposedModuleInterface.PackageReadyParam param) {
        XposedBridge.init(this);

        if ("com.google.android.gms".equals(param.getPackageName()) && param.isFirstPackage()) {
            XposedModule.setSelfPackageName("com.google.android.gms");
            XposedBridge.log("[fcmfix] start hook com.google.android.gms");
            new ReconnectManagerFix(param.getClassLoader());
        }
    }
}
