
package bgu.spl.a2.sim.json;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Phase1 {

    @SerializedName("Action")
    @Expose
    private String action;
    @SerializedName("Department")
    @Expose
    private String department;
    @SerializedName("Course")
    @Expose
    private String course;
    @SerializedName("Space")
    @Expose
    private String space;
    @SerializedName("Prerequisites")
    @Expose
    private List<String> prerequisites = null;
    @SerializedName("Student")
    @Expose
    private String student;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getSpace() {
        return space;
    }

    public void setSpace(String space) {
        this.space = space;
    }

    public List<String> getPrerequisites() {
        return prerequisites;
    }

    public void setPrerequisites(List<String> prerequisites) {
        this.prerequisites = prerequisites;
    }

    public String getStudent() {
        return student;
    }

    public void setStudent(String student) {
        this.student = student;
    }

}
