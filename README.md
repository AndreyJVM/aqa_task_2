# Задание 2: API: #
### *Нужно протестировать программу, которая помогает заказать бургер в [Stellar Burgers](https://stellarburgers.nomoreparties.site/)*
**Что нужно сделать:** протестировать ручки API для Stellar Burgers с использованием
документации API:
1. **Создание пользователя:** src/test/api.CreateUserTest
* создать уникального пользователя;
* создать пользователя, который уже зарегистрирован; 
* создать пользователя и не заполнить одно из обязательных полей.
2. **Логин пользователя:** src/test/api.LoginUserTest
* логин под существующим пользователем,
* логин с неверным логином и паролем.
3. **Изменение данных пользователя:** src/test/api.CreateUserTest
* с авторизацией,
* без авторизации,
4. **Создание заказа:** src/test/api.CreateOrderTest
* с авторизацией,
* без авторизации,
* с ингредиентами,
* без ингредиентов,
* с неверным хешем ингредиентов.
5. **Получение заказов конкретного пользователя:** src/test/api.GetOrderUserTest
* авторизованный пользователь, 
* неавторизованный пользователь.
### В проекте используется: ###

![Linkedin-url](https://img.shields.io/badge/Java-_11-red)
#### Библиотеки: ####
![Linkedin-url](https://img.shields.io/badge/Maven-version_4.0.0-blue)

![Linkedin-url](https://img.shields.io/badge/Allure-version_2.15-blue)

![Linkedin-url](https://img.shields.io/badge/RestAssured-version_5.3.0-blue)

![Linkedin-url](https://img.shields.io/badge/JUnit_4-version_4.13.2-blue)

---
###### Над проектом работал [Andrey Vorobev](https://github.com/AndreyJVM)
