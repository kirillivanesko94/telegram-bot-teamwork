package pro.sky.telegrambotshelter.shelter;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ShelterService {
    //TODO: необходимо заменить Map на БД
    private static Map<ShelterType, String> info = new HashMap<>();

    {
        info.put(ShelterType.DOG, "У нас тут песики!");
        info.put(ShelterType.CAT, "У нас тут кошечки!");
    }

    public String getInfo(ShelterType type) {
        // Инфу надо доставать из БД
        // сейчас просто заглушка
        return info.get(type);
    }
}
