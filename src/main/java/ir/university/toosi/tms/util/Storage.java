package ir.university.toosi.tms.util;

import java.util.Hashtable;

/**
 * @author :  FarzadFarzad Sedaghatbin
 * @version : 1.0
 */

public class Storage<K, V> extends Hashtable<K, V> {

    private V defaultValue;

    public Storage(V defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Override
    public synchronized V get(Object key) {
        if (key != null) {
            V val = super.get(key);
            if (val != null) {
                return val;
            }
        }
        return defaultValue;
    }
}
