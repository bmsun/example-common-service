package com.example.commons.utils;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import javax.script.*;
import java.io.*;
import java.net.URL;

/***
 * @Date 2018/4/10
 * @Description java调用js工具类
 *                    饿汉式单例
 * @author zhanghesheng
 * */
public class JsUtils {
    private static final String DEFAUT_ENCODING = "utf-8";
    private static final String LINE_SEPARATOR_UNIX = "\n";
    private static final String LINE_SEPARATOR_WINDOWS = "\r\n";
    private static final char DIR_SEPARATOR_UNIX = '/';
    private static final char DIR_SEPARATOR_WINDOWS = '\\';

    private JsUtils() {
    }

    private static JsUtils instance = new JsUtils();

    public static JsUtils getInstance() {
        return instance;
    }

    private ScriptEngine getJsEngine() {
        // 得到一个ScriptEngine对象
        ScriptEngineManager maneger = new ScriptEngineManager();
        ScriptEngine engine = maneger.getEngineByName("js");
        return engine;
    }

    /**
     * @param jsFilePath   js文件路径
     * @param functionName js方法名
     * @param var          js方法参数集
     * @return
     * @author zhanghesheng
     * @date 2018/4/10
     * @Description
     */
    public String callJs(String jsFilePath, String functionName, String... var) {
        String result =null;
        ScriptEngine jsEngine = this.getJsEngine();
        /*为文件注入全局变量*/
        // Bindings bindings = jsEngine.createBindings();
        //bindings.put("factor", 2);
        /*设置绑定参数的作用域*/
        // jsEngine.setBindings(bindings, ScriptContext.ENGINE_SCOPE);
        try {
            InputStream inputStream;
            if (jsFilePath.charAt(0) == DIR_SEPARATOR_UNIX || jsFilePath.charAt(0) == DIR_SEPARATOR_WINDOWS) {
                inputStream = new FileInputStream(jsFilePath);
            } else {//相对路径
                inputStream = ReadConfigUtils.class.getResourceAsStream(DIR_SEPARATOR_UNIX + jsFilePath);
            }
            InputStreamReader reader = new InputStreamReader(inputStream);
            jsEngine.eval(reader);
            if (jsEngine instanceof Invocable) {
                // 调用JS方法
                Invocable invocable = (Invocable) jsEngine;
                result = (String) invocable.invokeFunction(functionName, var);

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ScriptException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        return result;

    }

    public  Object eval(String condition) throws ScriptException {
        if (StringUtils.isBlank(condition)) {
            return null;
        }
        return getJsEngine().eval(condition);
    }


   //测试
    public static void main(String[] args) throws Exception {
        JsUtils instance = JsUtils.getInstance();
        Object eval = instance.eval("2>1");
        System.out.println(eval);
        String uifEncode = instance.callJs("des.js", "uifEncode", "123");
        System.out.println(uifEncode);
        String uifDecode = instance.callJs("des.js", "uifDecode", uifEncode);
        System.out.println(uifDecode);

    }
}
