import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Tester {

    /**
     * SYNTAX 1
     *
     * @param testClass - класс
     */
    public static void start(Class<?> testClass) {
        checkIfTestable(testClass);
        List<MethodData> methodData = gatherReflectionData(testClass);
        methodData.forEach(MethodData::invoke);
    }

    /**
     * SYNTAX 2
     *
     * @param testClass - имя класса
     */
    public static void start(String testClass) {
        try {
            start(Class.forName(testClass));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * проверка, что класс нужно тестировать (наличие анотации @Test)
     */
    private static void checkIfTestable(Class<?> clazz) {
        if (!clazz.isAnnotationPresent(Test.class)) {
            throw new checkIfTestableException("The class "
                    + clazz.getSimpleName()
                    + " is not annotated with @Test");
        }
    }

    /**
     *
     * @param clazz - класс
     * @return - List<MethodData> список объектов MethodData
     * отсортированный в порядке приорита выполнения методов тестирования
     */
    private static List<MethodData> gatherReflectionData(Class<?> clazz) {
        List<MethodData> beforeData = Arrays.stream(clazz.getDeclaredMethods())
                .filter(m -> m.isAnnotationPresent(BeforeSuite.class))
                .map(m -> MethodData.builder()
                        .className(clazz.getName())
                        .method(m)
                        .annotationName("BeforeSuite")
                        .methodName(m.getName())
                        .priority(0)
                        .build()
                ).toList();
        if (beforeData.size() > 1) {
            throw new MethodNotSingletonException("@BeforeSuite");
        }

        List<MethodData> afterData = Arrays.stream(clazz.getDeclaredMethods())
                .filter(m -> m.isAnnotationPresent(AfterSuite.class))
                .map(m -> MethodData.builder()
                        .className(clazz.getName())
                        .method(m)
                        .methodName(m.getName())
                        .annotationName("AfterSuite")
                        .priority(Integer.MAX_VALUE)
                        .build()
                ).toList();
        if (afterData.size() > 1) {
            throw new MethodNotSingletonException("@AfterSuite");
        }

        List<MethodData> testData = Arrays.stream(clazz.getDeclaredMethods())
                .filter(m -> m.isAnnotationPresent(Test.class))
                .map(m -> MethodData.builder()
                        .className(clazz.getName())
                        .method(m)
                        .annotationName("Test")
                        .methodName(m.getName())
                        .priority(m.getAnnotation(Test.class).priority()
                        ).build()
                ).toList();

        List<MethodData> dataList = new ArrayList<>();
        dataList.addAll(beforeData);
        dataList.addAll(testData);
        dataList.addAll(afterData);

        dataList.sort(Comparator.comparingInt(MethodData::getPriority));

        return dataList;
    }

}
