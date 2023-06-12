package pro.sky.telegrambotshelter.entity;

import javax.persistence.*;

/**
 * Класс сущности Фото для отчета
 * @autor Егор
 */
@Entity
public class Photo {
    /** Поле id */
    @Id
    @GeneratedValue
    private Long id;
    /** Поле путь файла */
    private String filePath;
    /** Поле размер файла */
    private Long fileSize;
    /** Поле тип медиа файла */
    private String mediaType;

    /** Поле данные файла */
    private byte[] data;

    /** Поле отчет, в котором находится фото*/
    @OneToOne
    private Report report;
}