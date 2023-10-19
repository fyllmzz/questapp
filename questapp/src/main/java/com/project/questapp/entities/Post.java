package com.project.questapp.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table (name = "post")
@Data
public class Post {
    @Id
    Long id;

    // Long userId;
    //userId alanı user objesini temsil eder.
    // birebir ilişki
//    @ManyToOne(fetch = FetchType.LAZY) //user obj db den hemen çekme
    //  @JsonIgnore
    //Request obj oluşturduğumuz için
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn (name = "user_id",nullable = false)//db deki userid alanı user tablosuna bağlandı
    @OnDelete(action = OnDeleteAction.CASCADE ) //bir user silindiğinde tüm postları silinmeli
    User user;

    String title;
    @Lob
    @Column(columnDefinition = "text")
    String text;//string alanını text olarak ayarlandı.
}
