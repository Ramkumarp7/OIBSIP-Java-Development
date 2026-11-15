# OASIS INFOBYTE — Online Examination System

Java Swing desktop assessment application for Oasis Infobyte Java Development, Task 4.
The refreshed interface uses FlatLaf and a Quizlet-inspired clean educational workspace.

## UI updates
- FlatLaf light/dark look and feel
- 220px navigation sidebar: Dashboard, Available Exams, My Results & Analytics, Profile & Security, Logout
- Greeting banner with current date and System Ready indicator
- Profile card with initials avatar, student ID, registered email, role and account status
- Exam metadata chips and difficulty label
- Search and category filters
- Quick-edit profile dialog with tabs for name, password and avatar color
- Pre-exam checklist
- Bottom-right toast notifications
- Royal-blue primary exam action

## Examination features
- Login, profile update, 10 Java MCQs, four radio options per question
- Previous/Next navigation, 30-minute timer, automatic and manual submission
- Close-window submission confirmation
- Result summary and answer review with explanations
- Logout from result screen

## Requirements
- JDK 17+
- Apache Maven 3.8+
- Internet connection on first build so Maven can download FlatLaf and Maven plugins

## Run
Windows: double-click `run.bat`
macOS/Linux: `./run.sh`

## Demo credentials
Username: `student`
Password: `oasis123`

## Project structure
```text
OIBSIP/
└── Java Development-Task 4-Online Examination System/
    ├── pom.xml
    ├── run.bat
    ├── run.sh
    ├── README.md
    └── src/main/java/com/oasis/exam/
```

## Notes
The student ID and registered email shown in the dashboard are sample display values for this demo.
Update these values in `DashboardFrame.java` if you have official candidate details to display.
