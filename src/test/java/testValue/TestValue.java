package testValue;

import org.apache.commons.lang3.RandomStringUtils;

/**
 * Содержит значения для тестов, такие как: login, password и т.д
 */
public class TestValue extends RandomStringUtils{
    public static final String
            TEST_LOGIN_ONE = randomAlphabetic(10).toLowerCase()+"@yandex.ru",
            TEST_LOGIN_TWO = randomAlphabetic(10).toLowerCase()+"@yandex.ru",
            TEST_PASSWORD_ONE = "123",
            TEST_PASSWORD_TWO = "123qwe",
            TEST_NAME_ONE = randomAlphabetic(10),
            TEST_NAME_TWO = randomAlphabetic(10),
            TEST_BUN = "61c0c5a71d1f82001bdaaa6d",
            TEST_BAD_BUN = "123",
            TEST_FILLING_ONE = "61c0c5a71d1f82001bdaaa7a",
            TEST_FILLING_TWO = "61c0c5a71d1f82001bdaaa76";
}