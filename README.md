# ReadWell - Your Personal Library

ReadWell is a Spring Boot application that allows users to create their own personal library where they can upload and manage their books privately, or share them with the public.

## Features

- **User Authentication**: Users can sign up, log in, and manage their accounts securely.
- **Book Management**: Users can upload, view, edit, and delete books from their personal library.
- **Privacy Settings**: Users can choose to keep their library private or share it with the public.
- **SendGrid Integration**: The application uses SendGrid for email services, allowing users to receive notifications and updates.
- **MariaDB Database**: MariaDB is used as the database to store user information, book details, and other application data.

## Installation

1. Clone the repository:

git clone https://github.com/your-username/readwell.git


2. Navigate to the project directory:

cd readwell


3. Configure SendGrid API Key:

   - Sign up for a SendGrid account if you haven't already: [SendGrid](https://sendgrid.com/)
   - Obtain your SendGrid API Key.
   - Set the API Key as an environment variable in your operating system or update the `application.properties` file with your API Key:

   ```properties
   spring.sendgrid.api-key=YOUR_SENDGRID_API_KEY

4. Configure MariaDB:

Install and configure MariaDB on your local machine or server.
Create a new database for the application.
Update the application.properties file with your MariaDB database configuration:
properties

spring.datasource.url=jdbc:mariadb://localhost:3306/your_database_name
spring.datasource.username=your_username
spring.datasource.password=your_password


5. Build the project using Maven:

mvn clean install

6. Run the application:

mvn spring-boot:run

The application will start running at http://localhost:8080.

Usage
Access the application in your web browser at http://localhost:8080.
Sign up for a new account or log in if you already have one.
Start managing your personal library by uploading, viewing, editing, and deleting books.
Adjust privacy settings as desired to share your library with the public or keep it private.
Contributing
Contributions are welcome! Please follow the contribution guidelines when making changes to the project.

License
This project is licensed under the MIT License.

Acknowledgements
Spring Boot
SendGrid
MariaDB



Feel free to adjust the instructions and details according to your project's specifics!

