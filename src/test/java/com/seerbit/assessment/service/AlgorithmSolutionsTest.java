package com.seerbit.assessment.service;

import com.seerbit.assessment.SeerbitAssessmentApplication;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SeerbitAssessmentApplication.class})
public class AlgorithmSolutionsTest {

    @Autowired
    private AlgorithmSolutions algorithmSolutions;

    @Test
    public void printPairs() {

        Assertions.assertEquals(Arrays.toString(new int[] {10, 6}), Arrays.toString(algorithmSolutions.printPairs(new int[] {1, 4, 45, 6, 10, 8},16)));
        Assertions.assertEquals(Arrays.toString(new int[] {1, -3}), Arrays.toString(algorithmSolutions.printPairs(new int[] {0, -1, 2, -3, 1},-2)));
        Assertions.assertEquals(Arrays.toString(new int[] {2,22}), Arrays.toString(algorithmSolutions.printPairs(new int[] {10, -1, 22, 2, 1},24)));
    }

    @Test
    public void findLowHigh() {
        System.out.println("findLowHigh");

        Assertions.assertEquals(Arrays.toString(new int[] {15, 17}), Arrays.toString(algorithmSolutions.findLowHigh(Arrays.asList(1, 1, 1, 2, 2, 2, 2, 2, 3, 3, 3, 4, 4, 4,4, 5, 5, 5, 6, 6, 6, 6, 6, 6),5)));
        Assertions.assertEquals(Arrays.toString(new int[] {-1, -1}), Arrays.toString(algorithmSolutions.findLowHigh(Arrays.asList(1, 1, 1, 2, 2, 2, 2, 2, 3, 3, 3, 4, 4, 4,4, 5, 5, 5, 6, 6, 6, 6, 6, 6),-2)));

    }

    @Test
    public void mergeIntervals(){
        Assertions.assertEquals("[1, 8] [10, 15] ", algorithmSolutions.mergeIntervals(Arrays.asList((new Pair(1,5)), new Pair(3,7), new Pair(4,6), new Pair(6,8), new Pair(10, 12), new Pair(11, 15))));
    }

    @Test
    public void mergeIntervals2(){
        Assertions.assertEquals( Arrays.deepToString(new int[][]{{1, 6}, {8, 10},{15, 18} }), Arrays.deepToString(algorithmSolutions.mergeInterval2(new int[][]{ {1,3}, {2, 6}, {8, 10}, {15, 18} })));
    }
}