package com.kooritea.fcmfix.xposed;

import android.content.Intent;

import com.kooritea.fcmfix.util.XposedUtils;

import java.lang.reflect.Method;

import com.kooritea.fcmfix.libxposed.XC_MethodHook;
import com.kooritea.fcmfix.libxposed.XposedBridge;
import com.kooritea.fcmfix.libxposed.XposedHelpers;

public class AutoStartFix extends XposedModule {
    private final String FCM_RECEIVE = ".android.c2dm.intent.RECEIVE";

    public AutoStartFix(ClassLoader classLoader){
        super(classLoader);
        try{
            this.startHook();
        }catch (Throwable e) {
            printLog("hook error AutoStartFix:" + e.getMessage());
        }
    }

    protected void startHook(){
        try{
            // oos15/cos15
            Method method = XposedUtils.findMethod(XposedHelpers.findClass("com.android.server.am.OplusAppStartupManager",classLoader),"shouldPreventSendReceiverReal",4);
            XposedBridge.hookMethod(method,new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam methodHookParam) {
                    if(methodHookParam.args[0] != null && XposedHelpers.getObjectField(methodHookParam.args[0],"intent") != null){
                        Intent intent = (Intent)XposedHelpers.getObjectField(methodHookParam.args[0],"intent");
                        if(isFCMIntent(intent) && targetIsAllow(intent.getPackage())){
                            methodHookParam.setResult(false);
                        }
                    }
                }
            });
        } catch (XposedHelpers.ClassNotFoundError | NoSuchMethodError  e) {
            printLog("No Such Method com.android.server.am.OplusAppStartupManager.shouldPreventSendReceiverReal");
        }
    }
}
