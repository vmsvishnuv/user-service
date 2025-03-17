package com.myproject.user_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq_gen")
    @SequenceGenerator(name = "user_seq_gen", sequenceName = "natural_sequence", allocationSize = 1)
    private Long id;

    @Column(unique = true)
    private String userID;

    @Column(name = "user_name")
    private String userName;

    private String email;

    private String address;

    @PrePersist
    public void generateCustomerUserID(){
        if(this.id != null){
            this.userID = "USER" + String.format("%04d", this.id);
        }
    }
}
