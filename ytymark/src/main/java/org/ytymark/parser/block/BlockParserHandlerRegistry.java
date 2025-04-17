package org.ytymark.parser.block;

import org.ytymark.enums.BlockParserHandlerEnum;
import org.ytymark.parser.ParserHandler;
import org.ytymark.annotation.BlockParserHandlerType;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.net.JarURLConnection;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 项目名称：ytymark
 * 作者：渊渟岳
 * 描述：扫描指定包下的 ParserHandlerType 注解类
 */
public class BlockParserHandlerRegistry {

    private final Map<String, ParserHandler> handlerMap = new HashMap<>();

    public BlockParserHandlerRegistry(String packageName) throws IOException, ClassNotFoundException {
        String path = packageName.replace('.', '/');
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Enumeration<URL> resources = classLoader.getResources(path);

        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            String protocol = resource.getProtocol();
            // 检查是什么方式启动的项目，使用不同的扫描路径
            if ("file".equals(protocol)) {
                File directory = new File(resource.getFile());
                scanDirectory(packageName, directory);
            } else if ("jar".equals(protocol)) {
                scanJar(resource, path);
            }else {
                throw new RuntimeException("未知异常");
            }
        }
    }

    /**
     * 非jar方式启动
     * @param packageName
     * @param directory
     */
    private void scanDirectory(String packageName, File directory) {
        if (!directory.exists()) return;
        File[] files = directory.listFiles();
        if (files == null) return;

        for (File file : files) {
            if (file.isDirectory()) {
                scanDirectory(packageName + "." + file.getName(), file);
            } else if (file.getName().endsWith(".class")) {
                String className = packageName + '.' + file.getName().replaceAll("\\.class$", "");
                loadHandlerClass(className);
            }
        }
    }

    /**
     * jar包方式启动
     * @param resource
     * @param path
     * @throws IOException
     */
    private void scanJar(URL resource, String path) throws IOException {
        JarURLConnection connection = (JarURLConnection) resource.openConnection();
        try (JarFile jarFile = connection.getJarFile()) {
            Enumeration<JarEntry> entries = jarFile.entries();

            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                String name = entry.getName();

                if (name.startsWith(path) && name.endsWith(".class") && !entry.isDirectory()) {
                    String className = name.replace('/', '.').replaceAll("\\.class$", "");
                    loadHandlerClass(className);
                }
            }
        }
    }

    /**
     * 加载 ParserHandlerType 注解下的类
     * @param className
     */
    private void loadHandlerClass(String className) {
        try {
            Class<?> clazz = Class.forName(className);
            if (ParserHandler.class.isAssignableFrom(clazz) && !Modifier.isAbstract(clazz.getModifiers())) {
                BlockParserHandlerType annotation = clazz.getAnnotation(BlockParserHandlerType.class);
                if (annotation != null) {
                    ParserHandler handler = (ParserHandler) clazz.getDeclaredConstructor().newInstance();
                    BlockParserHandlerEnum type = annotation.type();
                    String value = annotation.value().isEmpty() ? type.getValue() : annotation.value();
                    handlerMap.put(value, handler);
                }
            }
        } catch (Exception e) {
            System.err.println("加载类失败：" + className);
            e.printStackTrace();
        }
    }

    public Map<String, ParserHandler> getHandlerMap() {
        return handlerMap;
    }
}

