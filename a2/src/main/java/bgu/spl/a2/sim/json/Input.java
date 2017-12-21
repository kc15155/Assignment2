
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
    private List<Phase1AndActions> phase1 = null;
    @SerializedName("Phase 2")
    @Expose
    private List<Phase1AndActions> phase2 = null;
    @SerializedName("Phase 3")
    @Expose
    private List<Phase1AndActions> phase3 = null;

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

    public List<Phase1AndActions> getPhase1() {
        return phase1;
    }

    public void setPhase1(List<Phase1AndActions> phase1) {
        this.phase1 = phase1;
    }

    public List<Phase1AndActions> getPhase2() {
        return phase2;
    }

    public void setPhase2(List<Phase1AndActions> phase2) {
        this.phase2 = phase2;
    }

    public List<Phase1AndActions> getPhase3() {
        return phase3;
    }

    public void setPhase3(List<Phase1AndActions> phase3) {
        this.phase3 = phase3;
    }

}
