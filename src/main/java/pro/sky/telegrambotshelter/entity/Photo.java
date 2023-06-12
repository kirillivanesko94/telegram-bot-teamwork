package pro.sky.telegrambotshelter.entity;

import javax.persistence.*;

/**
 * Entity class for report photo
 * @autor Egor
 */
@Entity
public class Photo {
    /** Field identifier */
    @Id
    @GeneratedValue
    private Long id;
    /** Field file path */
    private String filePath;
    /** Field file size */
    private Long fileSize;
    /** Field file media type */
    private String mediaType;

    /** Field file data */
    private byte[] data;

    /** Field report*/
    @OneToOne
    private Report report;
}