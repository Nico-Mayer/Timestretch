import javax.persistence.*;


@Entity
@Table(name = "TASKS")
public class Task {
    @Id
    @SequenceGenerator(name="seq",sequenceName="TASK_ID", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="seq")
    @Column(name = "TASK_ID")
    private int taskID;
    @ManyToOne
    @JoinColumn(name = "PROJECT_ID", nullable = false)
    private Project project;
    @Column(name = "NAME", nullable = false)
    private String name;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "DONE")
    private int done;


    public  Task(Integer taskID, Project project, String name, String description, int done){
        this.taskID = taskID;
        this.project = project;
        this.name = name;
        this.description = description;
        this.done = done;
    }

    public Task() {

    }

    public int getTaskID() {
        return taskID;
    }

    public Project getProject() {
        return project;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getDone() {
        return done;
    }

    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDone(int done) {
        this.done = done;
    }
}
