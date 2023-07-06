package etu2009.framework.servlet;
 
import java.lang.annotation.*;
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface GetUrl {
    String url() default "";
}
