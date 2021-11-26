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
        }).start(8030);
        openDBConnection();

        // Rendern einer Projekt Html Seite
        app.get("/project{projectID}", ctx ->{
            String projectID  = ctx.pathParam("projectID");
            HashMap<String, ArrayList<Task>> TasksMap = new HashMap<>();
            TasksMap.put("taskEntrys", getTaskEntrys(projectID));
            HashMap<String, ArrayList<Project>> ProjectsMap = new HashMap<>();
            ProjectsMap.put("projectEntrys", getProjectEntrys());

           ctx.render("/public/project.html", TasksMap);
           ctx.render("/public/project.html", ProjectsMap);
        });

        // Neuen Task zu der DB Hinzufuegen
        app.post("/addNewTask{projectID}", ctx ->{
            String projectID = ctx.pathParam("projectID");
            String name = ctx.formParam("taskName");
            String description = ctx.formParam("taskDesc");
            addTaskToDB(Integer.parseInt(projectID), name, description);
            ctx.redirect("/project" + projectID);
        });


        // Daten für Home seite aus DB abfragen
        app.get("/", ctx ->{
            HashMap<String, ArrayList<Project>> ProjectMap = new HashMap<>();
            ArrayList<Project> projectEntrys = getProjectEntrys();

            ProjectMap.put("projectEntrys", projectEntrys);
            ctx.render("/public/index.html",ProjectMap);
        });
        // Project Object zur Datenbank hinzufügen
        app.post("/addProject", ctx -> {
            String name = ctx.formParam("projektName");
            String color = ctx.formParam("color");

            if(color == ""){
                color = "#264653";
            }

            preparedStatement = connection.prepareStatement("INSERT INTO PROJECTS VALUES(PROJECT_ID.nextval,?,0,?,?)");
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2,0);
            preparedStatement.setString(3, color);
            preparedStatement.executeUpdate();
            System.out.println("Added Project Entry to Database");
            ctx.redirect("/");
        });
        // Project Object aus Datenbank Löschen
        app.post("/deleteProject{projectID}", ctx ->{
            String projectID = ctx.pathParam("projectID");
            preparedStatement = connection.prepareStatement("DELETE  FROM TASKS WHERE PROJECT_ID = " + projectID);
            preparedStatement.executeUpdate();
            System.out.println("Deletet all Tasks for Project " + projectID);
            preparedStatement = connection.prepareStatement("DELETE FROM PROJECTS WHERE PROJECT_ID = " + projectID);
            preparedStatement.executeUpdate();
            System.out.println("Deletet Project " + projectID);
            ctx.redirect("/");
        });
        // Project Object Bearbeiten
        app.post("configProject{projectID}", ctx ->{
            String projectID = ctx.pathParam("projectID");
            String name = ctx.formParam("projektName");
            String color = ctx.formParam("color");

            if(name == "" && color != null){
                preparedStatement = connection.prepareStatement("UPDATE PROJECTS SET COLOR = ? WHERE PROJECT_ID = " + projectID);
                preparedStatement.setString(1,color);
            }else if(name != null && color == ""){
                preparedStatement = connection.prepareStatement("UPDATE PROJECTS SET NAME = ? WHERE PROJECT_ID = " + projectID);
                preparedStatement.setString(1,name);
            }else{
                preparedStatement = connection.prepareStatement("UPDATE PROJECTS SET NAME = ?, COLOR = ? WHERE PROJECT_ID = " + projectID);
                preparedStatement.setString(1,name);
                preparedStatement.setString(2,color);
            }
            preparedStatement.executeUpdate();
            System.out.println("Projekt " + projectID + " erfolgreich Bearbeitet.");
            ctx.redirect("/");
        });

    }

    // Ab hier Hilfsmethoden

    //Datenbank Verbindung Herstellen
    public static void openDBConnection(){
        String url = "jdbc:oracle:thin:@//dbcluster12.cs.ohm-hochschule.de:1521/oracle.ohmhs.de";
        String user = "STUDI16_OWNER";
        String password = "OHM16_OWNER";
        try {
            Class.forName("oracle.jdbc.OracleDriver");
            connection = DriverManager.getConnection(url, user,password);
            System.out.println("DB Connection Succsessful");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static ArrayList<Project> getProjectEntrys(){
        try {
            String sql = "SELECT * FROM PROJECTS ORDER BY PROJECT_ID";
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            ArrayList<Project> projectEntrys = new ArrayList<>();



            while (resultSet.next()){
                projectEntrys.add(new Project(resultSet.getInt(1), resultSet.getString(2), resultSet.getInt(3), resultSet.getInt(4), resultSet.getString(5), 0));
            }

            // Implementierung für open Tasks
            for (int i = 0; i < projectEntrys.size(); i++){
                ResultSet resultSet2 = statement.executeQuery("SELECT COUNT(DONE) FROM TASKS WHERE PROJECT_ID = " + projectEntrys.get(i).id + "AND DONE = 0");
                resultSet2.next();
                projectEntrys.get(i).openTasks = resultSet2.getInt(1);
                resultSet2.close();
            }

            return  projectEntrys;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ArrayList<Task> getTaskEntrys(String projectID){
        try {
            String sql = "SELECT * FROM TASKS WHERE TASKS.PROJECT_ID =" + projectID + "ORDER BY TASK_ID";
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            ArrayList<Task> taskEntrys = new ArrayList<>();
            while (resultSet.next()){
                taskEntrys.add(new Task(resultSet.getInt(1), resultSet.getInt(2), resultSet.getString(3),resultSet.getString(4), resultSet.getInt(5)));
            }
            System.out.println("Found " + taskEntrys.size()+ " Entrys for ProjectID " + projectID);
            return  taskEntrys;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void addTaskToDB(int projectID, String name, String description){
        try {
            preparedStatement = connection.prepareStatement("INSERT INTO TASKS VALUES (TASK_ID.nextval,?,?,?,?)");
            preparedStatement.setInt(1,projectID);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3,description);
            preparedStatement.setInt(4, 0);
            preparedStatement.executeUpdate();
            System.out.println("Added New Task to Project with ID: " + projectID);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}


