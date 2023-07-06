import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.stream.Collectors;

public class MyClass {

    public void myMethod(int huhu, String haha) {
        // do something
    }

    public static void main(String[] args) throws Exception {
       
        Class<?> clazz = MyClass.class;

        Method method = clazz.getMethod("myMethod", int.class, String.class);

        String[] parameterNames = Arrays.stream(method.getParameters())
                .map(Parameter::getName)
                .collect(Collectors.toList())
                .toArray(new String[0]);

        for (int i = 0; i < parameterNames.length; i++) {
            System.out.println("Nom du paramètre " + i + ": " + parameterNames[i]);
        }
    }
}
