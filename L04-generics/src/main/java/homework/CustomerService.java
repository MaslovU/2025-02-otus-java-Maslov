package homework;

import java.util.*;

public class CustomerService {

    private NavigableMap<Customer, String> treeMap = new TreeMap<>(Comparator.comparing(Customer::getScores));

    public Map.Entry<Customer, String> getSmallest() {
        // Возможно, чтобы реализовать этот метод, потребуется посмотреть как Map.Entry сделан в jdk
        var customerEntry = treeMap.firstEntry();
        var customer = customerEntry.getKey();
        return getNewCustomer(customer, customerEntry);
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        for (Map.Entry<Customer, String> entry: treeMap.entrySet()) {
            Customer customerKey = entry.getKey();
            if (customerKey.getScores() > customer.getScores()) {
                return getNewCustomer(customerKey, entry);
            }
        }
        return null;
    }

    public void add(Customer customer, String data) {
        treeMap.put(customer, data);
    }

    private Map.Entry<Customer, String> getNewCustomer(Customer customer, Map.Entry<Customer, String> entry) {
        var newCustomer = new Customer(customer.getId(), customer.getName(), customer.getScores());
        return new AbstractMap.SimpleEntry<>(newCustomer, entry.getValue());
    }
}
