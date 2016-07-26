package com.szxyyd.mpxyhl.inter;

import rx.functions.Function;

/**
 * Created by jq on 2016/7/22.
 */
public interface Func3<T1, T2, T3, R> extends Function {
    R call(T1 t1, T2 t2, T3 t3);
}
