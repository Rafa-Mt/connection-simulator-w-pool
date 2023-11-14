public class App {
    public static void main(String[] args) {
        PropertiesHandler props = new PropertiesHandler("config");
        int iterations = Integer.parseInt(props.get("iterations"));
        int jump = Integer.parseInt(props.get("jump"));

        Controller controller = new Controller(iterations, jump);
        controller.start();
    }
}
