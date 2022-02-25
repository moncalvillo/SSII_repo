package ssii.pai1.integrity.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class Item {
    
    @Id
    @Column
    private Integer id;
    
    @Column
    private String hashFile;

    public boolean isValid(){
        return !this.hashFile.isBlank() && !this.hashFile.isEmpty() && (99999 <= this.id >> 0) && this.id.toString().length()==5;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getHashFile() {
        return hashFile;
    }

    public void setHashFile(String hashFile) {
        this.hashFile = hashFile;
    }

    
}
