import io.javalin.Javalin;

import java.util.ArrayList;
import java.util.HashMap;

public class HelloWorld {
    public static void main(String[] args) {
        Javalin app = Javalin.create().start(8080);
        app.get("/", ctx -> ctx.result("Ich brauch dringend a Glühmopped"));
        app.get("/test", ctx -> ctx.html("<h1> Servus </h1>"));

        app.get("/project1", ctx -> {
            HashMap<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>() {};
            ArrayList<String> y = new ArrayList<>();
            for(int i = 0; i<50; i++){
                y.add(Integer.toString(i));
            }
            map.put("mylist",y);
            ctx.render("/template.html", map);
        });
    }
}