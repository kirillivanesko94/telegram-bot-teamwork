package pro.sky.telegrambotshelter.entity;

import javax.persistence.*;

@Entity
public class Photo {
    @Id
    @GeneratedValue
    private Long id;
    private String filePath;
    private Long fileSize;
    private String mediaType;

    private byte[] data;

    @OneToOne
    private Report report;
}