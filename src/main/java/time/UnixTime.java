package time;

import java.util.Date;

/**
 * @description:
 * @create: 2018-11-07 16:45
 */
public class UnixTime {
    private final long value;

    public UnixTime() {
        this(System.currentTimeMillis () / 1000L + 2208988800L);
    }

    public UnixTime(long time) {
        this.value = time;
    }

    public long getValue() {
        return value;
    }

    @Override
    public String toString() {
        return new Date ((getValue () - 2208988800L) * 1000L).toString ();
    }
}
