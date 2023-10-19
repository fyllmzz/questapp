package com.project.questapp.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import lombok.Data;
@Entity
@Table(name="user")
@Data
public class User {
    @Id
    Long id;

    String userName;
    String password;
}
