package fastrpc.context;

import fastrpc.context.annotation.Bean;
import fastrpc.context.annotation.Weave;
import fastrpc.context.exception.WeaveException;
import fastrpc.context.support.BeanUtils;
import fastrpc.exception.BeanNotFindException;
import fastrpc.exception.ParameterValidException;
import fastrpc.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * TODO
 * 目前只实现了属性注入
 */
public class RpcBeanContent implements BeanContent {
    private static final String packagePath = "fastrpc";
    private Map<String, Object> beanNameInstanceMapper = new HashMap<>();
    private Map<String, Class> classNamemapper = new HashMap<>();
    private List<Class> classList = new ArrayList<>();

    private Logger logger = LoggerFactory.getLogger(RpcBeanContent.class);

    public void init() {
        init(packagePath);
    }

    public void init(String pkg) {
        try {
            scanBean(pkg);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void scanBean(String pkg) throws IllegalAccessException {
        classList = BeanUtils.getClass(pkg, Bean.class);
        if (classList != null) {
            mapperBean();
        }
    }

    private void mapperBean() throws IllegalAccessException {
        for (Class clazz : classList) {
            String beanName = ((Bean) clazz.getAnnotation(Bean.class)).name();
            if (beanName.equals("")) {
                beanName = StringUtils.lowerFirstWorld(clazz.getSimpleName());
            }

            boolean isInstance = ((Bean) clazz.getAnnotation(Bean.class)).instance();
            if (!isInstance) {
                classNamemapper.put(clazz.getSimpleName(), clazz);
                continue;
            }
            Object instance = BeanUtils.getBeanInstance(clazz);
            if (instance == null) {
                continue;
            }
            beanNameInstanceMapper.put(beanName, instance);
        }

        if (beanNameInstanceMapper.isEmpty()) {
            return;
        }

        /**
         * 处理依赖注入
         * TODO
         * 目前只处理属性注入
         */
        for (Map.Entry<String, Object> entry : beanNameInstanceMapper.entrySet()) {
            Object object = entry.getValue();
            Field[] fields = object.getClass().getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(Weave.class)) {
                    String filedName = StringUtils.lowerFirstWorld(field.getType().getSimpleName());
                    Weave annotation = field.getAnnotation(Weave.class);
                    if (!annotation.name().equals("")) {
                        filedName = annotation.name();
                    }
                    logger.info("获取bean实例：" + filedName);
                    Object ins = beanNameInstanceMapper.get(filedName);
                    if (ins == null) {
                        logger.info("获取bean实例失败：" + filedName);
                        throw new WeaveException("依赖注入处理异常：发现需注入的bean未完成实例化...");
                    }
                    field.setAccessible(true);
                    field.set(object, ins);
                }
            }
        }

        /**
         * 非实例化类，静态属性注入
         */
        for (Map.Entry<String, Class> entry : classNamemapper.entrySet()) {
            String name = entry.getKey();
            Class clazz = entry.getValue();
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                if (!field.isAnnotationPresent(Weave.class) || !Modifier.isStatic(field.getModifiers())) {
                    continue;
                }
                Weave weave = field.getAnnotation(Weave.class);
                String acquireName = weave.name();
                if (acquireName.equals("")) {
                    acquireName = field.getName();
                }
                field.setAccessible(true);
                Object ins = beanNameInstanceMapper.get(acquireName);
                field.set(null, ins);
            }
        }
    }

    public List<Class> getClassList() {
        return this.classList;
    }

    public Map<String, Object> getBeanNameInstanceMapper() {
        return this.beanNameInstanceMapper;
    }

    @Override

    public <T> T getBeanByName(String name) {
        T res = (T) beanNameInstanceMapper.get(name);
        if (Objects.isNull(res)) {
            throw new BeanNotFindException("Can't found this bean : " + name);
        }
        return res;
    }

    @Override
    public <T> T getBean(Class<T> clazz) {
        if (clazz == null) {
            throw new ParameterValidException("Parameter Validation Exception : clazz null");
        }

        String name = StringUtils.lowerFirstWorld(clazz.getSimpleName());
        return getBeanByName(name);
    }

}
