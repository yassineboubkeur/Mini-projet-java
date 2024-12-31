
package gui;

public class ClassificationResult {
    private String title;
    private String actualContract;
    private String predictedContract;
    private String experienceRequired; // Ajout de l'attribut experienceRequired

    public ClassificationResult(String title, String actualContract, String predictedContract, String experienceRequired) {
        this.title = title;
        this.actualContract = actualContract;
        this.predictedContract = predictedContract;
        this.experienceRequired = experienceRequired; // Initialisation du nouvel attribut
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

    public String getExperienceRequired() {
        return experienceRequired; // Getter pour experienceRequired
    }

    public void setExperienceRequired(String experienceRequired) {
        this.experienceRequired = experienceRequired; // Setter pour experienceRequired
    }
}





//package gui;
//
//public class ClassificationResult {
//    private String title;
//    private String actualContract;
//    private String predictedContract;
//
//    public ClassificationResult(String title, String actualContract, String predictedContract) {
//        this.title = title;
//        this.actualContract = actualContract;
//        this.predictedContract = predictedContract;
//    }
//
//    public String getTitle() {
//        return title;
//    }
//
//    public void setTitle(String title) {
//        this.title = title;
//    }
//
//    public String getActualContract() {
//        return actualContract;
//    }
//
//    public void setActualContract(String actualContract) {
//        this.actualContract = actualContract;
//    }
//
//    public String getPredictedContract() {
//        return predictedContract;
//    }
//
//    public void setPredictedContract(String predictedContract) {
//        this.predictedContract = predictedContract;
//    }
//}
