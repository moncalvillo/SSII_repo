package ssii.pai1.integrity.model;


import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Table
public class Entity {
    
    @Id
    @Column
    private int id;
    
    @Column
    private String hashFile;
    
    @Column
    private String token;


    private boolean isValid(){
        return true;
    }
}
