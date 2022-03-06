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
    private String id;
    
    @Column
    private String hashFile;

    public boolean isValid(){
        return !this.id.equals(null) && !this.hashFile.equals(null) && !this.hashFile.isBlank() && !this.hashFile.isEmpty() && !this.id.isBlank() && !this.id.isEmpty();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHashFile() {
        return hashFile;
    }

    public void setHashFile(String hashFile) {
        this.hashFile = hashFile;
    }

    
}
