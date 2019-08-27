package com.company;


import com.sun.org.apache.xpath.internal.objects.XObject;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

import static spark.Spark.*;

public class Main {

    public static void main(String[] args) {
        Reflections reflections = new Reflections("com.company");
        Set<Class<? extends ITwoWayCommandHandler>> allTwoWayHanlders = reflections.getSubTypesOf(ITwoWayCommandHandler.class);
        Set<Class<? extends ITwoWayCommand>> allTwowayCommands = reflections.getSubTypesOf(ITwoWayCommand.class);
        // allClasses.forEach((n)->System.out.println(((ParameterizedType)n.getGenericInterfaces()[0]).getActualTypeArguments()[0]));
        allTwoWayHanlders.forEach((n) -> {
            Type[] twowayinterfaces = n.getGenericInterfaces();
            try {
                Object instance = n.getConstructors()[0].newInstance();
                for (Type inf : twowayinterfaces) {
                    try {
                        Type cls = ((ParameterizedType) inf).getActualTypeArguments()[0];
                        Method handleMethod = instance.getClass().getMethod("Handle", (Class<?>) cls);
                        get(((Class<?>) cls).getSimpleName(), (req, res) -> {
                            Class cmdCls=(Class<?>) cls;
                            Object object=cmdCls.getConstructors()[0].newInstance();
                            cmdCls.getField("FirstName").set(object,"Hossein");
                            cmdCls.getField("LastName").set(object,"Saneifar");
                            return handleMethod.invoke(instance,object);
                        });
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                }
            } catch (InstantiationException x) {
                x.printStackTrace();
            } catch (InvocationTargetException x) {
                x.printStackTrace();
            } catch (IllegalAccessException x) {
                x.printStackTrace();
            }
        });
    }
}
