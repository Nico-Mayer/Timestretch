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
        }).start(8080);
        openDBConnection();
        // Changelog eintrag zur DB hinzufuegen
        app.post("/changelogPost", ctx -> {
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
            ctx.redirect("/changeLogList");
        });

        // Rendern des Changelog
        app.get("/changeLogList", ctx ->{
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
            ctx.render("/public/changelog.html",Map);
        });

        // Rendern einer Projekt Html seite mit Dynamischen werten
        app.get("/project", ctx ->{
            String sql = "SELECT * FROM TASKS ORDER BY TASK_ID";
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);

            HashMap<String, ArrayList<Task>> Map = new HashMap<>();
            ArrayList<Task> taskEntrys = new ArrayList<>();

            while (resultSet.next()){
                taskEntrys.add(new Task(resultSet.getInt(1), resultSet.getInt(2), resultSet.getString(3),resultSet.getString(4), resultSet.getBoolean(5)));
            }
            Map.put("taskEntrys", taskEntrys);
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
           ctx.redirect("/project");

        });


        // Daten für Home seite aus DB abfragen
        app.get("/", ctx ->{
            String sql = "SELECT * FROM PROJEKTE";
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);

            HashMap<String, ArrayList<Project>> Map = new HashMap<>();
            ArrayList<Project> projectEntrys = new ArrayList<>();

            while (resultSet.next()){
                projectEntrys.add(new Project(resultSet.getInt(1), resultSet.getString(2), resultSet.getInt(3), resultSet.getBoolean(4), resultSet.getString(5)));
            }
            Map.put("projectEntrys", projectEntrys);
            ctx.render("/public/index.html", Map);
        });
        app.post("/addProject", ctx -> {
            String name = ctx.formParam("name");
            String color = ctx.formParam("color");

            preparedStatement = connection.prepareStatement("INSERT INTO `PROJEKTE` VALUES(0,?,0,?,?)");
            preparedStatement.setString(1, name);
            preparedStatement.setBoolean(2,false);
            preparedStatement.setString(3, color);
            preparedStatement.executeUpdate();
            System.out.println("Added Project Entry to Database");
            ctx.redirect("/");
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
}


