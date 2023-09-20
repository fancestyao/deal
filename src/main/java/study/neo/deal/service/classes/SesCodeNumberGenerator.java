package study.neo.deal.service.classes;

import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

@Component
public class SesCodeNumberGenerator {
    private static final Random RANDOM = new Random();
    private static final Set<Integer> USED_NUMBERS = new HashSet<>();
    private static final Integer MIN_VALUE = 100000;
    private static final Integer MAX_VALUE = 999999;

    public static Integer generate() {
        Integer number;
        do {
            number = RANDOM.nextInt(MAX_VALUE - MIN_VALUE + 1) + MIN_VALUE;
        } while (USED_NUMBERS.contains(number));
        USED_NUMBERS.add(number);
        return number;
    }
}