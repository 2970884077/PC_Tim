package com.ticktockx.sdk.pluginx;

import com.ticktockx.ex.PluginException;
import com.ticktockx.sdk.pluginx.event.Event;
import com.ticktockx.sdk.pluginx.event.EventHandler;
import com.ticktockx.sdk.pluginx.event.Listener;
import com.ticktockx.sdk.pluginx.event.MethodInvokeMapper;

import java.lang.reflect.Method;
import java.util.*;

public class EventRegisterHandler {


    static {
        handler = new EventRegisterHandler();
    }
    private static EventRegisterHandler handler;

    private Map<Object, List<MethodInvokeMapper>> listenerMapper = new HashMap<>();


    public void registerListener(Object obj){
        try {
            if (obj.getClass().getAnnotation(Listener.class) != null) {
                Method[] methods = obj.getClass().getDeclaredMethods();
                for (Method method : methods) {
                    registerEvent(method);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void registerEvent(final Method method) throws Exception{
        Class<?> type = method.getDeclaringClass();
        addMethod(method,(Listener)(type.newInstance()));
    }

    private void addMethod(Method method,Listener listenerInstance){
        EventHandler manager = method.getDeclaredAnnotation(EventHandler.class);
        if (manager != null) {
            MethodInvokeMapper mapper = new MethodInvokeMapper(manager.priority(), method);
            List<MethodInvokeMapper> methodMappers = listenerMapper.get(listenerInstance);
            if(methodMappers!=null){
                methodMappers.add(mapper);
                listenerMapper.put(listenerInstance,methodMappers);
            }else{
                methodMappers = new ArrayList<>();
                methodMappers.add(mapper);
                listenerMapper.put(listenerInstance,methodMappers);
            }
        }
    }
    public void callEvent(final Event event){
        try {
            Set<Map.Entry<Object, List<MethodInvokeMapper>>> set = listenerMapper.entrySet();
            for (Map.Entry<Object, List<MethodInvokeMapper>> entry : set) {
                Object listener = entry.getKey();
                List<MethodInvokeMapper> methods = entry.getValue();
                List<MethodInvokeMapper> invoker = new ArrayList<>();
                for (MethodInvokeMapper mapper : methods) {
                    Class[] classes = mapper.getMethod().getParameterTypes();
                    if (classes.length == 1) {
                        if (classes[0].getName().equals(event.getName())) {
                            invoker.add(mapper);
                        }
                    } else {
                        throw new PluginException("the event method must have one parameter");
                    }
                }
                invoker.sort(Comparator.comparing(MethodInvokeMapper::getPriority));
                for (MethodInvokeMapper method : invoker) {
                    method.getMethod().invoke(listener, event);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public static EventRegisterHandler getHandler() {
        return handler;
    }
}
