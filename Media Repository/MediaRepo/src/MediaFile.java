import java.util.Date;

public class MediaFile
{
    private String name, format, size, type, date;
    
    public MediaFile(String name, String format, String size, String type, String date){
        this.name = name;
        this.format = format;
        this.size = size;
        this.type = type;
        this.date = date;
    }
    
    public String getName(){
        return this.name;
    }
    public void setName(String name){
        this.name = name;
    }
    
     public String getFormat(){
        return this.format;
    }
    public void setFormat(String format){
        this.format = format;
    }
    
     public String getSize(){
        return this.size;
    }
    public void setSize(String size){
        this.size = size;
    }
    
     public String getType(){
        return this.type;
    }
    public void setType(String type){
        this.type = type;
    }
    
     public String getDate(){
        return this.date;
    }
    public void setDate(String date){
        this.date = date;
    }
}