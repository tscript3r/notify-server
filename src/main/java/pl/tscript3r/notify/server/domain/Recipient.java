package pl.tscript3r.notify.server.domain;

public class Recipient {

    private Integer userId;
    private String email;
    private String name;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        return userId;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null)
            return (obj.hashCode() == userId);
        return false;
    }

}