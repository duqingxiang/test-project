package com.test;

public class SelectSortTest {

    public static void main(String[] args) {
        int[] array = {7, 3, 5, 1, 2, 9, 4};

        selectSort(array);

        for (int i=0;i<array.length;i++) {
            System.out.print(array[i]+" ");
        }

        System.out.println("");
    }


    public static void selectSort(int[] array) {


        int temp;
        for (int i = 0; i < array.length; i++) {

            int index = i;
            for (int j = array.length -1; j > i; j--) {
                if (array[j] < array[index]) {
                    index = j;
                }
            }

            temp = array[i];
            array[i] = array[index];
            array[index] = temp;
        }
    }
}
