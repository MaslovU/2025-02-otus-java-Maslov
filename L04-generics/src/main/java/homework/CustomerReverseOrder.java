package homework;

import java.util.ArrayDeque;
import java.util.Deque;

public class CustomerReverseOrder {

    // надо подобрать подходящую структуру данных, тогда решение будет в "две строчки"
    Deque<Customer> deque = new ArrayDeque<>();

    public void add(Customer customer) {
        deque.add(customer);
    }

    public Customer take() {
        return deque.pollLast(); // это "заглушка, чтобы скомилировать"
    }
}