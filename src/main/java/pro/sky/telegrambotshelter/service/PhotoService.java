package pro.sky.telegrambotshelter.service;

import org.springframework.stereotype.Service;
import pro.sky.telegrambotshelter.repository.PhotoRepository;

/**
 * Service class for photo
 * @autor Egor
 */
@Service
public class PhotoService {

    /**
     * Method-constructor for DI
     */
    private final PhotoRepository photoRepository;
    public PhotoService(PhotoRepository photoRepository) {
        this.photoRepository = photoRepository;
    }
}
