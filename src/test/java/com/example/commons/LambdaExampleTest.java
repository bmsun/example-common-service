package com.example.commons;

import com.example.commons.model.UserEntity;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/***
 * @Date 2018/3/13
 * @Description jdk1.8  lambda函数式编程案例
 * @author zhanghesheng
 * */
public class LambdaExampleTest {
      int flag = 0;

    /**
     * 匿名函数:
     * 实基类必须是只有一个抽象方法的接口
     */
    @Test
    public void testLambdaInterface() {
        //void
        LambdaInterface0 lambdaInterface0 = name -> {
            System.out.println("Hi:\t" + name);
        };
        //测试
        String name = "zhangsan";
        lambdaInterface0.sayHi(name);


        //有返回值
        LambdaInterface1 lambdaInterface1 = name1 -> {
            name1 = "Hi:\t" + name1;
            return name1;
        };
        //测试
        String name1 = "zhangsan";
        String s = lambdaInterface1.sayHi(name);
        System.out.println(s);
    }


    /**
     * 匿名函数:
     * 多线程应用
     */
    @Test
    public void testThread() {

        try {
            System.out.println("Im main thread");
            TimeUnit.MILLISECONDS.sleep(30);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //线程启动
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        executorService.submit(
                /**lambda函数式*/
                () -> {
            for (int i = 1; i < 1000; i++) {
                flag+=1;
                System.out.println("Im thread1"+"\t"+flag);
            }
        });
        executorService.submit(
                /**lambda函数式*/
                () -> {
            for (int i = 1; i < 1000; i++) {
                flag+=1;
                System.out.println("Im thread2"+"\t"+flag);
            }
        });

        // 关闭启动线程
        executorService.shutdown();


    }

    /**
     * 对Collection处理：
     *    1、forEach: 遍历;
     *    2、filter: 根据条件过滤;
     *    3、加入Predicate: 即加入判断条件;
     *    4、map: 将对象进行转换;
     *    5、distinct: 去重;
     *    6、reduce: 将所有值合并成一个,被称为折叠操作,SQL中类似 sum()、avg() 或者 count() 的聚集函数，实际上就是 reduce 操作
     *    7、数字求和，平均数，最大值，最小值。先调用mapToXxx方法，再调用sum(),avg()等方法
     *    8、skip: 跳过流的前n个元素
     *    9、limit: 截取流的前N个元素
     *    10 anyMatch：是否匹配任一元素
     *    11 allMatc 是否匹配所有元素
     *    12 noneMatch 是否未匹配所有元素： 与allMatch恰恰相反，它用于判断流中的所有元素是否都不满足指定条件
     */
    @Test
    public void testList() {
        List<String> features = Arrays.asList("Lambdas", "Lambdas","Default Method", "Stream API", "Date and Time API");
       /**1、forEach:遍历;*/
        //features.forEach(n -> System.out.println(n));
        // 使用Java 8的方法引用更方便，方法引用由::双冒号操作符标示，
        //features.forEach(System.out::println);

        /**2、filter:根据条件过滤*/
        List<String> filterList = features.stream().filter(name -> name.contains("API")).collect(Collectors.toList());
        //filterList.stream().forEach(System.out::println);
        //parallelStream 并行流，顺序随机
       // filterList.parallelStream().forEach(System.out::println);

        /**3、加入Predicate*/
        Predicate<String> startsWithJ = (feature) -> feature.startsWith("J");
        Predicate<String> fourLetterLong = (feature) -> feature.length() > 4;
        Predicate<String> andCondition = (feature) -> feature.length() <=10;
        /*features.stream()
                .filter(startsWithJ.or(fourLetterLong).and(andCondition))
                .forEach(System.out::println);
                */
        //等同如下
       /* features.stream()
                .filter(feature->feature.startsWith("J")||feature.length()>4&&feature.length()<=10)
                .forEach(System.out::println);
       */
        /**4、map:将对象进行转换*/
        List<String> mapList = features.stream().map((feature) -> StringUtils.deleteWhitespace(feature)).collect(Collectors.toList());
        //mapList.forEach(System.out::println);


        /**5、distinct:去重*/
       // features.stream().distinct().forEach(System.out::println);


        /**6、reduce:将所有值合并成一个*/
        List<Integer> costs = Arrays.asList(100, 200, 300, 400, 500);
        Integer integer = costs.stream().reduce((sum, cost) -> sum + cost).get();
        //System.out.println(integer);

        /**7、数字求和，平均数，最大值，最小值。先调用mapToXxx方法，再调用sum(),avg()等方法*/
        int sum = costs.stream().mapToInt(num->num).sum();
        Double avg = costs.stream().mapToInt(num->num).average().getAsDouble();
        int max = costs.stream().mapToInt(num->num).max().getAsInt();
        int min = costs.stream().mapToInt(num->num).min().getAsInt();
       // System.out.println(sum+"\t"+avg+"\t"+max+"\t"+min);

        /**8、skip: 跳过流的前n个元素*/
       // features.stream().skip(3).forEach(System.out::println);

        /**9、limit: 截取流的前N个元素*/
        List<String> result = features.stream()
                .limit(3)
                .collect(Collectors.toList());
        //result.forEach(System.out::println);
        /**10 anyMatch：是否匹配任一元素*/
        boolean flag = features.stream()
                .anyMatch(feature->feature.contains("API"));
        System.out.println(flag);

        /**11 allMatc 是否匹配所有元素：*/
         flag = features.stream()
                .allMatch(feature->feature.contains("API"));
        System.out.println(flag);

        /**12 noneMatch 是否未匹配所有元素： 与allMatch恰恰相反，它用于判断流中的所有元素是否都不满足指定条件*/
        flag = features.stream()
                .noneMatch(feature->feature.contains("API"));
        System.out.println(flag);

    }


    @Test
    public void testGroupBy(){


       List<UserEntity> userEntities = Lists.newArrayList();
        userEntities.add(new UserEntity("1","zhangsan",13,null));
        userEntities.add(new UserEntity("2","lisi",13,null));
        userEntities.add(new UserEntity("3","zhaowu",12,null));
        userEntities.add(new UserEntity("4","wangliu",14,null));

        /** Collectors.groupingBy(UserEntity::getAge, Collectors.counting())
         *   按年龄分组，并得到每个年龄段的总数
         * */
        Map<Integer, Long> map = userEntities.stream()
                .collect(Collectors.groupingBy(UserEntity::getAge, Collectors.counting()));

        /** Collectors.groupingBy(UserEntity::getAge, Collectors.counting())
         *   按年龄分组 key值为分组条件Age; value值为满足该条件的实体集合。
         * */
        Map<Integer, List<UserEntity>> listMap = userEntities.stream()
                .collect(Collectors.groupingBy(UserEntity::getAge));
        System.out.println(map);
        System.out.println(listMap);
    }


}


interface LambdaInterface0 {

    void sayHi(String name);
}

interface LambdaInterface1 {

    String sayHi(String name);
}