import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import java.sql.*;
import java.util.*;

public class App {
    static EntityManagerFactory emf;
    static EntityManager em;
    static EntityTransaction entityTransaction;

    public static void main(String[] args) {
        openDBConnection();
        Javalin app = Javalin.create(config -> {
            config.addStaticFiles("/public", Location.CLASSPATH);
        }).start(8090);

        //-------------------------------------Rendern der TH Templates-------------------------------------------------
        // Index.html
        app.get("/", ctx ->{
            HashMap<String, List<Project>> ProjectMap = new HashMap<>();
            List<Project> projectEntrys = getProjectEntrys();
            ProjectMap.put("projectEntrys", projectEntrys);
            ctx.render("/public/index.html",ProjectMap);
        });

        // Project.html
        app.get("/project{projectID}", ctx ->{
            int projectID  = Integer.parseInt(ctx.pathParam("projectID"));
            HashMap<String, List<Task>> TasksMap = new HashMap<>();
            TasksMap.put("taskEntrys", getTaskEntrys(projectID));
            HashMap<String, List<Project>> ProjectsMap = new HashMap<>();
            ProjectsMap.put("projectEntrys", getProjectEntrys());
           ctx.render("/public/index.html", TasksMap);
           ctx.render("/public/index.html", ProjectsMap);
        });
        //------------------------------------------------TASKS---------------------------------------------------------

        // Neuen Task zu der DB Hinzufuegen
        app.post("/addNewTask{projectID}", ctx ->{
            int projectId = Integer.parseInt(ctx.pathParam("projectID"));
            String name = ctx.formParam("taskName");
            String description = ctx.formParam("taskDesc");
            addTaskToDB(projectId,name, description);
            ctx.redirect("/project" + projectId);
        });

        // Task aus DB Löschen
        app.post("/deleteTask{taskId}", ctx ->{
           int taskId = Integer.parseInt(ctx.pathParam("taskId"));
           int projectId = em.find(Task.class, taskId).getProject().getProjectId();
           deleteTask(taskId);
           ctx.redirect("project" + projectId);
        });

        // Task auf Done Setzen
        app.get("/doneTask{taskId}", ctx ->{
            System.out.println("in");
            int taskId = Integer.parseInt(ctx.pathParam("taskId"));
            int projectId = em.find(Task.class, taskId).getProject().getProjectId();
            entityTransaction.begin();
            Task task = em.find(Task.class, taskId);
            task.setDone(1);
            entityTransaction.commit();
            ctx.redirect("project" + projectId);
        });

        //------------------------------------------------PROJECTS------------------------------------------------------

        // Project Object zur Datenbank hinzufügen
        app.post("/addProject", ctx -> {
            String name = ctx.formParam("projektName");
            String color = ctx.formParam("color");
            if(color == ""){
                color = "#264653";
            }
            addProject(name, color);
            ctx.redirect("/");
        });

        // Project Object Bearbeiten
        app.post("configProject{projectID}", ctx ->{
            int projectID = Integer.parseInt(ctx.pathParam("projectID"));
            String name = ctx.formParam("projektName");
            String color = ctx.formParam("color");
            editProject(projectID, name, color);
            ctx.redirect("/");
        });

        // Project Object aus Datenbank Löschen
        app.post("/deleteProject{projectID}", ctx ->{
            int projectID = Integer.parseInt(ctx.pathParam("projectID"));
            deleteProject(projectID);
            ctx.redirect("/");
        });

    }

    // -----------------------------------------------Ab hier Hilfsmethoden--------------------------------------------
    //Datenbank Verbindung Herstellen
    public static void openDBConnection(){
        emf = Persistence.createEntityManagerFactory("timestretch_pu");
        em = emf.createEntityManager();
        entityTransaction = em.getTransaction();
        System.out.println("Connected to DB");
    }

    //Funktionen für Tasks

    public static List<Task> getTaskEntrys(int projectID){
        List<Task> tasks = em.createQuery("SELECT tasks FROM Task tasks WHERE project.projectId = " + projectID, Task.class).getResultList();
        return tasks;
    }

    public static void addTaskToDB(int projectID, String name, String description){
        Project project = em.find(Project.class, projectID);
        entityTransaction.begin();
        Task task = new Task();
        task.setProject(project);
        task.setName(name);
        task.setDescription(description);
        em.persist(task);
        entityTransaction.commit();
    }
    public static void editTask(){

    }
    public static void deleteTask(int taskID){
        Task task = em.find(Task.class, taskID);
        entityTransaction.begin();
        em.remove(task);
        entityTransaction.commit();
    }
    public static void taskSetDone(int taskID){

    }

    //Funktionen für Projecte

    public static List<Project> getProjectEntrys(){
        List<Project> projects = em.createQuery("SELECT projects FROM Project projects ORDER BY projectId", Project.class).getResultList();

        for (int i = 0; i< projects.size(); i++){
            Query q = em.createQuery("SELECT count (done) FROM Task where project.projectId = " + projects.get(i).getProjectId() + "AND done = 0");
            int count = ((Number) q.getSingleResult()).intValue();
            projects.get(i).setOpenTasks(count);
        }

        return  projects;
    }

    public static void addProject(String name, String color){
        entityTransaction.begin();
        Project newProject = new Project();
        newProject.setName(name);
        newProject.setColor(color);
        em.persist(newProject);
        entityTransaction.commit();
    }

    public static void editProject(int projectID, String name, String color){
        entityTransaction.begin();
        Project project = em.find(Project.class, projectID);
        if(name == "" && color != null){
            project.setColor(color);
        }else if(name != null && color == ""){
            project.setName(name);
        }else{
            project.setName(name);
            project.setColor(color);
        }
        entityTransaction.commit();
    }

    public static void deleteProject(int projectID){
        entityTransaction.begin();
        Query q = em.createQuery("DELETE From Task tasks WHERE tasks.project.projectId = " + projectID);
        q.executeUpdate();
        Project project = em.find(Project.class, projectID);
        em.remove(project);
        entityTransaction.commit();
    }
}


