
import java.io.*;
import java.util.*;

class Feedback {
    int id;
    String customerName;
    String message;
    int rating;

    Feedback(int id, String customerName, String message, int rating) {
        this.id = id;
        this.customerName = customerName;
        this.message = message;
        this.rating = rating;
    }

    void display() {
        System.out.println("\nFeedback ID: " + id);
        System.out.println("Customer: " + customerName);
        System.out.println("Message: " + message);
        System.out.println("Rating: " + rating + "/5");
    }

    String toFileFormat() {
        return id + "|" + customerName + "|" + message + "|" + rating;
    }
}

public class Main {
    static ArrayList<Feedback> feedbackList = new ArrayList<>();
    static Scanner sc = new Scanner(System.in);
    static final String FILE_NAME = "feedback.txt";

    static void loadFeedback() {
        File file = new File(FILE_NAME);

        if (!file.exists()) {
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] data = line.split("\\|", 4);

                if (data.length == 4) {
                    try {
                        int id = Integer.parseInt(data[0]);
                        int rating = Integer.parseInt(data[3]);

                        if (rating >= 1 && rating <= 5) {
                            feedbackList.add(
                                new Feedback(id, data[1], data[2], rating)
                            );
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Skipping invalid record.");
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading feedback.");
        }
    }

    static void saveFeedback() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (Feedback feedback : feedbackList) {
                writer.println(feedback.toFileFormat());
            }
        } catch (IOException e) {
            System.out.println("Error saving feedback.");
        }
    }

    static int generateId() {
        int maxId = 1000;

        for (Feedback feedback : feedbackList) {
            if (feedback.id > maxId) {
                maxId = feedback.id;
            }
        }

        return maxId + 1;
    }

    static void addFeedback() {
        int id = generateId();

        System.out.print("Enter Customer Name: ");
        String name = sc.nextLine().trim();

        System.out.print("Enter Feedback Message: ");
        String message = sc.nextLine().trim();

        if (name.isEmpty() || message.isEmpty()
                || name.contains("|") || message.contains("|")) {
            System.out.println("Invalid name or message!");
            return;
        }

        System.out.print("Enter Rating (1-5): ");

        int rating;
        try {
            rating = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number.");
            return;
        }

        if (rating < 1 || rating > 5) {
            System.out.println("Rating must be between 1 and 5.");
            return;
        }

        feedbackList.add(new Feedback(id, name, message, rating));
        saveFeedback();

        System.out.println("Feedback submitted successfully!");
        System.out.println("Your Feedback ID: " + id);
    }

    static void viewFeedback() {
        if (feedbackList.isEmpty()) {
            System.out.println("No feedback available.");
            return;
        }

        for (Feedback feedback : feedbackList) {
            feedback.display();
        }
    }

    static void searchFeedback() {
        System.out.print("Enter Customer Name: ");
        String name = sc.nextLine().trim();

        boolean found = false;

        for (Feedback feedback : feedbackList) {
            if (feedback.customerName.equalsIgnoreCase(name)) {
                feedback.display();
                found = true;
            }
        }

        if (!found) {
            System.out.println("No feedback found for this customer.");
        }
    }

    static void showAverageRating() {
        if (feedbackList.isEmpty()) {
            System.out.println("No ratings available.");
            return;
        }

        int total = 0;

        for (Feedback feedback : feedbackList) {
            total += feedback.rating;
        }

        double average = (double) total / feedbackList.size();

        System.out.printf("Average Rating: %.2f/5%n", average);
    }

    static void showFeedbackByRating() {
        System.out.println("\n1. Positive Feedback (4-5)");
        System.out.println("2. Negative Feedback (1-2)");
        System.out.print("Enter Choice: ");

        String choice = sc.nextLine();

        boolean found = false;

        for (Feedback feedback : feedbackList) {
            if ((choice.equals("1") && feedback.rating >= 4)
                    || (choice.equals("2") && feedback.rating <= 2)) {
                feedback.display();
                found = true;
            }
        }

        if (!found) {
            System.out.println("No matching feedback found.");
        }
    }

    static void deleteFeedback() {
        System.out.print("Enter Feedback ID to delete: ");

        int id;
        try {
            id = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID.");
            return;
        }

        for (int i = 0; i < feedbackList.size(); i++) {
            if (feedbackList.get(i).id == id) {
                feedbackList.remove(i);
                saveFeedback();
                System.out.println("Feedback deleted successfully!");
                return;
            }
        }

        System.out.println("Feedback ID not found.");
    }

    public static void main(String[] args) {
        loadFeedback();

        while (true) {
            System.out.println("\n===== HOTEL FEEDBACK SYSTEM =====");
            System.out.println("1. Submit Feedback");
            System.out.println("2. View All Feedback");
            System.out.println("3. Search by Customer");
            System.out.println("4. Average Rating");
            System.out.println("5. Positive / Negative Feedback");
            System.out.println("6. Delete Feedback");
            System.out.println("7. Exit");

            System.out.print("Enter Choice: ");
            String choice = sc.nextLine();

            switch (choice) {
                case "1":
                    addFeedback();
                    break;
                case "2":
                    viewFeedback();
                    break;
                case "3":
                    searchFeedback();
                    break;
                case "4":
                    showAverageRating();
                    break;
                case "5":
                    showFeedbackByRating();
                    break;
                case "6":
                    deleteFeedback();
                    break;
                case "7":
                    System.out.println("Thank you!");
                    return;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }
}
