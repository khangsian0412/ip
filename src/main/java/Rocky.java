import java.util.Objects;
import java.util.Scanner;
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
        while (scanner.hasNextLine()) {
            String userInput = scanner.nextLine();
            if (Objects.equals(userInput, "bye")) {
                break;
            }
            System.out.println(divider);
            System.out.println(userInput + ", but what it mean?");
            System.out.println(divider);
        }
        System.out.println("Bye. We meet again soon!");
        System.out.println(divider);
    }
}
