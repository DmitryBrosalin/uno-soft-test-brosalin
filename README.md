## Тестовое задание для компании Uno-Soft на должность Java-разработчик

Количество полученных групп с более чем одним элементом: 1910 (для test.txt) и 105036 (для lng-big.csv)

Время работы программы (в среднем): 5 секунд (для test.txt) и 30 секунд (для lng-big.csv)

Проект собирается при помощи maven в исполняемый .jar

jar запускается с ограничением по памяти 1ГБ:

java -jar uno-soft-test-brosalin.jar test.txt -Xmx1G

Или

java -jar uno-soft-test-brosalin.jar lng-big.csv -Xmx1G

Исполняемый .jar находится в папке out/artifacts/uno_soft_test_brosalin_jar

Файл lng-big.csv слишком большой для github, поэтому перед запуском программы его надо разархивировать из lng-big.7z.

Полученные группы выводятся в файл output.txt.