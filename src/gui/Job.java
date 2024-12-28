package gui;


public  class Job {
    private String title;
    private String contract;
    private String experience;
    private String startDate;
    private String endDate;
    private int jobPosts;

    public Job(String title, String contract, String experience, String startDate, String endDate, int jobPosts) {
        this.title = title;
        this.contract = contract;
        this.experience = experience;
        this.startDate = startDate;
        this.endDate = endDate;
        this.jobPosts = jobPosts;
    }

    public String getTitle() {
        return title;
    }

    public String getContract() {
        return contract;
    }

    public String getExperience() {
        return experience;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public int getJobPosts() {
        return jobPosts;
    }
}




//public class Job {
//    private String title;
//    private String contract;
//    private String experience;
//
//    public Job(String title, String contract, String experience) {
//        this.title = title;
//        this.contract = contract;
//        this.experience = experience;
//    }
//
//    public String getTitle() {
//        return title;
//    }
//
//    public String getContract() {
//        return contract;
//    }
//
//    public String getExperience() {
//        return experience;
//    }
//}
