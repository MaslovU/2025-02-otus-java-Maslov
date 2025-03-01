package ru.otus;

import com.google.common.collect.Lists;
import com.google.common.math.LongMath;

import java.util.ArrayList;
import java.util.List;

public class HelloOtus {
    public static void main(String[] args) {
        List<Integer> example = new ArrayList<>();
        int min = 0;
        int max = 10;
        for (int i = min; i < max; i++) {
            example.add(i);
        }

        var myMath = LongMath.ceilingPowerOfTwo(max);

        System.out.println("The smallest power of two greater than or equal to " + max + " is: " + myMath);

        System.out.println(Lists.reverse(example));
    }
}
