
public class Main2 {
    public static void main(String[] args) {
        Margulan tea = new Tea();
        System.out.println(" Making Tea ");
        tea.prepareRecipe();

        System.out.println("\nMaking Coffee ");
        Margulan coffee = new Coffee();
        coffee.prepareRecipe();

        System.out.println("\nMaking Hot Chocolate ");
        Margulan choco = new HotChocolate();
        choco.prepareRecipe();
    }
}
