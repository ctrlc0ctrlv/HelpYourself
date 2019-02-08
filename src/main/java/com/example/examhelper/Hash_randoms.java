package com.example.examhelper;

import java.util.Random;

public class Hash_randoms {
    private static Integer[] mistakes = {1,2,3,4,5};

    public static void main (String[]args){
        for (int i = 0; i<20;i++){
            System.out.println(getHashNum());
            if (i==9){
                mistakes = new Integer[0];
                System.out.println("======");
            }
        }
    }

    private static int getHashNum (){
        //С функцией рандома нужно категорически поколдовать, потому что какой-то он не шибко рандомный
        int x;
        Integer[] tasks = {84, 5, 3, 4, 1, 91, 88, 92, 6, 93, 87, 86, 95, 89, 85, 94, 90, 2};

        Random random = new Random();
        boolean not_mistakes = random.nextBoolean();

        if (mistakes.length>0){
            if (not_mistakes){
                x = tasks[random.nextInt(tasks.length)];
            }else{
                x = mistakes[random.nextInt(mistakes.length)];
            }
        }else{
            x = tasks[random.nextInt(tasks.length)];
        }
        return x;
    }
}
