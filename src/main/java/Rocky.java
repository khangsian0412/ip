/**
 * A chatbot that displays a greeting and then exits.
 */
public class Rocky {
    /**
     * Starts the chatbot and prints its greeting and farewell messages.
     *
     * @param args command-line arguments, which this program does not use
     */
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
        System.out.println("Bye. We meet again soon!");
        System.out.println(divider);
    }
}
