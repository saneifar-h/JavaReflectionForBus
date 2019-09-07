package com.company;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.org.apache.xpath.internal.objects.XObject;
import org.reflections.Reflections;
import spark.QueryParamsMap;

import java.lang.reflect.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static spark.Spark.*;

public class Main {

    public static void main(String[] args) {
        Reflections reflections = new Reflections("com.company");
        Set<Class<? extends ITwoWayCommandHandler>> allTwoWayHanlders = reflections.getSubTypesOf(ITwoWayCommandHandler.class);
        allTwoWayHanlders.forEach((n) -> {
            Type[] twowayinterfaces = n.getGenericInterfaces();
            try {
                Object instance = n.getConstructors()[0].newInstance();
                for (Type inf : twowayinterfaces) {
                    try {
                        Type cls = ((ParameterizedType) inf).getActualTypeArguments()[0];
                        Method handleMethod = instance.getClass().getMethod("Handle", (Class<?>) cls);
                        get(((Class<?>) cls).getSimpleName(), (req, res) -> {
                            Class cmdCls = (Class<?>) cls;
                            Object object = cmdCls.getConstructors()[0].newInstance();
                            Set<String> queryParams = req.queryParams();
                            Field[] fields = cmdCls.getDeclaredFields();
                            for (Field field : fields) {
                                if (queryParams.contains(field.getName()))
                                    field.set(object, req.queryParams(field.getName()));
                            }
                            if (cls.getClass().equals(String.class))
                                return handleMethod.invoke(instance, object);
                            else
                                return new ObjectMapper().writeValueAsString(handleMethod.invoke(instance, object));
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
