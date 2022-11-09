package RoundTabler;

import java.lang.reflect.Field;

public class Configuration {

    private String type;
    private String dbType;
    private String server;
    private String user;
    private String password;
    private String database;
    private String file;

    public Configuration() {
        this.type = "";
        this.dbType = "";
        this.server = "";
        this.user = "";
        this.password = "";
        this.database = "";
        this.file = "";
    }

    //All getters and setters for config class

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDbType() {
        return this.dbType;
    }

    public void setDbType(String dbType) {
        this.dbType = dbType;
    }

    public String getServer() {
        return this.server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getUser() {
        return this.user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDatabase() {
        return this.database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getFile() {
        return this.file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    //Ensure all required parameters have been filled out except --resultfile since it isn't required

    public boolean allFilled(){
        for(Field f : getClass().getDeclaredFields()) {
            try {
                if (f.get(this) == "" && !f.getName().equals("file")) {
                    return false;
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

}
