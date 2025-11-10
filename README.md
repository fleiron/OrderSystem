# 🧩 Order System — Практична №4

Багатомодульна система управління замовленнями  
*(Generics, Lambda, Multithreading, Modules)*

---

## ⚙️ Структура проєкту
order-system/
├── order-model/ # Модель товарів (Product, Electronics, Clothing)
├── order-processing/ # Обробка замовлень (OrderProcessor)
├── order-storage/ # Сховище замовлень (OrderStorage)
├── order-concurrent/ # Потоки для обробки замовлень
└── order-app/ # Головний модуль з Main.java 



---

## 🚀 Як запустити
> Вимоги: **Java 17** і **Maven 3.9+**

---

### 1️⃣ Клонування репозиторію
```bash
git clone https://github.com/fleiron/OrderSystem.git
cd OrderSystem
2️⃣ Очистка і збірка
bash
Копіювати код
mvn clean install -DskipTests
🔹 Це збере всі модулі:
order-model, order-processing, order-storage, order-concurrent, order-app

3️⃣ Запуск програми
bash
Копіювати код
cd order-app
mvn exec:java -Dexec.mainClass=com.example.order.app.Main -DskipTests
✅ Приклад виводу

yaml
Копіювати код
Processed order of Electronics: міцний дерев’яний гаманець, price: 1042.06, thread: pool-2-thread-1
Processed order of Clothing: неймовірний шерстяний стіл, price: 200.78, thread: pool-2-thread-4
All stored orders: 10
Electronics stored: 5
Clothing stored: 5
🧠 Реалізовано з використанням:

Generics з обмеженнями <T extends Product>

Lambda-виразів і method references

Модулів Java 17 (module-info.java)

Патерну Builder (Lombok)

JavaFaker для генерації даних

Багатопоточності (Executors)
