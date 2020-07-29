package com.hujinwen.entity.tuple;

/**
 * Created by joe on 2020/4/8
 * <p>
 * 二元组
 */
public class TwoTuple<A, B> {
    public final A first;

    public final B second;

    public TwoTuple(A first, B second) {
        this.first = first;
        this.second = second;
    }

}
