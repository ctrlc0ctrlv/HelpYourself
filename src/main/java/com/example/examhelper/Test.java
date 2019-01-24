package com.example.examhelper;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class Test {
    public static void main(String args[]) {
        String months[] = {"Янв", "Фев", "Мар", "Апр", "Май", "Июн", "Июл", "Авг", "Сен",
                "Окт", "Ноя", "Дек"};

        int year;
        // Создание григорианского календаря инициализированного
        // текущей датой и временем в
        // локализации и часовом поясе по умолчанию.

        GregorianCalendar gcalendar = new GregorianCalendar();

        // Вывод реального времени и даты
        System.out.print("Дата: ");
        System.out.print(months[gcalendar.get(Calendar.MONTH)]);
        System.out.print(" " + gcalendar.get(Calendar.DATE) + " ");
        System.out.println(year = gcalendar.get(Calendar.YEAR));
        System.out.print("Время: ");
        System.out.print(gcalendar.get(Calendar.HOUR) + ":");
        System.out.print(gcalendar.get(Calendar.MINUTE) + ":");
        System.out.println(gcalendar.get(Calendar.SECOND));

        // Тест если текущий год является високосным
        if(gcalendar.isLeapYear(year)) {
            System.out.println(year + " - високосный год");
        }else {
            System.out.println(year + " - не високосный год");
        }
    }
}
