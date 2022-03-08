package ssii.pai1.integrity.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class Item {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    private String path;
    
    @Column
    private String hashFile;

    
    public Item(String path, String hashFile) {
        this.path = path;
        this.hashFile = hashFile;
    }

    public boolean isValid(){
        //Hash validation
        boolean b = this.hashFile != null && !this.hashFile.isBlank() && !this.hashFile.isEmpty();
        //Path validation
        b = b && this.path != null && !this.path.isBlank() && !this.path.isEmpty();
        return  b;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }



    public String getPath() {
        return path;
    }



    public void setPath(String path) {
        this.path = path;
    }



    public String getHashFile() {
        return hashFile;
    }

    public void setHashFile(String hashFile) {
        this.hashFile = hashFile;
    }

    
}
