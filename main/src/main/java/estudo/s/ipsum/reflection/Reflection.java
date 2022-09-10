package estudo.s.ipsum.reflection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import estudo.s.ipsum.exception.ReflectException;

public class Reflection {
    
    private Object reflected;

    public Reflection(Object toReflect) {
        this.reflected = toReflect;
    }

    public Object invoke(String declaredMethodName, Object... args) {
        try {
            Method declaredMethod = getDeclaredMethod(declaredMethodName);

            return declaredMethod.invoke(reflected, args);
        
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
            throw new ReflectException(e.getMessage());
        }
    }

    public Method getDeclaredMethod(String declaredMethodName) {
        try {
            return reflected.getClass().getDeclaredMethod(declaredMethodName);

        } catch (NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
            throw new ReflectException(e.getMessage());
        }
    }

}
