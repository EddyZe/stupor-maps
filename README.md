# Описание

<img src=https://img.shields.io/badge/Vaadin-blue></img>
<img src=https://img.shields.io/badge/Spring--boot-green></img>
<img src=https://img.shields.io/badge/lombok-red></img>
<img src=https://img.shields.io/badge/Java-orange></img>

<img src="screens/2024-06-27_13-55-47.png"></img>
Программа по заданным точкам выделяет полигон на карте и выводит координаты создавшегося полигона в консоль.

Укажите на карте точки затем нажмите "Create polygon".



### Компиляция и запуск

___1. Компиляция___
```
mvn clean install
```

___2. Запуск___

````
cd target

java -jar stupor-maps-0.0.1-SNAPSHOT.jar
````

После запуска в браузере перейдите на: http://localhost:8080