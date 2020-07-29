package com.hujinwen.entity.tuple;

/**
 * Created by joe on 2020/7/29
 * <p>
 * 三元组
 */
public class ThreeTuple<A, B, C> {

    public final A first;

    public final B second;

    public final C third;

    public ThreeTuple(A first, B second, C third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }
}
