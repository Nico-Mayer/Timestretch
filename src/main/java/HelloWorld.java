import io.javalin.Javalin;

public class HelloWorld {
    public static void main(String[] args) {
        Javalin app = Javalin.create().start(8080);
        app.get("/", ctx -> ctx.result("Ich brauch dringend a Glühmopped"));
        app.get("/test", ctx -> ctx.html("<h1> Servus </h1>"));
    }
}