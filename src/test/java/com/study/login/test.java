package com.study.login;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

public class test {
    /**
     * 경품 추첨을 위한 N개의 번호(0보다 큰 정수)들이 있다.
     * 이 번호들은 1부터 N사이의 정수들 중의 하나여야 한다.
     * 이 번호들 중 빠진 번호가 있는지를 찾아내는 알고리즘을 구하여라.
     */

    public static void main(String[] args) {
        Integer[] numArr = {1,2,3,4,5,8};

        Set<Integer> set  = new HashSet<>();


        int length = numArr.length;
        for(int i=1; i<length+1; i++) {
            set.add(i);
        }


        set.removeAll(Arrays.asList(numArr));

        System.out.println(set);
    }
}
