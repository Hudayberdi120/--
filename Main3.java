public class Main3 {
    public static void main(String[] args) {
        ChatMediator chat = new ChatMediator();

        User alice = new User(chat, "Alice");
        User bob = new User(chat, "Bob");
        User charlie = new User(chat, "Charlie");

        chat.registerColleague(alice);
        chat.registerColleague(bob);
        chat.registerColleague(charlie);

        alice.send("Hi everyone!");
        bob.send("Hello Alice!");
        charlie.send("Hey folks!");
    }
}
