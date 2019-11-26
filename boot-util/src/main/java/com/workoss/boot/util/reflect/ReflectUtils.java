package com.workoss.boot.util.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import com.workoss.boot.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.objenesis.ObjenesisStd;
import org.springframework.objenesis.instantiator.ObjectInstantiator;


/**
 * @Description: 反射工具类
 * @Author: luanfeng
 * @Date: 2017/8/11 8:10
 * @Version: 1.0.0
 */
public class ReflectUtils {
    private static final Logger log = LoggerFactory.getLogger(ReflectUtils.class);
    private static final String SETTER_PREFIX = "set";
    private static final String GETTER_PREFIX = "get";
    private static final String CACHE_PREFIX = "reflect_";

    private static final ObjenesisStd objenesisStd = new ObjenesisStd(true);

    private static Map<String, AbstractMethodAccess> methodAccessCache = new ConcurrentHashMap<>();

    private static Map<String, Map<String, Object>> classMethodCache = new ConcurrentHashMap<>();


    public  static <T> T newInstance(Class<T> clazz){
        ObjectInstantiator objectInstantiator = objenesisStd.getInstantiatorOf(clazz);
        return (T)objectInstantiator.newInstance();
    }

    public static Object invokeMethod(Object obj, String methodName, Object param) {
        if (!hasMethod(obj.getClass(), methodName)) {
            log.warn("{} 没有找到方法 {}", obj.getClass(), methodName);
            return null;
        }
        AbstractMethodAccess methodAccess = getMethodAccessCache(obj.getClass());
        if (param == null) {
            return methodAccess.invoke(obj, methodAccess.getIndex(methodName));
        }
        return methodAccess.invoke(obj, methodAccess.getIndex(methodName), param);
    }


    public static Object getPropertyByInvokeMethod(Object obj, String property) {
        String getterMethodName = GETTER_PREFIX + StringUtils.capitalize(property);
        return invokeMethod(obj, getterMethodName, null);
    }

    public static void setPropertyByInvokeMethod(Object obj, String property, Object value) {
        String setterMethodName = SETTER_PREFIX + StringUtils.capitalize(property);
        invokeMethod(obj, setterMethodName, value);
    }

    public static boolean hasMethod(Class clazz, String methodName) {
        Map<String, Object> map = classMethodCache.get(clazz.getName());
        if (map == null) {
            AbstractMethodAccess methodAccess = getMethodAccessCache(clazz);
            String[] methodNames = methodAccess.getMethodNames();
            if (methodNames != null && methodNames.length > 0) {
              map = Arrays.stream(methodNames).distinct().filter(name -> !isClassDefaultMethod(name)).collect(Collectors
					  .toMap(name->name,name->true));
            }
            classMethodCache.put(clazz.getName(), map);
        }
        return map.containsKey(methodName);
    }

    public static boolean isClassDefaultMethod(String methodName) {
        return "equals".equalsIgnoreCase(methodName) || "toString".equalsIgnoreCase(methodName)
                || "hashCode".equalsIgnoreCase(methodName) || "canEqual".equalsIgnoreCase(methodName);
    }

    /**
     * 直接读取对象属性值, 无视private/protected修饰符, 不经过getter函数.
     */
    public static Object getFieldValue(final Object obj, final String fieldName) {
        Field field = getAccessibleField(obj, fieldName);

        if (field == null) {
            throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + obj + "]");
        }

        Object result = null;
        try {
            result = field.get(obj);
        } catch (IllegalAccessException e) {
            log.error("不可能抛出的异常{}", e.getMessage());
        }
        return result;
    }

    /**
     * 直接设置对象属性值, 无视private/protected修饰符, 不经过setter函数.
     */
    public static void setFieldValue(final Object obj, final String fieldName, final Object value) {
        Field field = getAccessibleField(obj, fieldName);

        if (field == null) {
            throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + obj + "]");
        }

        try {
            field.set(obj, value);
        } catch (IllegalAccessException e) {
            log.error("不可能抛出的异常:{}", e.getMessage());
        }
    }


    public static AbstractMethodAccess getMethodAccessCache(Class obj) {
        AbstractMethodAccess methodAccess = methodAccessCache.get(CACHE_PREFIX + obj.getName());
        if (methodAccess == null) {
            methodAccess = AbstractMethodAccess.get(obj);
            methodAccessCache.put(CACHE_PREFIX + obj.getName(), methodAccess);
            return methodAccess;
        }
        return methodAccess;
    }

    /**
     * 循环向上转型, 获取对象的DeclaredField, 并强制设置为可访问.
     * <p>
     * 如向上转型到Object仍无法找到, 返回null.
     */
    public static Field getAccessibleField(final Object obj, final String fieldName) {
        if (obj == null || fieldName == null) {
            throw new RuntimeException("object fieldName not null");
        }
        for (Class<?> superClass = obj.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
            try {
                Field field = superClass.getDeclaredField(fieldName);
                makeAccessible(field);
                return field;
            } catch (NoSuchFieldException e) {// NOSONAR
                // Field不在当前类定义,继续向上转型
            }
        }
        return null;
    }

    public static void makeAccessible(Field field) {
        boolean notPublic = !Modifier.isPublic(field.getModifiers()) || !Modifier
				.isPublic(field.getDeclaringClass().getModifiers()) || Modifier
                .isFinal(field.getModifiers());
        if (notPublic && !field.isAccessible()) {
            field.setAccessible(true);
        }
    }

}