
import javax.persistence.*;

@Entity
@Table(name = "PROJECTS")

public class Project {
    @Id
    @SequenceGenerator(name="seq",sequenceName="CHANGE_ID", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="seq")
    @Column(name = "PROJECT_ID", nullable = false)
    private int projectId;

    @Column(name = "NAME", nullable = false)
    private String name;
    @Column(name = "TOTAL_TIME")
    private int totalTime;
    @Column(name = "DONE")
    private int done;
    @Column(name = "COLOR", nullable = false)
    private String color;
    @Column(name = "OPEN_TASKS")
    private int openTasks;

    public Project(Integer projectId, String name, int totalTime, int done, String color, int openTasks) {
        this.projectId = projectId;
        this.name = name;
        this.totalTime = totalTime;
        this.done = done;
        this.color = color;
        this.openTasks = openTasks;
    }

    public Project() {

    }


    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public String getName() {
        return name;
    }

    public int getTotalTime() {
        return totalTime;
    }

    public int getDone() {
        return done;
    }

    public String getColor() {
        return color;
    }
    public int getOpenTasks() {
        return openTasks;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setTotalTime(int totalTime) {
        this.totalTime = totalTime;
    }

    public void setDone(int done) {
        this.done = done;
    }

    public void setColor(String color) {
        this.color = color;
    }
    public void setOpenTasks(int openTasks) {
        this.openTasks = openTasks;
    }
}
