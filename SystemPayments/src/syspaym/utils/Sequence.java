package syspaym.utils;

/**
 * Created by Admin on 28.04.2016.
 */
public final class Sequence {
    private static Long _nextId;

    public static Long getNextId()
    {
        if(_nextId == null)
            _nextId = 0L;

        return ++_nextId;
    }
}
