package gui;

public class Job {
    private String title;
    private String contract;
    private String experience;

    public Job(String title, String contract, String experience) {
        this.title = title;
        this.contract = contract;
        this.experience = experience;
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
}
