
package bgu.spl.a2.sim.json;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Input {

    @SerializedName("threads")
    @Expose
    private Integer threads;
    @SerializedName("Computers")
    @Expose
    private List<JsonComputer> computers = null;
    @SerializedName("Phase 1")
    @Expose
    private List<Phase1> phase1 = null;
    @SerializedName("Phase 2")
    @Expose
    private List<Phase2> phase2 = null;
    @SerializedName("Phase 3")
    @Expose
    private List<Phase3> phase3 = null;

    public Integer getThreads() {
        return threads;
    }

    public void setThreads(Integer threads) {
        this.threads = threads;
    }

    public List<JsonComputer> getComputers() {
        return computers;
    }

    public void setComputers(List<JsonComputer> computers) {
        this.computers = computers;
    }

    public List<Phase1> getPhase1() {
        return phase1;
    }

    public void setPhase1(List<Phase1> phase1) {
        this.phase1 = phase1;
    }

    public List<Phase2> getPhase2() {
        return phase2;
    }

    public void setPhase2(List<Phase2> phase2) {
        this.phase2 = phase2;
    }

    public List<Phase3> getPhase3() {
        return phase3;
    }

    public void setPhase3(List<Phase3> phase3) {
        this.phase3 = phase3;
    }

}
