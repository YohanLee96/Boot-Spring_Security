package com.study.login;


import org.junit.jupiter.api.Test;

import java.util.function.Function;

public class UtilTest {

    @Test
    void test() {
        final Function<Integer, Integer> identity = Function.identity();
        System.out.println(" >> " + identity.apply(3));

        final Function<Integer, Integer> identity2 = t->t;
        System.out.println(" >> "+ identity.apply(444));
    }
}
