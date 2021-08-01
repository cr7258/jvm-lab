package java.lang;

/**
 * @author chengzw
 * @description 自己写的java.lang.String.class类不会被加载，这样便可以防止核心API库被随意篡改
 * @since 2021/7/18
 */
public class String {
    public static void main(String[] args) {
        System.out.println("**************My String Class**************");
    }
}
