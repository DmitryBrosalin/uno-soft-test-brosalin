public class TestCase {
    public static void main(String[] args) {
        long time = System.nanoTime();
        StringGroupFinder stringGroupFinder = new StringGroupFinder();
        stringGroupFinder.readFile("test.txt");
        time = System.nanoTime() - time;
        System.out.println("Программа выполнена за " + time/1000000000 + " секунд");
    }
}
