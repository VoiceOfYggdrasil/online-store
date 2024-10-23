package com.pluralsight;

import java.io.*;
import java.io.BufferedReader;
import java.io.FileReader;
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
            System.out.println("3. Exit");

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

        System.out.println("Here's a list of all our products: ");

        for (Product product : inventory) {
            System.out.println(product.getId() + " | " + product.getName() + " | " +
                    product.getPrice());
        }

        System.out.print("Want to search for a specific product? Enter the item ID to add it directly to your cart: ");
        String inputId = scanner.nextLine();
        Product product = findProductById(inputId, inventory);

        if (product != null) {
            cart.add(product);
            System.out.println(product.getName() + " added to cart.");
        } else {
            System.out.println("404: product not found.");
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
        totalAmount = 0;
        for (Product product : cart) {
            System.out.println(product.getId() + " | " +
                    product.getName() + " | $" + product.getPrice());
            totalAmount += product.getPrice();
        }
        System.out.printf("Products in cart: $%.2f%n", totalAmount);

        System.out.println("Remove item from cart? Y or N: ");
        String response = scanner.nextLine().trim();

        if (response.equalsIgnoreCase("y")) {
            System.out.println("Please enter ID of item you wish to remove: ");
            String productId = scanner.nextLine().trim();
            Product removeProduct = findProductById(productId, cart);

            if (removeProduct != null) {
                cart.remove(removeProduct);
                totalAmount -= removeProduct.getPrice();
                System.out.println(removeProduct.getName() + " removed from cart.");
                System.out.printf("Products in cart: $%.2f%n", totalAmount);
            } else {
                System.out.println("404: product not found in cart.");
            }
        }
    }

    public static void checkOut(ArrayList<Product> cart, double totalAmount) {
        // This method should calculate the total cost of all items in the cart,
        // and display a summary of the purchase to the user. The method should
        // prompt the user to confirm the purchase, and deduct the total cost
        // from their account if they confirm.

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
}
