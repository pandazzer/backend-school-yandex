# backend-school-yandex
REST сервис который добавляет, удаляет и выводит товары и/или категории товаров в формате json.
Сервис умеет обрабатывать post (для добавления товаров/категорий), get (для получения списка товаров или категорий) и 
delete (для удаления товаров или категорий) запросы.

Примеры запроса сервису:
post: localhost:80/imports
delete: localhost:80/delete/{id}
get: localhost:80/nodes/{id}


Запустить приложение можно через класс BackendSchoolYandexApplication

Можно собрать Docker контейнер, находясь в папке проекта и набрав в терминале:

sudo docker build . --tag bsy
и затем:

sudo docker run -p 80:80 bsy