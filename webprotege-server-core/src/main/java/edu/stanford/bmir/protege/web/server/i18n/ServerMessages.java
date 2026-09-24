package edu.stanford.bmir.protege.web.server.i18n;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 轻量的服务端用户可见文案 i18n 支持。
 *
 * 文案目录位于 classpath 下的 messages_zh.properties（UTF-8 编码）。
 * 当前请求语言由 DispatchServlet 在请求开始时设置、结束时清除；
 * 未设置语言、语言非中文或目录未命中时，返回调用点提供的英文缺省文案。
 */
public class ServerMessages {

    private static final ThreadLocal<Locale> CURRENT_LOCALE = new ThreadLocal<>();

    private static final Map<String, String> ZH_CATALOG = loadCatalog();

    public static void setLocale(Locale locale) {
        CURRENT_LOCALE.set(locale);
    }

    public static void clear() {
        CURRENT_LOCALE.remove();
    }

    /**
     * Gets the message for the current request locale.
     *
     * @param key            the catalog key
     * @param defaultEnglish the English message used as the fallback
     * @param args           optional MessageFormat arguments
     * @return the message for the current locale
     */
    public static String get(String key, String defaultEnglish, Object... args) {
        String message = defaultEnglish;
        Locale locale = CURRENT_LOCALE.get();
        if (locale != null && "zh".equalsIgnoreCase(locale.getLanguage())) {
            message = ZH_CATALOG.getOrDefault(key, defaultEnglish);
        }
        if (args.length > 0) {
            return MessageFormat.format(message, args);
        }
        return message;
    }

    /**
     * Gets the Chinese message from the catalog, regardless of any request bound locale.
     * Used by background threads (e.g. mailers) whose locale is configured rather than
     * derived from the current HTTP request.
     */
    public static String getChinese(String key, String defaultEnglish, Object... args) {
        String message = ZH_CATALOG.getOrDefault(key, defaultEnglish);
        if (args.length > 0) {
            return MessageFormat.format(message, args);
        }
        return message;
    }

    private static Map<String, String> loadCatalog() {
        Properties properties = new Properties();
        try (InputStream in = ServerMessages.class.getClassLoader().getResourceAsStream("messages_zh.properties")) {
            if (in != null) {
                properties.load(new InputStreamReader(in, StandardCharsets.UTF_8));
            }
        } catch (IOException ignored) {
            // 目录缺失或不可读时保持英文缺省文案
        }
        Map<String, String> catalog = new ConcurrentHashMap<>();
        properties.forEach((key, value) -> catalog.put(String.valueOf(key), String.valueOf(value)));
        return catalog;
    }
}
