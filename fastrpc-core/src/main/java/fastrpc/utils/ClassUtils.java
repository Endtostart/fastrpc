package fastrpc.utils;

import fastrpc.context.annotation.RpcService;

import java.io.File;
import java.io.IOException;
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
}
