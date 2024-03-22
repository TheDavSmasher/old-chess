package serviceTests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import service.AppService;


class AppServiceTest {

    @Test
    void clearData() {
        Assertions.assertDoesNotThrow(AppService::clearData);
    }
}