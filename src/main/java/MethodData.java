import lombok.Builder;
import lombok.Getter;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

@Builder
@Getter
public class MethodData {
    private String className;
    private String methodName;
    private String annotationName;
    private int priority;

    private Method method;

    public void invoke() {
        try {
            Class<?> clazz = Class.forName(className);
            Object testClass = clazz.getDeclaredConstructor().newInstance();

            Parameter par1 = method.getParameters()[0];
            Class<?> par1Type = par1.getType();

            // в качестве примера некая вариативность параметров методов (на все случаи жизни не придумаешь)
            if (par1Type.equals(int.class)) {
                method.invoke(testClass, 1);
            } else if(par1Type.equals(String.class)) {
                method.invoke(testClass, "abc");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
