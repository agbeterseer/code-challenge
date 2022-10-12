package com.seerbit.assessment.service;

import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class AlgorithmSolutions {


    public int[] printPairs(int[] arr, int sum)
    {

        // for the purpose this test I have the printPairs method return int[]
        // with this I want to achieve having out put the pairs that make up the result.
        // assuming I am to return an empty array if no pair is found

        HashSet<Integer> s = new HashSet<>();
        for (int j : arr) {
            int temp = sum - j;

            // checking for condition
            if (s.contains(temp)) {
                return new int[]{j, temp};
            }
            s.add(j);
        }
        return new int[]{};
    }



    /**
     * Binary Search algorithm will be used
     */
    public int[] findLowHigh(List<Integer> array, int key){

        int low = findLowIndex(array, key);
        int high = findHighIndex(array, key);

        return new int[]{low,high};
    }

    private int findLowIndex(List<Integer> arr, int key) {
        int low = 0;
        int high = arr.size() - 1;
        int mid = high / 2;

        while (low <= high) {
            int mid_elem = arr.get(mid);
            if (mid_elem < key) {
                low = mid + 1;
            }
            else {
                high = mid - 1;
            }
            mid = low + (high - low) / 2;
        }
        if (low < arr.size() && arr.get(low) == key) {
            return low;
        }

        return -1;
    }

    private int findHighIndex(List<Integer> arr, int key) {
        int low = 0;
        int high = arr.size() - 1;
        int mid = high / 2;

        while (low <= high) {
            int mid_elem = arr.get(mid);
            if (mid_elem <= key) {
                low = mid + 1;
            }
            else {
                high = mid - 1;
            }
            mid = low + (high - low) / 2;
        }
        if(high == -1){
            return high;
        }
        if (arr.get(high) == key) {
            return high;
        }

        return -1;
    }


    public String mergeIntervals(List<Pair> v) {
        if(v == null || v.size() == 0) {
            return null;
        }

        ArrayList<Pair> result = new ArrayList<>();
        result.add(new Pair(v.get(0).first, v.get(0).second));

        for(int i = 1 ; i < v.size(); i++) {
            int x1 = v.get(i).first;
            int y1 = v.get(i).second;
            int x2 = result.get(result.size() - 1).first;
            int y2 = result.get(result.size() - 1).second;

            if(y2 >= x1) {
                result.get(result.size() - 1).second = Math.max(y1, y2);
            } else {
                result.add(new Pair(x1, y1));
            }
        }

        // Assuming I am to format the response
        StringBuilder output= new StringBuilder();
        for (Pair pair : result) {
            output.append(String.format("[%d, %d] ", pair.first, pair.second));
        }
        return output.toString();
    }

    // Another approach to mergeIntervals.. here arrays can be sorted ie. in a case where u have unsorted array
    public int[][] mergeInterval2(int[][] intervals) {
        if (intervals.length <= 1)
            return intervals;

        // Sort by ascending starting point
        Arrays.sort(intervals, Comparator.comparingInt(i -> i[0]));
        List<int[]> result = new ArrayList<>();
        int[] newInterval = intervals[0];
        result.add(newInterval);
        for (int[] interval : intervals) {
            if (interval[0] <= newInterval[1]) // Overlapping intervals, move the end if needed
                newInterval[1] = Math.max(newInterval[1], interval[1]);
            else {                             // Disjoint intervals, add the new interval to the list
                newInterval = interval;
                result.add(newInterval);
            }
        }

        return result.toArray(new int[result.size()][]);
    }

}

class Pair{
    public int first;
    public int second;

    public Pair(int x, int y){
        this.first = x;
        this.second = y;
    }
}
