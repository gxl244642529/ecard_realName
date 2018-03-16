package com.damai.note;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.citywithincity.interfaces.IViewContainer;
import com.citywithincity.utils.ReflectUtil;
import com.damai.helper.a.InitData;
import com.damai.helper.a.Model;
import com.damai.helper.a.Res;
import com.damai.helper.a.Singleton;

/**
 * api的通知器
 *
 * @author Randy
 *
 */

public class ClassParser {
    /**
     *
     */
    private static Map<Class<?>, ClsInfo[]> classMap = new HashMap<Class<?>, ClsInfo[]>();


    private static final ItemEventParser itemEventParser;
    private static final EventParser eventParser;
    private static final NotificationParser notificationParser;
    private static IMethodParser[] parsers;
    static {
        notificationParser = new NotificationParser();
        itemEventParser = new ItemEventParser();
        eventParser = new EventParser();
        parsers = new IMethodParser[] { notificationParser, itemEventParser,
                eventParser };
    }

    public static NotificationParser getNotificationParser() {
        return notificationParser;
    }

    public static void unregister(Object observer) {
        Class<?> clazz = observer.getClass();
        ClsInfo[] infos = classMap.get(clazz);
        if (infos != null) {
            for (ClsInfo notifyInfo : infos) {
                notifyInfo.clearObserver();
            }
        }
    }

    /**
     *
     * @param clazz
     * @param observer
     */
    private static void parseFields(IViewContainer observer,Class<?> clazz ,List<ClsInfo> list) {
        Class<?> endClass = null;
        if (observer instanceof FragmentActivity) {
            endClass = FragmentActivity.class;
        } else if (observer instanceof Fragment) {
            endClass = Fragment.class;
        }else if (observer instanceof Activity) {
            endClass = Activity.class;
        }else{
        	endClass = Object.class;
        }
        Field[] fields = ReflectUtil.getDeclareFields(clazz, endClass);
        for (Field field : fields) {
            if (field.isAnnotationPresent(Res.class)) {
                ResInfo info = new ResInfo(field);
                list.add(info);
            }else if(field.isAnnotationPresent(Model.class)){
                list.add(new ModelInfo(field));
            }else if(field.isAnnotationPresent(Singleton.class)){
                Singleton singleton = field.getAnnotation(Singleton.class);
                if(singleton.scope()==Singleton.ALL){
                    list.add(new SingletonInfoAll(field));
                }else{
                    list.add(new SingletonInfoDestroyWhenExit(field));
                }
            }else if(field.isAnnotationPresent(InitData.class)){
                list.add(new InitDataInfo(field));
            }

        }
    }


    private static void parseMethods(IViewContainer observer,Class<?> clazz,List<ClsInfo> list){
        Method[] methods = clazz.getMethods();
        for (IMethodParser parser : parsers) {
            parser.beginParse(observer);
        }

        for (Method method : methods) {
            for (IMethodParser parser : parsers) {
                parser.getMethodInfo(method, observer,list);
            }
        }
        for (IMethodParser parser : parsers) {
            parser.endParse(observer);
        }
    }

    public static void parse(IViewContainer observer) {
        Class<?> clazz = observer.getClass();
        ClsInfo[] infos = classMap.get(clazz);
        if (infos == null) {
            List<ClsInfo> list = new ArrayList<ClsInfo>();

            parseMethods(observer, clazz, list);
            parseFields(observer, clazz, list);

            infos = new ClsInfo[list.size()];
            if (list.size() > 0) {
                list.toArray(infos);
            }
            classMap.put(clazz, infos);

        }
    }

    /**
     * 注册观察者
     *
     * @param observer
     */
    public static void register(IViewContainer observer) {
        Class<?> clazz = observer.getClass();
        ClsInfo[] infos = classMap.get(clazz);
        for (ClsInfo methodInfo : infos) {
            methodInfo.setTarget(observer);
        }
    }

    public static ItemEventViewInfo[] getItemEventViewInfos(Class<?> clazz) {
        return itemEventParser.getItemEventViewInfos(clazz);
    }

}




