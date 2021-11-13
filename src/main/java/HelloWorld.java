import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class HelloWorld {
    static String url = "jdbc:mysql://sql11.freesqldatabase.com:3306/sql11450649";
    static String user = "sql11450649";
    static String password = "b8XJHtWxnq";
    static Statement stmt = null;

    public static void main(String[] args) {
        // DATENBANK CONNECTION
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, user,password);
            System.out.println("Connection Succsessful");
            stmt = conn.createStatement();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        // ROUTES
        Javalin app = Javalin.create(config -> {
            config.addStaticFiles("/public", Location.CLASSPATH);
        }).start(8080);
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
        app.post("/changelog", ctx -> {
            ctx.html("Success");
            stmt.executeUpdate("INSERT INTO CHANGE_LOG VALUES (0, 'TEST', 'TESTCON', 'NM')");
        });
    }
}

