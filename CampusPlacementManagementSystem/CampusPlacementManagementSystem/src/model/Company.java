package model;

/**
 * Model class representing a recruiting Company entity.
 */
public class Company {
    private int companyId;
    private String companyName;
    private String location;
    private String hrName;
    private String hrEmail;
    private String hrPhone;

    public Company() {}

    public Company(String companyName, String location, String hrName, String hrEmail, String hrPhone) {
        this.companyName = companyName;
        this.location = location;
        this.hrName = hrName;
        this.hrEmail = hrEmail;
        this.hrPhone = hrPhone;
    }

    public Company(int companyId, String companyName, String location, String hrName, String hrEmail, String hrPhone) {
        this(companyName, location, hrName, hrEmail, hrPhone);
        this.companyId = companyId;
    }

    // Getters and Setters
    public int getCompanyId() { return companyId; }
    public void setCompanyId(int companyId) { this.companyId = companyId; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getHrName() { return hrName; }
    public void setHrName(String hrName) { this.hrName = hrName; }

    public String getHrEmail() { return hrEmail; }
    public void setHrEmail(String hrEmail) { this.hrEmail = hrEmail; }

    public String getHrPhone() { return hrPhone; }
    public void setHrPhone(String hrPhone) { this.hrPhone = hrPhone; }

    @Override
    public String toString() {
        return "Company{" +
                "id=" + companyId +
                ", name='" + companyName +
                ", location='" + location +
                '}';
    }
}
