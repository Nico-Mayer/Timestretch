import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

public class HelloWorld {
    static String url = "jdbc:mysql://sql11.freesqldatabase.com:3306/sql11450649";
    static String user = "sql11450649";
    static String password = "b8XJHtWxnq";
    static Statement stmt = null;
    static Connection conn = null;

    public static void main(String[] args) {
        // DATENBANK CONNECTION
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url, user,password);
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
        }).start(8000);

        app.post("/changelog", ctx -> {
            String name = ctx.formParam("feature-name");
            String descri = ctx.formParam("feature-description");
            String kuerzel = ctx.formParam("kuerzel");
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            String currentDate = dateFormat.format(date);
            PreparedStatement psmt = conn.prepareStatement("INSERT INTO `CHANGE_LOG` VALUES(0,?,?,?,?)");
            psmt.setString(1, name);
            psmt.setString(2, descri);
            psmt.setString(3, kuerzel);
            psmt.setString(4, currentDate);
            psmt.executeUpdate();
            System.out.println("Added Entry to Database");
            ctx.redirect("/changeLogList");
        });
        app.get("/changeLogList", ctx ->{
            String sql = "SELECT * FROM CHANGE_LOG";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            HashMap<String, ArrayList<ChangeEntry>> Map = new HashMap<>() {};
            ArrayList<ChangeEntry> changeEntrys = new ArrayList<ChangeEntry>();

            while(rs.next())
            {
                changeEntrys.add(new ChangeEntry(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5)));
            }
            Map.put("mylist", changeEntrys);
            ctx.render("/public/changeLogList.html",Map);
        });
    }
}


