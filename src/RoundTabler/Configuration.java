package RoundTabler;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class Configuration {

    private String type;
    private String[] validScanTypes = { "all", "nacha", "pci"};
    private String dbType;
    private String[] validDbTypes = { "mysql", "mysql", "mysql"};
    private String server;
    private String user;
    private String password;
    private String database;
    private String file;
    private String table;
    private String queryStatement;

    public Configuration() {
        this.type = "";
        this.dbType = "";
        this.server = "";
        this.user = "";
        this.password = "";
        this.database = "";
        this.file = "";
        this.table = "";
        this.queryStatement = "";
    }

    //All getters and setters for config class

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean validateScanType(){
        List<String> types = Arrays.asList(this.validScanTypes);

        return types.contains(this.type.toLowerCase());
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

    public boolean validateDbType(){
        List<String> types = Arrays.asList(this.validDbTypes);

        return types.contains(this.dbType.toLowerCase());
    }

    public String getFile() {
        return this.file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getTable() {
        return this.table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getQueryStatement() {
        return this.queryStatement;
    }

    public void setQueryStatement(String statement) {
        this.queryStatement = statement;
    }

    //Ensure all required parameters have been filled out except --resultfile since it isn't required

    public boolean allFilled(){
        for(Field f : getClass().getDeclaredFields()) {
            try {
                if (f.get(this) == "" && !f.getName().equals("file") && !f.getName().equals("table") && !f.getName().equals("queryStatement")) {
                    return false;
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

}
