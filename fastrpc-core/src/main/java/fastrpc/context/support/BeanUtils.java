package fastrpc.context.support;

import fastrpc.utils.ClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BeanUtils {
    List<Class> classes = new ArrayList<>();
    private static Logger logger = LoggerFactory.getLogger(BeanUtils.class);

    /**
     * TODO
     * 只处理默认构造器情况
     * 未考虑含参构造器
     */
    public static  <T> T getBeanInstance(Class<T> clazz){
        try {
            return clazz.newInstance();
        } catch (InstantiationException e) {
            logger.info("实例化对象失败：className = = > " + clazz.getName());
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Class> getClass(String pkg) {
        return ClassUtils.getClass(pkg);
    }

    public static List<Class> getClass(String pkg, Class anno) {

        List<Class> result = new ArrayList<>();

        List<Class> classes = getClass(pkg);

        if (Objects.isNull(classes) || classes.isEmpty()) {
            return null;
        }
        for (Class clazz : classes) {
            if (clazz.isAnnotationPresent(anno)) {
                result.add(clazz);
            }
        }

        return result;
    }

}
