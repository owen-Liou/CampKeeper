package com.github.owenliou.campkeeper.base.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;

import java.beans.PropertyDescriptor;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class EntityUtils {

    public static <T> T getProperty(Object entity, String propertyName) {
        if (StringUtils.isNotBlank(propertyName)) {
            try {
                PropertyDescriptor propDesc = PropertyUtils.getPropertyDescriptor(entity, propertyName);
                if (propDesc != null && propDesc.getReadMethod() != null) {
                    return (T) PropertyUtils.getProperty(entity, propertyName);
                }
                return null;
            } catch (Exception e) {
                log.error("{}, {} getProperty({})", e.getLocalizedMessage(), entity.getClass().getName(), propertyName, e);
            }
        }
        return null;
    }

    public static void setProperty(Object entity, String propertyName, Object value) {
        if (StringUtils.isNotBlank(propertyName)) {
            try {
                PropertyDescriptor propDesc = PropertyUtils.getPropertyDescriptor(entity, propertyName);
                if (propDesc != null && propDesc.getWriteMethod() != null) {
                    PropertyUtils.setProperty(entity, propertyName, value);
                }
            } catch (Exception e) {
                log.error("{}, {} setProperty({})", e.getLocalizedMessage(), entity.getClass().getName(), propertyName, e);
            }
        }
    }

    /**
     * 取得有內容的 Property
     * @param entity
     * @param superClass
     * @param subClass
     */
    public static Map<String, Object> getEntityValuedProperties(Object entity, Class<?> superClass, Class<?> subClass) {
        Map<String, Object> result = new LinkedHashMap<>();
        Stream.of(subClass.getDeclaredFields())
                .filter(field -> PropertyUtils.isReadable(entity, field.getName()))
                .filter(field -> getProperty(entity, field.getName()) != null)
                .forEach(field -> {
                    Object fieldValue = getProperty(entity, field.getName());
                    if (fieldValue instanceof Collection<?>) {
                        if (!((Collection<?>) fieldValue).isEmpty()) {
                            result.put(field.getName(), fieldValue);
                        }
                    } else {
                        if (fieldValue instanceof String) {
                            if (StringUtils.isNotBlank(fieldValue.toString())) {
                                result.put(field.getName(), fieldValue);
                            }
                        } else {
                            result.put(field.getName(), fieldValue);
                        }
                    }
                });
        // is sub class of Entity
        if (superClass.isAssignableFrom(subClass.getSuperclass())) {
            result.putAll(getEntityValuedProperties(entity, superClass, subClass.getSuperclass()));
        }
        return result;
    }

/*
    public static Map<String, Object> getEntityValuedProperties(Object entity) {
        Map<String, Object> result = new LinkedHashMap<>();
        Stream.of(entity.getClass().getDeclaredFields())
                .filter(field -> PropertyUtils.isReadable(entity, field.getName()))
                .filter(field -> getProperty(entity, field.getName()) != null)
                .forEach(field -> {
                    Object fieldValue = getProperty(entity, field.getName());
                    if (fieldValue instanceof Collection<?>) {
                        if (!((Collection<?>) fieldValue).isEmpty()) {
                            result.put(field.getName(), fieldValue);
                        }
                    } else {
                        if (fieldValue instanceof String) {
                            if (StringUtils.isNotBlank(fieldValue.toString())) {
                                result.put(field.getName(), fieldValue);
                            }
                        } else {
                            result.put(field.getName(), fieldValue);
                        }
                    }
                });
        return result;
    }
*/

    /*
     * 取得所有 Property 內容，包含 null
     * @param entity
     * @param superClass
     * @param subClass
     * @return
    public static Map<String, Object> getEntityProperties(Object entity, Class<?> superClass, Class<?> subClass) {
        Map<String, Object> result = new LinkedHashMap<>();
        Field[] fields = subClass.getDeclaredFields();
        try {
            for (Field f : fields) {
                String fieldName = f.getName();
                if (PropertyUtils.isReadable(entity, fieldName)) {
                    Object fieldValue = PropertyUtils.getProperty(entity, fieldName);
                    result.put(f.getName(), fieldValue);
                }
            }
            // is sub class of Entity
            if (superClass.isAssignableFrom(subClass.getSuperclass())) {
                result.putAll(getEntityProperties(entity, superClass, subClass.getSuperclass()));
            }
        } catch(Exception e) {
            log.error(e.getLocalizedMessage(), e);
            return Collections.emptyMap();
        }
        return result;
    }

     */
/*
    public static void assignValuedPropertiesToEntity(Object source, Object target) {
        Map<String, Object> values = getEntityValuedProperties(source, source.getClass(), source.getClass());
        assignPropertiesToEntity(values, target);
    }
    
    public static void assignPropertiesToEntity(Object source, Object target) {
    	Map<String, Object> values = getEntityProperties(source, source.getClass(), source.getClass());
    	assignPropertiesToEntity(values, target);
    }

    private static void assignPropertiesToEntity(Map<String, Object> values, Object target) {
    	Set<String> propertyNames = values.keySet();
    	for (String propertyName: propertyNames) {
    		try {
    			setProperty(target, propertyName, values.get(propertyName));
    		} catch (Exception e) {
    			log.error(e.getLocalizedMessage(), e);
    		}
    	}
    }

    public static void readValuesFromMap(Map<String, Object> values, Object target) {
        Set<String> propertyNames = values.keySet();
        for (String propertyName: propertyNames) {
            try {
                setProperty(target, propertyName, values.get(propertyName));
            } catch (Exception e) {
                log.error(e.getLocalizedMessage(), e);
            }
        }
    }

    public static <T> T cloneEntity(Object source) {
        try {
            return (T) BeanUtils.cloneBean(source);
        } catch(Exception e) {
            log.error(e.getLocalizedMessage(),e);
        }
        return null;
    }

    public static boolean isPropertyExists(Object entity, String propertyName) {
        PropertyDescriptor propDesc;
        try {
            propDesc = PropertyUtils.getPropertyDescriptor(entity, propertyName);
            return (propDesc != null);
        } catch(Exception e) {
            log.error(e.getLocalizedMessage(),e);
        }
        return false;
    }
*/

}
