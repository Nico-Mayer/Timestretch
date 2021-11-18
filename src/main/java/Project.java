public class Project {
    public int id;
    public String name;
    public int totalTime;
    public boolean done;
    public String color;

    public  Project(Integer id, String name, Integer totalTime, boolean done, String color){
        this.id = id;
        this.name = name;
        this.totalTime = totalTime;
        this.done = done;
        this.color = color;
    }
}
