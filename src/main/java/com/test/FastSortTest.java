package com.test;

public class FastSortTest {

    public static void main(String[] args) {

        int[] array = {7, 3, 5, 1, 2, 9, 4};

        quickSort(array, 0, array.length - 1);

        for (int i=0;i<array.length;i++) {
            System.out.print(array[i]+" ");
        }

        System.out.println("");

    }

    public static void quickSort(int[] a, int start, int end) {
        if (start >= end)
            return;
        int i = start;
        int j = end;
        int base = a[start];
        while (i != j) {
            while (a[j] >= base && j > i)
                j--;
            while (a[i] <= base && i < j)
                i++;
            if (i < j) {
                int temp = a[i];
                a[i] = a[j];
                a[j] = temp;
            }
        }
        a[start] = a[i];
        a[i] = base;
        quickSort(a, start, i - 1);
        quickSort(a, i + 1, end);
    }
}
