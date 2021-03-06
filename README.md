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

## ВАЖНОЕ замечание для Стаса

Лучше смотреть в ветку uniqueUrls, т.к. там реализация без ключей идемпотентности.

Теперь по условиям уникальность надо поддерживать для ключа идемпотентности.
Используется атомарная операция поиска и добавления новой записи в MongoDB.
Метод добавления сделан синхронизированным, чтобы избежать возможной генерации разных id для одинаковых записей в разных потоках.
На поле idempotenceKey "повешен" индекс для ускорения поиска и для обеспечения уникальности на уровне БД. 

Реализация идемпотентности POST запроса имеет очевидный минус: при больших объемах хранимых данных и при большой нагрузке на приложение скорость поиска url по ключу идемпотентности будет очень медленной. Я осознаю, что эта реализация неоптимальна и собираюсь переделать эту часть. Хранение и поиск ключа идемпотентности необходимо выделить из сущности url и перенести в кэш, где данные будут храниться ограниченное время. Это позволит снять нагрузку на БД и всегда иметь небольшой объем данных, по которому производится поиск ключа.  


## Запуск

Компиляция:

    mvn clean install



Для работы приложения используется MongoDB.
Скачивание и запуск docker образа mongo:

    docker pull mongo
    docker run -d -p 27017:27017 mongo 
    

Запуск приложения:
 
    java -jar ./target/compacturl-1.0.jar
    
    
Приложение доступно на порту 9090

    localhost:9090/
    
    
## API

**POST** http://localhost:9090/ - создать новую короткую ссылку 

POST запрос должен содержать специальный header **Idempotence-Key** позволяющий сделать его (запрос) идемпотентным, т.е. отправив несколько раз запрос с одинаковым содержимым и одинаковым ключом идемпотентности, результат вернется одинаковый.
Рекомендуется изменять ключ идемпотентности при каждом запросе. контроль за этим остается на стороне клиента. В качестве ключа идемпотентности рекомендуется использовать UUID. 
```json
{
	"url" : "http://ya.ru"
}
```
```text
curl http://localhost:9090/ \
  -X POST \
  -H 'Idempotence-Key: <Ключ идемпотентности>' \
  -H 'Content-Type: application/json' \
  -d '{
        "url": "http://ya.ru"
      }'
```

В ответе будет содержаться короткая ссылка (например: localhost:9090/WGltQuR)

**GET** http://localhost:9090/{идентификатор_короткой_ссылки} - редирект на первоначальную длинную ссылку.