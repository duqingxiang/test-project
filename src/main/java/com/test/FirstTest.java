package com.test;

import java.math.BigDecimal;

public class FirstTest {

    public static void main(String[] args) {

        BigDecimal amount = new BigDecimal("27");


        for (int i = 0; i < 100; i++) {
            double dd = Math.pow(i, 3);
            BigDecimal ab = new BigDecimal(dd);
            System.out.println(Math.pow(ab.doubleValue(), 1.0 / 3));
            //java自带幂函数，注意开方时是小数。
            System.out.println(pow(ab.doubleValue(), 3));
            System.out.println("========================");
        }



    }


    static double gety(double x, double m, int n)
    //求函数值 例如  求8开立方，就是 y=x*x*x-8, m为要开立方的值即8，n表示求立方根即3，
    {
        double v = x;//1
        for (int i = 1; i < n; i++)//2
            v *= x;
        return v - m;//3
    }

    static double getdy(double x, int n) {
        //求函数的导数值例如： y=x*x*x-8 就是  dy=2*x*x
        double v = n;//4
        for (int i = 1; i < n; i++)//5
            v *= x;//6
        return v;//7
    }

    static double pow(double m, int n) {
        //求对m开n次方，其中n需要大于2。
        double x = m;
        while (Math.abs(gety(x, m, n) / getdy(x, n)) > 0.001)//8
            x -= gety(x, m, n) / getdy(x, n);//9
        return x - gety(x, m, n) / getdy(x, n);//10
    }
}



