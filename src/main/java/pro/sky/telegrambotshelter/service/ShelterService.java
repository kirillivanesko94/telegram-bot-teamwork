package pro.sky.telegrambotshelter.service;

import org.springframework.stereotype.Service;
import pro.sky.telegrambotshelter.entity.Shelter;
import pro.sky.telegrambotshelter.repository.ShelterRepository;
import pro.sky.telegrambotshelter.shelter.ShelterType;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShelterService {
    private final ShelterRepository shelterRepository;

    public ShelterService(ShelterRepository shelterRepository) {
        this.shelterRepository = shelterRepository;
    }

    public List<Shelter> getInfo(ShelterType type) {
        return shelterRepository.findAll().stream().filter(x -> x.getAnimals().contains(type)).collect(Collectors.toList());
    }
}
