package com.nirakar.example.transaction.dto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public record User(
    int id,
    String name,
    int age,
    String gender,
    String phoneNumber,
    String emailId,
    List<Address> address
){
    public static List<User> getAll() {
        List<User> employees = new ArrayList<>();

        employees.add(new User(1, "John Smith", 30, "Male", "9078123456", "john@example.com",
                Arrays.asList(new Address("123 Main St", "City1", 12345))));
        employees.add(new User(2, "Jane Doe", 25, "Female", "9087654321", "jane.doe@example.com",
                Arrays.asList(new Address("456 Oak Ave", "City2", 67890))));
        employees.add(new User(3, "Michael Johnson", 35, "Male", "9870125436", "michael.johnson@example.com",
                Arrays.asList(new Address("789 Elm Rd", "City3", 24680))));
        employees.add(new User(4, "Emily Williams", 28, "Female", "9870124436", "emily.william@test.com",
                Arrays.asList(new Address("987 Pine Ln", "City4", 13579))));
        employees.add(new User(5, "David Jones", 32, "Male", "9870121136", "david.jones@test.com",
                Arrays.asList(new Address("654 Cedar St", "City5", 97531))));
        employees.add(new User(6, "Sarah Johnson", 29, "Female", "9870125426", "sarah.johnson@test.com",
                Arrays.asList(new Address("321 Maple Ave", "City6", 46803))));
        employees.add(new User(7, "Christopher Lee", 31, "Male", "9870125432", "christopher.lee@test.com",
                Arrays.asList(new Address("789 Oak Rd", "City7", 24680))));
        employees.add(new User(8, "Olivia Wilson", 27, "Female", "9870125422", "olivia.wilson@test.com",
                Arrays.asList(new Address("456 Elm St", "City8", 68024))));
        employees.add(new User(9, "Andrew Thompson", 33, "Male", "9870125433", "andrew.thompson@test.com",
                Arrays.asList(new Address("123 Pine Ave", "City9", 40682))));
        employees.add(new User(10, "Emma Davis", 26, "Female", "9870125126", "emma.davis@test.com",
                Arrays.asList(new Address("987 Cedar Rd", "City10", 82460)))
        );
        return employees;
    }

}
