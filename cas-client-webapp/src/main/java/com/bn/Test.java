package com.bn;

import java.util.LinkedList;
import java.util.Scanner;

/**
 * Created by kai on 2015/7/22.
 */
public class Test {


    public static void main(String[] args) {
        final LinkedList<String> list = new LinkedList<String>();
        for (int i = 0; i < 1000;i++){
            list.add(i+"");
        }

        for (int i=0;i<5;i++){
            new Thread(new Runnable() {
                public void run() {
                    synchronized (list){
                        while(!list.isEmpty()){

                            System.out.println(Thread.currentThread().getName());
                            list.notify();
                            String item = list.remove();
                            System.out.println(item);
                            try {
                                list.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                }
            }).start();
        }


        Scanner sc = new Scanner(System.in);
        sc.nextLine();
    }
}
