package com.moxie.commons;

import com.google.common.base.Preconditions;
import com.netflix.astyanax.util.TimeUUIDUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wangyanbo on 16/11/14.
 */
public class BaseStringUtils {
    private static final String IgnoreChars = "*#";

    /**
     * @param ignoredChars 比较s1和s2时可忽略的字符合集
     * @param matchCount   除开ignoredChars至少有多少个字符s1和s2相同
     * @return 1-s1和s2除开ignoredChars外其它字符都匹配且匹配的字符数大于或等于matchCount;
     * 0-s1和s2除开ignoredChars外其它字符都匹配但匹配的字符数小于matchCount;
     * -1-s1和s2有不匹配的字符且该字符不包含在IgnoredChars中
     */
    public static int compare(String s1, String s2, String ignoredChars, int matchCount) {
        if (ignoredChars == null) {
            return s1.equals(s2) ? 1 : -1;
        }

        if (length(s1, ignoredChars) == 0 || length(s2, ignoredChars) == 0) {
            return 0;
        }

        if (s1.length() != s2.length()) {
            return -1;
        }

        int count = 0;
        for (int i = 0; i < s1.length(); i++) {
            if (!StringUtils.contains(ignoredChars, s1.charAt(i)) && !StringUtils.contains(ignoredChars, s2.charAt(i))) {
                if (s1.charAt(i) == s2.charAt(i)) {
                    count++;
                } else {
                    return -1;
                }
            }
        }

        if (matchCount == -1) {
            matchCount = s1.length();
        }

        if (count >= matchCount) {
            return 1;
        } else {
            return 0;
        }
    }

    public static int compare(String s1, String s2) {
        return compare(s1, s2, -1);
    }

    public static int compare(String s1, String s2, int matchCount) {
        return compare(s1, s2, IgnoreChars, matchCount);
    }

    /**
     * @param ignoredChars 比较s1和s2时可忽略的字符合集
     * @return null-如果s1,s2除开ignoredChars外有不相同的字符
     * 合并后的字符串- 12*4 与 1*34 在ignoredChars为 * 时合并的结果为 1234
     */
    public static String merge(String s1, String s2, String ignoredChars) {
        Preconditions.checkArgument(StringUtils.isNotBlank(ignoredChars), "ignoredChars不能为空");
        if (length(s1, ignoredChars) == 0) {
            return s2;
        }
        if (length(s2, ignoredChars) == 0) {
            return s1;
        }
        if (s1.length() != s2.length()) {
            return null;
        }

        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < s1.length(); i++) {
            if (!StringUtils.contains(ignoredChars, s1.charAt(i)) && !StringUtils.contains(ignoredChars, s2.charAt(i))) {
                if (s1.charAt(i) == s2.charAt(i)) {
                    buf.append(s1.charAt(i));
                } else {
                    return null;
                }
            } else {
                buf.append(StringUtils.contains(ignoredChars, s1.charAt(i)) ? s2.charAt(i) : s1.charAt(i));
            }
        }
        return buf.toString();
    }

    public static String merge(String s1, String s2) {
        return merge(s1, s2, IgnoreChars);
    }

    public static String mergeSave(String s1, String s2) {
        return mergeSave(s1, s2, IgnoreChars);
    }

    /**
     * 返回合并后的字符串, 如无法合并返回有效字符数最多的字符串
     */
    public static String mergeSave(String s1, String s2, String ignoredChars) {
        String result = s2;
        if (length(s1, ignoredChars) != 0) {
            if (length(s2, ignoredChars) != 0) {
                String merged = BaseStringUtils.merge(s1, s2, "*#");
                if (StringUtils.isNotBlank(merged)) { //合并成功
                    result = merged;
                } else if (length(s1, ignoredChars) > length(s2, ignoredChars)) {
                    result = s1;
                }
            } else {
                result = s1;
            }
        } else if (s1 != null && s2 == null) {
            result = s1;
        }

        return result;
    }

    public static int length(String src, String ignoreChars) {
        if (StringUtils.isBlank(src)) {
            return 0;
        }

        return src.length() - ignoreChars.chars().map(c -> StringUtils.countMatches(src, (char) c)).sum();
    }

    public static String uuid() {
        return TimeUUIDUtils.getUniqueTimeUUIDinMillis().toString();
    }

    /**
     * 将正常uuid中的'-'去掉
     */
    public static String uuidSimple() {
        return uuid().replaceAll("-", "");
    }

    public static long timeFromUUID(String uuid) {
        if (StringUtils.isBlank(uuid)) {
            throw new IllegalArgumentException("uuid为空");
        }
        return TimeUUIDUtils.getTimeFromUUID(UUID.fromString(uuid));
    }

    public static long timeFromSimpleUUID(String simpleUUID) {
        StringBuilder buf = new StringBuilder();
        buf.append(simpleUUID.substring(0, 8)).append("-")
                .append(simpleUUID.substring(8, 12)).append("-")
                .append(simpleUUID.substring(12, 16)).append("-")
                .append(simpleUUID.substring(16, 20)).append("-")
                .append(simpleUUID.substring(20));
        return timeFromUUID(buf.toString());
    }

    public static boolean contains(String src, String contained) {
        return contains(src, contained, IgnoreChars);
    }

    public static boolean contains(String src, String contained, String ignoreChars) {
        if (StringUtils.isBlank(ignoreChars)) {
            return src.contains(contained);
        }

        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < contained.length(); i++) {
            if (StringUtils.contains(ignoreChars, contained.charAt(i))) {
                buf.append(".");
            } else {
                buf.append(contained.charAt(i));
            }
        }

        Matcher matcher = Pattern.compile(buf.toString()).matcher(src);
        if (matcher.find()) {
            return true;
        } else {
            return false;
        }
    }

    public static String collapseWhiteSpace(String src) {
        if (src == null) {
            return null;
        }

        return src.replaceAll("[\\s\\u00A0]{2,}", " ");
    }

    public static String substringAfter(String src, String separator) {
        String result = StringUtils.substringAfter(src, separator);
        return StringUtils.defaultIfBlank(result, src);
    }

    /**
     * @return 下划线下的字符变成大写; 连续两个大写后面的大写变小写. 样例请参照BaseStringUtilsTest.java
     */
    public static String underScoreToCamel(String src, boolean firstLetterUpperCase) {
        src = src.replaceAll("_\\d{1,}$", "");

        StringBuilder buf = new StringBuilder();
        Arrays.stream(src.split("_")).forEach(s ->
                buf.append(toCamel(s))
        );

        String result = buf.toString();
        if (firstLetterUpperCase) {
            result = Character.toUpperCase(result.charAt(0)) + result.substring(1);
        } else {
            result = Character.toLowerCase(result.charAt(0)) + result.substring(1);
        }

        return result;
    }

    private static String toCamel(String word) {
        word = Character.toUpperCase(word.charAt(0)) + word.substring(1);
        StringBuilder buf = new StringBuilder();
        Matcher matcher = Pattern.compile("[A-Z]{2,}").matcher(word);
        int end = 0;
        while (matcher.find()) {
            int start = matcher.start();
            end = matcher.end();
            buf.append(word.substring(0, start + 1));
            buf.append(matcher.group().substring(1).toLowerCase());
        }
        buf.append(word.substring(end));
        String result = buf.toString();
        return result.substring(0, result.length() - 1) + Character.toLowerCase(result.charAt(result.length() - 1));
    }
}
