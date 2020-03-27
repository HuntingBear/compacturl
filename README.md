# Compact URL maker

Разработать и реализовать web-сервис для сокращения URL.
Сервис должен позволять персистентно создавать короткую ссылку, ведущую на самого себя, а затем производить переадресацию на исходный ресурс 
при обращении по короткой ссылке.
Создание короткой ссылки должно быть реализовано как метод REST API (реализация frontend не требуется).

Web-сервис должен быть реализован с использованием следующих технологий:
- Java 13
- Spring Boot
- REST / JSON
- Maven


## Запуск

Компиляция:

    mvn clean install



Для работы приложения используется MongoDB.
Запуск docker образа mongo:

    docker run -d -p 27017:27017 mongo 
    

Запуск приложения:
 
    java -jar ./target/compacturl-1.0.jar
    
    
Приложение доступно на порту 9090

    localhost:9090/
    
    
## API

**POST** http://localhost:9090/ - создать новую короткую ссылку 
```json
{
	"url" : "http://ya.ru"
}
```

В ответе будет содержаться короткая ссылка (например: localhost:9090/WGltQuR)

**GET** http://localhost:9090/{идентификатор_короткой_ссылки} - редирект на первоначальную длинную ссылку.