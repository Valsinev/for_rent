# for_rent

**for_rent** This is a small Spring Boot MVC application that serves as a template for rooms/apartment/house for rent.


## ✨ Features

- **Displays information about the owner to the user** – on endpoint "/".
- **Displays information about the attraction in the area to the user** – "/attractions".
- **Displays information about the services the owner gives to the user** – "/services".
- **Displays information about the location of the property to the user** – "/location" + button to set automatic trip in google maps.
- **Displays information about the price and availability to the user** – "/pricing" displays all reserved dates as a calendar.
- **Displays photos of the property to the user** – "/photos".
- **Add/remove reservation dates to be displayed to the users** – "/admin".


## ⚙️ Installation & Setup

### Requirements

- Java 17+
- Maven
- IntelliJ IDEA (recommended)

### 📥 Clone the Repository

bash
git clone https://github.com/Valsinev/for_rent.git




### ⚙️ Environment Variables
Before running the application, set the following environment variables:
| Variable                   | Description                                                     | Default            |
| -------------------------- | --------------------------------------------------------------- | ------------------ |
| `SERVER_PORT`              | (Optional) Port to run the app on                               | `8080`             |
| `DATABASE_URL`             | Mysql database url to store the reserved dates                  |                    |
| `DATABASE_USER`            | The user for the database                                       |                    |
| `DATABASE_PASSWORD`        | The password for the database                                   |                    |
| `SPRING_SECURITY_USER`     | Hardcoded admin user that can access /admin endpoint and add/remove reservations  |                    |
| `SPRING_SECURITY_PASSWORD` | Hardcoded password for the admin user                           |                    |
| `OWNER_PHONE_NUMBER`       | Phone number for contacts to make reservations                  |                    |
| `OWNER_EMAIL_ADDRESS`      | Email address for contacts                          |                    |

💡 In IntelliJ, go to Run > Edit Configurations > Environment Variables to set these.

⚠️ Note: The images for this application are hardcoded if you want to change them you must add new in images folder and update them in the html templates.

### 🚀 Running the Application
✅ In IntelliJ (Recommended)

1. Open the project in IntelliJ IDEA.

2. Set environment variables (see above).

3. Find the main class: Application.java.

4. Right-click it and choose Run.

The API will start and be available at: http://localhost:8080/

🧪 From Terminal (Alternative)

./mvnw spring-boot:run

Or package and run the JAR:

./mvnw package
java -jar target/www-0.0.1-SNAPSHOT.jar

Make sure environment variables are exported before running:

...



## 🛠️ Technologies Used

- Java 17

- Spring Boot

- Spring Security

- Maven

- Bootstrap 5 (UI framework)

- Hibernate

- Spring Data JPA

