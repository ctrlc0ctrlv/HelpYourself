package com.example.examhelper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Hash_randoms {
    public static void main (String[]args){
        Scanner in = new Scanner(System.in);
        int num = in.nextInt();

        String[] inputs = new String[num];

        in = new Scanner(System.in);
        for (int i = 0; i < num; i++) {
            inputs[i] = in.nextLine();
        }

        StringBuilder st;
        Set<String> sp1 = new HashSet<>();

        for (String input : inputs) {
            st = new StringBuilder();
            for (int x = 0; x < 7; x++) {
                if (input.charAt(x) == 'a' | input.charAt(x) == 'b' | input.charAt(x) == 'c') {
                    st.append("2");
                } else if (input.charAt(x) == 'd' | input.charAt(x) == 'e' | input.charAt(x) == 'f') {
                    st.append("3");
                } else if (input.charAt(x) == 'g' | input.charAt(x) == 'h' | input.charAt(x) == 'i') {
                    st.append("4");
                } else if (input.charAt(x) == 'j' | input.charAt(x) == 'k' | input.charAt(x) == 'l') {
                    st.append("5");
                } else if (input.charAt(x) == 'm' | input.charAt(x) == 'n' | input.charAt(x) == 'o') {
                    st.append("6");
                } else if (input.charAt(x) == 'p' | input.charAt(x) == 'q' | input.charAt(x) == 'r' | input.charAt(x) == 's') {
                    st.append("7");
                } else if (input.charAt(x) == 't' | input.charAt(x) == 'u' | input.charAt(x) == 'v') {
                    st.append("8");
                } else if (input.charAt(x) == 'w' | input.charAt(x) == 'x' | input.charAt(x) == 'y' | input.charAt(x) == 'z') {
                    st.append("9");
                }
            }
            sp1.add(st.toString());
        }

        List qwerty = new ArrayList<>(sp1);
        System.out.println(qwerty.size());
    }
}
