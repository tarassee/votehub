package com.tarasiuk.votehub.util;

import java.util.Collection;

public final class RandomSelectionUtil {

    private RandomSelectionUtil() {
    }

    // todo: refactor
    public static <T> T selectOneRandomly(Collection<T> coll) {
        int num = (int) (Math.random() * coll.size());
        for (T t : coll) if (--num < 0) return t;
        throw new AssertionError();
    }

}
