package com.dgnt.pro.quickTeamPicker.util;

/**
 * Created by Andrew on 10/7/2015.
 * Code from https://gist.github.com/ishikawa/21680
 */
public class PartitionUtil {

    /*
     * Dynamic Programming
     */
    public static int[] solveDynamicProgrammatically(int[] books, int k) {
        assert k > 0 && books.length >= k;

        // prefix sums: sum[k] = books[i..k]
        final int[] sum = new int[books.length];

        sum[0] = books[0];
        for (int i = 1; i < books.length; i++) sum[i] = sum[i-1] + books[i];

        // M[n<=length][m<=k], D[n<=length][m<=k]
        final int[][] M = new int[books.length+1][k+1];
        final int[][] D = new int[books.length+1][k+1];

        for (int n = 1; n <= books.length; n++) M[n][1] = sum[n-1];
        for (int m = 1; m <= k; m++) M[1][m] = books[0];

        for (int n = 2; n <= books.length; n++) {
            for (int m = 2; m <= k; m++) {
                M[n][m] = Integer.MAX_VALUE;
                for (int x = 1; x < n; x++) {
                    final int largest = Math.max(M[x][m-1], sum[n-1]-sum[x-1]);

                    if (largest < M[n][m]) {
                        M[n][m] = largest;
                        D[n][m] = x;
                    }
                }
            }
        }

        int[] dividers = new int[k-1];
        for (int m = k, n = books.length; m > 1; m--)
            n = dividers[m - 2] = D[n][m];
        return dividers;
    }

    /*
     * Recursive
     */
    private static int sum(int[] x, int begin, int end) {
        int sum = 0;
        for (int i = begin; i < end; i++) sum += x[i];
        return sum;
    }

    private static int minimumPossibleLength(int[] books, int n, int k) {
        if (k == 1) return sum(books, 0, n);
        if (n == 1) return books[0];
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < n; i++) {
            int a = minimumPossibleLength(books, i + 1, k - 1);
            int b = sum(books, i + 1, n);
            min = Math.min(min, Math.max(a, b));
        }
        return min;
    }

    public static int[] solveRecursively(int[] books, int k) {
        assert k > 0;
        assert books.length >= k;

        int min = minimumPossibleLength(books, books.length, k);
        int[] dividers = new int[k - 1];
        int sum = 0, d = 0;
        //System.out.printf("%s, %d => %d\n", Arrays.toString(books), k, min);
        for (int i = 0; i < books.length; i++) {
            if (sum + books[i] > min) {
                dividers[d++] = i;
                sum = books[i];
            } else {
                sum += books[i];
            }
        }
        assert d == dividers.length;
        return dividers;
    }
}
