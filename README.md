# PrepWise AI – AI-Powered Interview Preparation Platform

PrepWise AI is a full-stack web application designed to help students prepare for technical interviews and placement opportunities through AI-assisted learning, interview practice, resume analysis, and career guidance.

The platform brings multiple interview preparation tools together in one place, helping users practice technical concepts, improve their problem-solving skills, and track their preparation progress.

## Features

### 1. AI Interview Practice
- Practice technical, HR, behavioral, and mixed interview questions.
- Choose a technical role, difficulty level, experience level, and number of questions.
- Receive AI-assisted feedback on interview responses.

### 2. Resume Analyzer
- Analyze resumes and review ATS-related scores.
- Identify matched, weak, and missing keywords.
- Review resume completeness and areas that need improvement.

### 3. Coding Practice
- Practice programming questions to improve problem-solving skills.
- Strengthen coding knowledge for technical placement assessments.

### 4. Group Discussion Practice
- Practice group discussion topics.
- Improve communication and idea presentation for placement rounds.

### 5. Personalized Study Plan
- Organize interview preparation activities.
- Plan learning based on career goals and technical roles.

### 6. AI Mentor
- Get AI-assisted guidance while learning technical concepts.
- Support interview preparation through interactive assistance.

### 7. Career Roadmap
- Explore preparation paths for different technical roles.
- Identify skills and topics to focus on during placement preparation.

### 8. Interview History and Progress
- Review previous interview practice sessions.
- Track available scores and preparation activity.

### 9. Leaderboard and Profile
- View leaderboard information.
- Manage profile and application preferences.

## Technology Stack

| Component | Technologies |
|---|---|
| Frontend | React.js, Vite |
| Styling | Tailwind CSS |
| Backend | Java, Spring Boot |
| API Communication | REST APIs |
| Database | MySQL |
| Authentication | JSON Web Tokens (JWT) |
| AI Integration | Gemini API |
| Voice Interaction | Web Speech API |
| Computer Vision | MediaPipe |

## Project Architecture

The application follows a full-stack architecture.

- **Frontend:** Provides the user interface and interactive interview preparation features.
- **Backend:** Handles application logic, REST API endpoints, authentication, and communication with the database and AI services.
- **Database:** MySQL stores application data.
- **AI Services:** Gemini API supports AI-powered functionality where integrated.

## Project Structure

```text
prepwise-ai/
├── backend/
│   ├── src/
│   │   └── main/
│   │       ├── java/
│   │       └── resources/
│   ├── pom.xml
│   └── ...
├── frontend/
│   ├── src/
│   ├── public/
│   ├── package.json
│   └── ...
├── .gitignore
└── README.md
```

*The structure above is a simplified overview. Your actual folders and files may differ.*

## Getting Started

### Prerequisites

Install the following tools before running the project:

- Java Development Kit (JDK), compatible with the backend configuration
- Maven, or use the Maven Wrapper if included
- Node.js and npm
- MySQL Server
- Git
- A Gemini API key for features that use the Gemini API

### 1. Clone the Repository

```bash
git clone https://github.com/Santhiyashankar/prepwise-ai.git
cd prepwise-ai
```

### 2. Configure the Database

1. Start your MySQL server.
2. Create a database for PrepWise AI.
3. Configure the database URL, username, and password for your local environment.
4. Ensure the backend configuration matches your database settings.

Do not commit real database passwords or other sensitive credentials.

### 3. Configure Environment Variables

Configure the required environment variables locally before starting the backend.

For example:

```text
GEMINI_API_KEY=your_gemini_api_key
```

Replace the placeholder with your own API key. Do not publish actual API keys in this repository.

If the backend uses additional environment variables, configure those according to the application properties and backend code.

### 4. Run the Backend

Open a terminal and navigate to the backend directory:

```bash
cd backend
```

If the project includes the Maven Wrapper, use the appropriate command for your operating system.

**Windows:**

```powershell
.\mvnw.cmd spring-boot:run
```

**Linux/macOS:**

```bash
./mvnw spring-boot:run
```

Alternatively, if Maven is installed:

```bash
mvn spring-boot:run
```

Ensure the database and required environment variables are configured before starting the application.

### 5. Run the Frontend

Open a separate terminal from the project root:

```bash
cd frontend
npm install
npm run dev
```

Open the local URL displayed by Vite in your terminal, usually:

```text
http://localhost:5173
```

The port may differ if the default port is already in use.

### 6. Verify the Application

- Confirm that the frontend starts successfully.
- Confirm that the Spring Boot backend starts successfully.
- Verify the MySQL connection.
- Test the application's features and API integrations.

## Security

- Keep API keys and database credentials outside version control.
- Use environment variables or local configuration files for secrets.
- Do not upload personal resumes or other private user data.
- Never commit production credentials to a public repository.

## Learning Objectives

This project provides practical experience with:

- Full-stack web application development
- React.js frontend development
- Java and Spring Boot backend development
- REST API integration
- MySQL database connectivity
- JWT-based authentication
- AI API integration
- Interactive interview preparation features

## Future Improvements

Potential improvements include:

- Additional interview question categories
- More detailed performance analytics
- Enhanced personalized learning recommendations
- Expanded coding practice features
- Improved accessibility and user experience

## Author

**Santhiya Shankar**

GitHub: [@Santhiyashankar](https://github.com/Santhiyashankar)

---

*PrepWise AI – Practice, Learn, and Prepare for Your Next Interview.*
