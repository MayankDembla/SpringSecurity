package com.dembla.security.api.ApiGateway.solution;

// https://leetcode.com/problems/height-checker/
public class OrderCheck {

  public static void main(String[] args) {
    //
      int height[] = {1,1,3,3,4,1} ;

    System.out.println(heightChecker(height));
  }


    public static int heightChecker(int[] heights) {

        int count[] = new int[101];
        int target[] = new int[heights.length];

        // clone array
        for (int i=0; i<heights.length; i++)
            target[i] = heights[i];

        // sort heights using counting sort
        for (int i=0; i<heights.length; i++)
            count[heights[i]]++;

        int j= 0;
        for (int i=0; i<count.length; i++)
            while (count[i] != 0){
                heights[j++] = i;
                count[i]--;
            }

        // Just compare positions
        j = 0;
        for (int i=0; i<target.length; i++){
            if (target[i] != heights[i]) j++;
        }
        return j;
    }
}
