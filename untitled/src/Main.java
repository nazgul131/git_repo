import javax.annotation.Resource;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 26.04.2016.
 */


public class Main implements Resource {
    int a = 2;

    String brand;
    public boolean b;

    public static void m(final List args)
    {
        args.add("1");
    }

    public static void main(String[] args)
    {
        Main m = new Main();

        List I = new ArrayList();

        Object s = new String().getClass();
        System.out.println(I.size());
    }

    @Override
    public String name() {
        return null;
    }

    @Override
    public String lookup() {
        return null;
    }

    @Override
    public Class type() {
        return null;
    }

    @Override
    public AuthenticationType authenticationType() {
        return null;
    }

    @Override
    public boolean shareable() {
        return false;
    }

    @Override
    public String mappedName() {
        return null;
    }

    @Override
    public String description() {
        return null;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return null;
    }
}
