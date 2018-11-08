package hashmap;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @description: This is used to test HashMap
 * @author: Qiao.Jian
 * @create: 2018-09-16 10:11
 */
public class HashMapTest {
    private static  Map<String, String> employers = new HashMap<> (  );

    public static void main(String[] args) {
        employers.put ( "1", "qiaojian" );
        employers.put ( "2", "leo" );
        employers.put ( "3", null );
        employers.put ( "4", "min" );
        employers.put ( null, "空1" );

        Iterator<Map.Entry<String, String>> iter = employers.entrySet ().iterator ();
        while (iter.hasNext ()) {
            //Exception in thread "main" java.util.ConcurrentModificationException
            //employers.put ( "5", "bab" );
            Map.Entry<String, String> next = iter.next ();
            if (( "2" ).equals (next.getKey ())) {
                iter.remove ();
            }
        }
        System.out.println ( employers.size () );
        employers.put ( null, "空2" );
        employers.put ( null, "空3" );
        employers.put ( "3", "非空" );
        System.out.println ( employers.size () );
    }
}
