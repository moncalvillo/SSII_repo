package ssii.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class LogLine {

    private static final String path = "./src/main/resources/logs/logs.log";
    
    private String msg;
    private LogType type;
    private String request;
    private String method;
    private String bodyParams;
    private LocalDateTime date;
    
    public LogLine(String msg, LogType type, String request, String method, String bodyParams) {
        this.msg = msg;
        this.type = type;
        this.request = request;
        this.method = method;
        this.bodyParams = bodyParams;
        this.date = LocalDateTime.now();
    }
    
    public void writeLog(){
        try {
            FileWriter fWriter = new FileWriter(path,true);
            fWriter.append(this.toString());
            System.out.println(this);
            fWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public String toString() {
        return "[" + this.type + "]~[" + this.date + "]~" + this.msg + " ~ " + this.method + " " + this.request + " " + this.bodyParams + "\n";
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }
    public LogType getType() {
        return type;
    }
    public void setType(LogType type) {
        this.type = type;
    }
    public String getRequest() {
        return request;
    }
    public void setRequest(String request) {
        this.request = request;
    }
    public String getMethod() {
        return method;
    }
    public void setMethod(String method) {
        this.method = method;
    }
    public String getBodyParams() {
        return bodyParams;
    }
    public void setBodyParams(String bodyParams) {
        this.bodyParams = bodyParams;
    }


    

}
