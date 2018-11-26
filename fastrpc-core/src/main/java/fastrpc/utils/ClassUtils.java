package fastrpc.utils;

import fastrpc.context.annotation.RpcService;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class ClassUtils {
    /**
     * 获取文件目录下的所有类
     * @param packageName
     * @return
     */
    public static ArrayList<Class> getClass(String packageName) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String path = packageName.replace(".", "/");
        try {
            Enumeration resources = classLoader.getResources(path);
            List<File> dirs = new ArrayList();
            while (resources.hasMoreElements()) {
                URL resource = (URL) resources.nextElement();
                dirs.add(new File(resource.getFile()));
            }
            ArrayList classes = new ArrayList();
            for (File file : dirs) {
                classes.addAll(findClass(file, packageName));
            }
            return classes;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取包文件下的类
     * @param dir
     * @param packageName
     * @return
     * @throws ClassNotFoundException
     */
    private static List<Class> findClass(File dir, String packageName) throws ClassNotFoundException {
        List<Class> classes = new ArrayList<>();
        if (!dir.exists()) {
            return classes;
        }
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                classes.addAll(findClass(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                classes.add(Class.forName(packageName + "." + file.getName().substring(0, file.getName().length() - 6)));
            }
        }
        return classes;
    }

    /**
     * 判断两个方法是否相同
     */
    public static boolean compareMethod(Method m1, Method m2) {
        if (!m1.getName().equals(m2.getName())) {
            return false;
        }

        Type[] paramType1 = m1.getParameterTypes();
        Type[] paramType2 = m2.getParameterTypes();
        if (paramType1.length != paramType2.length) {
            return false;
        }

        for (int i = 0; i < paramType1.length; i++) {
            if (!paramType1[i].getTypeName().equals(paramType2[i].getTypeName())) {
                return false;
            }
        }

        return true;
    }
}
