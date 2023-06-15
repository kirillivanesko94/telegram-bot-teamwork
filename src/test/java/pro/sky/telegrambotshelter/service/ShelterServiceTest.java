package pro.sky.telegrambotshelter.service;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pro.sky.telegrambotshelter.entity.Shelter;
import pro.sky.telegrambotshelter.repository.ShelterRepository;
import pro.sky.telegrambotshelter.shelter.ShelterType;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;


@ContextConfiguration(classes = {ShelterService.class})
@ExtendWith(SpringExtension.class)
public class ShelterServiceTest {
    @Autowired
    private ShelterService shelterService;

    @MockBean
    private ShelterRepository shelterRepository;

    @Test
    void getInfoTest() {
        Shelter shelter = new Shelter();
        shelter.setType(ShelterType.DOG);
        shelter.setName("собаки");
        shelter.setDescription("приют для собак");
        shelter.setInstruction("как забрать собаку");

        when(shelterRepository.findFirstByType(ShelterType.DOG)).thenReturn(Optional.of(shelter));
        String expected = "приют для собак";

        String actual = shelterService.getInfo(ShelterType.DOG);
        assertEquals(expected, actual);
    }

    @Test
    void getInfoIsNullTest() {
        Shelter shelter = new Shelter();
        shelter.setType(ShelterType.DOG);
        shelter.setName("собаки");
        shelter.setInstruction("как забрать собаку");

        when(shelterRepository.findFirstByType(ShelterType.DOG)).thenReturn(Optional.of(shelter));
        String expected = null;

        String actual = shelterService.getInfo(ShelterType.DOG);
        assertEquals(expected, actual);
    }

    @Test
    void getInstructionTest() {
        Shelter shelter = new Shelter();
        shelter.setType(ShelterType.DOG);
        shelter.setName("собаки");
        shelter.setDescription("приют для собак");
        shelter.setInstruction("как забрать собаку");

        when(shelterRepository.findFirstByType(ShelterType.DOG)).thenReturn(Optional.of(shelter));
        String expected = "как забрать собаку";

        String actual = shelterService.getInstruction(ShelterType.DOG);
        assertEquals(expected, actual);
    }

    @Test
    void getInstructionNullTest() {
        Shelter shelter = new Shelter();
        shelter.setType(ShelterType.DOG);
        shelter.setName("собаки");
        shelter.setDescription("приют для собак");

        when(shelterRepository.findFirstByType(ShelterType.DOG)).thenReturn(Optional.of(shelter));
        String expected = null;

        String actual = shelterService.getInstruction(ShelterType.DOG);
        assertEquals(expected, actual);
    }
}
