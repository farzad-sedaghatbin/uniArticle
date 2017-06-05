package ir.university.toosi.wtms.web.util;


import ir.university.toosi.wtms.web.action.UserManagementAction;

/**
 * @author : Hamed Hatami Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */
public class BundleUtil {

    private static UserManagementAction me = (UserManagementAction) ManagedBeanManager.lookup(UserManagementAction.class);

    public static synchronized String getBundleValue(String bundleKey, String... arguments) {
        return me.getBundleMessage(bundleKey, arguments);
    }

}
