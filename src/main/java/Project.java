import java.sql.Blob;

public class Project {
    public int id;
    public String name;
    public int totalTime;
    public int done;
    public String color;
    public int openTasks;

    public  Project(Integer id, String name, Integer totalTime, int done, String color, int openTasks){
        this.id = id;
        this.name = name;
        this.totalTime = totalTime;
        this.done = done;
        this.color = color;
        this.openTasks = openTasks;
    }
}
