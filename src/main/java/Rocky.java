import java.util.Objects;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
public class Rocky {
    public static void main(String[] args) {
        String banner = " ____             _          \n"
                + "|  _ \\ ___   ___| | ___   _ \n"
                + "| |_) / _ \\ / __| |/ / | | |\n"
                + "|  _ < (_) | (__|   <| |_| |\n"
                + "|_| \\_\\___/ \\___|_|\\_\\__, |\n"
                + "                         |___/\n";
        String divider = "____________________________________________________________";

        System.out.println(divider);
        System.out.println(banner);
        System.out.println("Hello! I Rocky.");
        System.out.println("Amaze, what a special human being! What rocky do for you?");
        System.out.println(divider);
        Scanner scanner = new Scanner(System.in);
        List<String> statements = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String userInput = scanner.nextLine().trim();
            if (Objects.equals(userInput, "bye")) {
                break;
            }
            if (Objects.equals(userInput, "list")) {
                System.out.println(divider);
                if (statements.isEmpty()){
                    System.out.println("Rocky don't see anything!");
                }
                else {
                    for (int i = 0; i < statements.size(); i++) {
                        System.out.println((i + 1) + ". " + statements.get(i));
                    }
                    System.out.println(divider);
                    continue;
                }
            }
            System.out.println(divider);
            System.out.println("added: " + userInput + ", but what it mean?");
            statements.add(userInput);
            System.out.println(divider);
        }
        System.out.println("Bye. We meet again soon!");
        System.out.println(divider);
    }
}
