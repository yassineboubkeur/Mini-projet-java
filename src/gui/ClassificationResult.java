package gui;

public class ClassificationResult {
    private String title;
    private String actualContract;
    private String predictedContract;

    public ClassificationResult(String title, String actualContract, String predictedContract) {
        this.title = title;
        this.actualContract = actualContract;
        this.predictedContract = predictedContract;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getActualContract() {
        return actualContract;
    }

    public void setActualContract(String actualContract) {
        this.actualContract = actualContract;
    }

    public String getPredictedContract() {
        return predictedContract;
    }

    public void setPredictedContract(String predictedContract) {
        this.predictedContract = predictedContract;
    }
}
