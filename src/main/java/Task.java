public class Task {
    public int taskID;
    public int projectID;
    public String name;
    public String description;
    public boolean done;


    public  Task(Integer taskID, Integer projectID, String name, String description, boolean done){
        this.taskID = taskID;
        this.projectID = projectID;
        this.name = name;
        this.description = description;
        this.done = done;
    }

}
