package ntnu.idatt2106.backend;

import jakarta.annotation.Resource;
import ntnu.idatt2106.backend.config.TestDatabaseJpaConfig;
import ntnu.idatt2106.backend.model.Refrigerator;
import ntnu.idatt2106.backend.repository.RefrigeratorRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
        classes = { TestDatabaseJpaConfig.class },
        loader = AnnotationConfigContextLoader.class)
public class RefrigeratorTests {
    @Resource
    private RefrigeratorRepository refrigeratorRepository;

    @Test
    void possible_to_register_a_refrigerator_in_the_database() {
        int refrigeratorId = 1;
        Refrigerator refrigerator = new Refrigerator(refrigeratorId, "Test", "NTNU");

        Refrigerator result = refrigeratorRepository.save(refrigerator);

        assertEquals(result, refrigerator);
    }

}
