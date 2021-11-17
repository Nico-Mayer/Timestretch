import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

public class App {
    static Connection connection = null;
    static Statement statement = null;
    static PreparedStatement preparedStatement = null;
    static ResultSet resultSet = null;

    public static void main(String[] args) {
        // ROUTES
        Javalin app = Javalin.create(config -> {
            config.addStaticFiles("/public", Location.CLASSPATH);
        }).start(7080);

        // Changelog eintrag zur DB hinzufuegen
        app.post("/changelogPost", ctx -> {
            openDBConnection();
            String name = ctx.formParam("feature-name");
            String descri = ctx.formParam("feature-description");
            String kuerzel = ctx.formParam("kuerzel");
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            String currentDate = dateFormat.format(date);
            preparedStatement = connection.prepareStatement("INSERT INTO `CHANGE_LOG` VALUES(0,?,?,?,?)");
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, descri);
            preparedStatement.setString(3, kuerzel);
            preparedStatement.setString(4, currentDate);
            preparedStatement.executeUpdate();
            System.out.println("Added Changlog Entry to Database");
            closeDBSession();
            ctx.redirect("/changeLogList");
        });

        // Rendern des Changelog
        app.get("/changeLogList", ctx ->{
            openDBConnection();
            String sql = "SELECT * FROM CHANGE_LOG ORDER BY CHANGE_ID DESC";
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            HashMap<String, ArrayList<ChangeEntry>> Map = new HashMap<>();
            ArrayList<ChangeEntry> changelogEntrys = new ArrayList<ChangeEntry>();

            while(resultSet.next())
            {
                changelogEntrys.add(new ChangeEntry(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4), resultSet.getString(5)));
            }
            Map.put("changelogEntrys", changelogEntrys);
            closeDBSession();
            ctx.render("/public/changelog.html",Map);
        });

        // Rendern einer Projekt Html seite mit Dynamischen werten
        app.get("/project", ctx ->{
            openDBConnection();
            String sql = "SELECT * FROM TASKS ORDER BY TASK_ID";
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);

            HashMap<String, ArrayList<Task>> Map = new HashMap<>();
            ArrayList<Task> taskEntrys = new ArrayList<>();

            while (resultSet.next()){
                taskEntrys.add(new Task(resultSet.getInt(1), resultSet.getInt(2), resultSet.getString(3),resultSet.getString(4), resultSet.getBoolean(5)));
            }
            Map.put("taskEntrys", taskEntrys);
            closeDBSession();
           ctx.render("/public/project.html", Map);
        });

        // Neuen Task zu der DB Hinzufuegen
        app.post("/addNewTask", ctx ->{
            openDBConnection();
            int projectID = 1;
            String name = "test";
            String description = "";
           preparedStatement = connection.prepareStatement("INSERT INTO `TASKS` VALUES (0,?,?,?,?)");
           preparedStatement.setInt(1,projectID);
           preparedStatement.setString(2, name);
           preparedStatement.setString(3,description);
           preparedStatement.setBoolean(4, false);
           preparedStatement.executeUpdate();
            System.out.println("Added New Task");
            closeDBSession();
           ctx.redirect("/project");

        });
    }

    //Datenbank Verbindung Herstellen
    public static void openDBConnection(){
        String url = "jdbc:mysql://sql11.freesqldatabase.com:3306/sql11450649";
        String user = "sql11450649";
        String password = "b8XJHtWxnq";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, user,password);
            System.out.println("DB Connection Succsessful");

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    // Datenbank Verbindung Trennen
    public static void closeDBSession(){
        try {
            connection.close();
            if (statement != null){
                statement.close();
            }
            if(preparedStatement != null){
                preparedStatement.close();
            }
            if(resultSet != null){
                resultSet.close();
            }
            System.out.println("DB Connection Closed");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


