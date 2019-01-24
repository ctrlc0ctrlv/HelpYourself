package com.example.examhelper;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;

public class QueueExample {
    public static void main(String[] args) {
        //пример естественного добавления элементов в приоритетную очередь
        Queue<Integer> integerPriorityQueue = new PriorityQueue<>(7);
        Random rand = new Random();
        for(int i=0; i<10; i++){
            integerPriorityQueue.add(rand.nextInt(100));
            Integer in = integerPriorityQueue.poll();
            System.out.println("Обрабатываем Integer:"+in);
        }
        //Пример PriorityQueue с компаратором
        Queue<Customer> customerPriorityQueue = new PriorityQueue<>(7, idComparator);
        addDataToQueue(customerPriorityQueue);
        pollDataFromQueue(customerPriorityQueue);

    }

    //Анонимный класс компаратора
    private static Comparator<Customer> idComparator = new Comparator<Customer>(){
        @Override
        public int compare(Customer c1, Customer c2) {
            return c1.getId() - c2.getId();
        }
    };

    // служебный метод добавления элементов в очередь
    private static void addDataToQueue(Queue<Customer> customerPriorityQueue) {
        Random rand = new Random();
        for(int i=0; i<10; i++){
            int id = rand.nextInt(100);
            customerPriorityQueue.add(new Customer(id, "Pankaj "+id));
            System.out.println("Добавляем клиента "+id);
        }
    }

    //служебный метод для обработки данных очереди
    private static void pollDataFromQueue(Queue<Customer> customerPriorityQueue) {
        while(true){
            Customer cust = customerPriorityQueue.poll();
            if(cust == null) break;
            System.out.println("Обработка клиента с id=" + cust.getId());
        }
    }

    public static class Customer {
        private int id;
        private String name;

        Customer(int i, String n){
            this.id=i;
            this.name=n;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

    }
}
