package com.pluralsight;

import java.io.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;

public class Store {

    public static void main(String[] args) {
        // Initialize variables
        ArrayList<Product> inventory = new ArrayList<Product>();
        ArrayList<Product> cart = new ArrayList<Product>();
        double totalAmount = 0.0;

        // Load inventory from CSV file
        loadInventory("products.csv", inventory);

        // Create scanner to read user input
        Scanner scanner = new Scanner(System.in);
        int choice = -1;

        // Display menu and get user choice until they choose to exit
        while (choice != 3) {
            System.out.println("Welcome to the Online com.pluralsight.Store!");
            System.out.println("1. Show Products");
            System.out.println("2. Show Cart");
            System.out.println("3. Check Out");
            System.out.println("4. Exit");

            choice = scanner.nextInt();
            scanner.nextLine();

            // Call the appropriate method based on user choice
            switch (choice) {
                case 1:
                    displayProducts(inventory, cart, scanner);
                    break;
                case 2:
                    displayCart(cart, scanner, totalAmount);
                    break;
                case 3:
                    checkOut(cart, scanner, totalAmount);
                    break;
                case 4:
                    System.out.println("Thank you for shopping with us!");
                    break;
                default:
                    System.out.println("Invalid choice!");
                    break;
            }
        }
    }

    public static void loadInventory(String fileName, ArrayList<Product> inventory) {
        // This method should read a CSV file with product information and
        // populate the inventory ArrayList with com.pluralsight.Product objects. Each line
        // of the CSV file contains product information in the following format:
        //
        // id,name,price
        //
        // where id is a unique string identifier, name is the product name,
        // price is a double value representing the price of the product

        try {
            BufferedReader buffReader = new BufferedReader(new FileReader(fileName));
            String input;
            while ((input = buffReader.readLine()) != null) {
            String[] strings = input.split("\\|");
            String id = strings[0];
            String name = strings[1];
            double price = Double.parseDouble(strings[2]);
            inventory.add(new Product(id, name, price));
            }
            buffReader.close();
        } catch (IOException e) {
            System.out.println("ERROR: file read error.");
        }
    }

    public static void displayProducts(ArrayList<Product> inventory, ArrayList<Product> cart, Scanner scanner) {
        // This method should display a list of products from the inventory,
        // and prompt the user to add items to their cart. The method should
        // prompt the user to enter the ID of the product they want to add to
        // their cart. The method should
        // add the selected product to the cart ArrayList.

        System.out.println("\nPRODUCT MENU:\n");

        for (Product product : inventory) {
            System.out.println(product.getId() + " | " + product.getName() + " | " +
                    product.getPrice());
        }

        boolean display = true;
        while (display) {
            System.out.println("\n1. Search Menu");
            System.out.println("2. Add Item To Cart");
            System.out.println("3. Go Back");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    searchProduct(scanner, inventory);
                    break;

                case "2":
                    System.out.print("Enter item ID to add to cart: ");
                    String addItemId = scanner.nextLine();

                    for (Product product : inventory) {
                        if (product.getId().equalsIgnoreCase(addItemId)) {
                            cart.add(product);
                        }
                    }
                    break;

                case "3":
                    display = false;
                    break;

                default:
                    System.out.println("Invalid choice!");
                    break;
            }
        }
    }

    public static void searchProduct(Scanner scanner, ArrayList<Product> inventory) {

        System.out.println("\nSEARCH MENU:\n");

        boolean display = true;
        while (display) {

            System.out.println("1. Search by ID");
            System.out.println("2. Search by Name");
            System.out.println("3. Go Back");
            String choice = scanner.nextLine();

            switch (choice) {

                case "1":
                    System.out.print("Enter the ID of the item you want: ");
                    String searchId = scanner.nextLine();

                    System.out.println(findProductById(searchId, inventory));
                    break;

                case "2":
                    System.out.print("Enter the name of the item you want: ");
                    String searchName = scanner.nextLine();

                    System.out.println(findProductByName(searchName, inventory));
                    break;

                case "3":
                    display = false;
                    break;

                default:
                    System.out.println("Invalid choice!");
                    break;
            }
        }
    }

    public static void displayCart(ArrayList<Product> cart, Scanner scanner, double totalAmount) {
        // This method should display the items in the cart ArrayList, along
        // with the total cost of all items in the cart. The method should
        // prompt the user to remove items from their cart by entering the ID
        // of the product they want to remove. The method should update the cart ArrayList and totalAmount
        // variable accordingly.

        if (cart.isEmpty()) {
            System.out.println("cart is empty.");
            return;
        }

        System.out.println("Your Cart: ");
        cartDisplayScreen(cart);

        totalAmount = 0;
        for (Product product : cart) {
            System.out.println(product.getId() + " | " +
                    product.getName() + " | $" + product.getPrice());
            totalAmount += product.getPrice();
        }
        System.out.printf("\nCart Subtotal: $%.2f%n", totalAmount);

        System.out.println("\nRemove item from cart? Y or N: ");
        String response = scanner.nextLine().trim();

        if (response.equalsIgnoreCase("y")) {
            System.out.println("Please enter ID of item you wish to remove: ");
            String productId = scanner.nextLine().trim();
            Product removeProduct = findProductById(productId, cart);

            if (removeProduct != null) {
                cart.remove(removeProduct);
                totalAmount -= removeProduct.getPrice();
                System.out.println(removeProduct.getName() + " removed from cart.");
                System.out.printf("\nCart Subtotal: $%.2f%n", totalAmount);
            } else {
                System.out.println("404: product not found in cart.");
            }
        }

        if (response.equalsIgnoreCase("n")) {
            return;
        }
    }

    public static void cartDisplayScreen(ArrayList<Product> cart) {
        for (Product product : cart) {
            System.out.println(product);
        }
    }

    public static double cartTotalCost(ArrayList<Product> cart) {
        double totalCost = 0;

        for (Product product : cart) {
            totalCost += product.getPrice();
        }

        return totalCost;
    }

    public static void checkOut(ArrayList<Product> cart, Scanner scanner, double totalAmount) {
        // This method should calculate the total cost of all items in the cart,
        // and display a summary of the purchase to the user. The method should
        // prompt the user to confirm the purchase, and deduct the total cost
        // from their account if they confirm.
        StringBuilder stringBuilder = new StringBuilder();
        LocalDateTime localDateTime = LocalDateTime.now();

        System.out.println("Cart Contents: \n");

        totalAmount = 0;
        for (Product product : cart) {
            System.out.println(product.getId() + " | " +
                    product.getName() + " | $" + product.getPrice());
            totalAmount += product.getPrice();
        }
        System.out.println("Cart Total: $" + totalAmount);

        System.out.print("Confirm Purchase? Y or N: ");
        String input = scanner.nextLine();

        if (input.equalsIgnoreCase("y")) {
            System.out.println("Please present money.");
            double payment = scanner.nextDouble();
            scanner.nextLine();

            for (Product product : cart) {
                System.out.println(product.getId() + " | " +
                        product.getName() + " | $" + product.getPrice());
                totalAmount += product.getPrice();
            }
            double totalDue = cartTotalCost(cart);

            if (totalDue <= payment) {
                System.out.println("Receipt: ");
                System.out.println(localDateTime.toLocalDate());
                for (Product product : cart) {
                    System.out.println(product.getId() + " | " +
                            product.getName() + " | $" + product.getPrice());
                    totalAmount += product.getPrice();
                }
                System.out.println("Change Received: $" + (payment - totalDue));
                System.out.println("Total: " + cartTotalCost(cart));
                System.out.println("Amount Paid: " + payment);

                cart.clear();
            } else {
                System.out.println("Insufficient Funds");
            }
        } else if (input.equalsIgnoreCase("n")) {
            return;
        } else {
            System.out.println("Invalid Selection");
        }
    }

    public static Product findProductById(String id, ArrayList<Product> inventory) {
        // This method should search the inventory ArrayList for a product with
        // the specified ID, and return the corresponding com.pluralsight.Product object. If
        // no product with the specified ID is found, the method should return
        // null.

        for (Product product : inventory) {
            if (product.getId().equalsIgnoreCase(id.trim())) {
                return product;
            }
        }
        return null;
    }

    public static Product findProductByName(String name, ArrayList<Product> inventory) {


        for (Product product : inventory) {
            if (product.getName().equalsIgnoreCase(name.trim())) {
                return product;
            }
        }
        return null;
    }
}
