package com.hxh.simpleintentutil;

import android.app.Activity;
import android.content.Intent;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BindUtil {
    public static void start(Activity activity, Class activityClass, Serializable gs)
    {
        Intent intent = new Intent(activity, activityClass);
        intent.putExtra("key",gs);
        activity.startActivity(intent);
    }

    public static void bind(Activity activity) {
        String className = activity.getClass().getName() + "_BindIntent";
        try {
            Class<?> bindClass = Class.forName(className);
            Constructor<?> constructorMethod = bindClass.getDeclaredConstructor(activity.getClass());
            Object instance = constructorMethod.newInstance(activity);
            Method bindMethod = bindClass.getMethod("bind");
            bindMethod.invoke(instance);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
