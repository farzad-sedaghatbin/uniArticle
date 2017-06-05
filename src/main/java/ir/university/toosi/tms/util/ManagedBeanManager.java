package ir.university.toosi.tms.util;


import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.naming.InitialContext;
import java.util.Set;

/**
 * @author : Hamed Hatami , Javad Sarhadi , Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */
public class ManagedBeanManager {

    private final static String BEAN_MANAGER_PATH = Configuration.getProperty("BEAN_MANAGER_PATH");

    private static BeanManager getBeanManager() {
        try {
            return (BeanManager) new InitialContext().lookup(BEAN_MANAGER_PATH);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static <T> T lookup(Class<T> clazz) {
        try {
            BeanManager beanManager = getBeanManager();
            Set<Bean<?>> beans = beanManager.getBeans(clazz);
            if (beans.isEmpty()) {
                return null;
            }
            Bean<T> bean = (Bean<T>) beans.iterator().next();
            T beanInstance = beanManager.getContext(bean.getScope()).get(bean);
            if (beanInstance != null) {
                return beanInstance;
            }
            CreationalContext<T> ct = beanManager.createCreationalContext(bean);
            return beanManager.getContext(bean.getScope()).get(bean, ct);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static <T> T lookup(String name) {
        try {
            BeanManager beanManager = getBeanManager();
            Set<Bean<?>> beans = beanManager.getBeans(name);
            if (beans.isEmpty()) {
                return null;
            }
            Bean<T> bean = (Bean<T>) beans.iterator().next();
            T beanInstance = beanManager.getContext(bean.getScope()).get(bean);
            if (beanInstance != null) {
                return beanInstance;
            }
            CreationalContext<T> ct = beanManager.createCreationalContext(bean);
            return beanManager.getContext(bean.getScope()).get(bean, ct);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}