# Meeting Calendar Assistant

A Spring Boot application that serves as a meeting calendar assistant, allowing users to book meetings, find available time slots, and check for meeting conflicts.

## Features

- Book meetings with calendar owners
- Find available time slots between two employees
- Check for meeting conflicts among participants
- RESTful API endpoints
- Input validation and error handling
- In-memory H2 database for easy testing

## Prerequisites

- Java 21 or higher
- Maven 3.6 or higher
- Postman (for API testing)

## Setup Instructions

1. **Clone the repository**
   ```bash
   git clone https://github.com/Somtrip/meeting-assistance.git
   cd meeting-assistance
   ```

2. **Build the project**
   ```bash
   mvn clean install
   ```

3. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

The application will start on `http://localhost:8080`

## Database Access

The application uses an H2 in-memory database. After starting the application, you can access the H2 console at:
- URL: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:caldb`
- Username: `sa`
- Password: (leave empty)


### Insert Employee Data

After connecting, you’ll see a SQL editor.

Run this SQL to insert employees:

```sql
INSERT INTO employee (id, email, name)
VALUES ('4062fcfd-feeb-46d9-a5c9-c9d3475da470', 'alice@example.com', 'Alice');

INSERT INTO employee (id, email, name)
VALUES ('3ca4abfc-8d7f-4485-898b-46a782385d5f', 'bob@example.com', 'Bob');

INSERT INTO employee (id, email, name)
VALUES ('25a821dd-d674-4221-8c27-ae57a8928e98', 'carol@example.com', 'Carol');

```

 Verify

Run:
```sql
SELECT * FROM employee;

```

You should see rows for Alice, Bob, and Carol with their UUIDs.

## API Endpoints

### 1. Book a Meeting

**Endpoint:** `POST /api/meetings/book`

**Request Body:**
```json
{
  "title": "Design Sync",
  "organizerId": "4062fcfd-feeb-46d9-a5c9-c9d3475da470",
  "start": "2025-08-20T10:00:00",
  "end": "2025-08-20T10:30:00",
  "participantIds": ["3ca4abfc-8d7f-4485-898b-46a782385d5f", "25a821dd-d674-4221-8c27-ae57a8928e98"]
}

```

**Response:**
```json
{
    "meetingId": "8f81cf48-8aa9-4dc3-802a-dfac66c7daae",
    "status": "BOOKED"
}
```

### 2. Find Available Slots

**Endpoint:** `GET /api/slots?employee1Id={id1}&employee2Id={id2}&durationInMinutes={minutes}`

**Example:**
```html

GET http://localhost:8080/api/slots?employee1Id=4062fcfd-feeb-46d9-a5c9-c9d3475da470&employee2Id=3ca4abfc-8d7f-4485-898b-46a782385d5f&durationInMinutes=30

```

**Response:**
```json
{
    "slots": [
        {
            "start": "2025-08-22T09:00:00",
            "end": "2025-08-22T17:00:00"
        },
        {
            "start": "2025-08-25T09:00:00",
            "end": "2025-08-25T17:00:00"
        },
        {
            "start": "2025-08-26T09:00:00",
            "end": "2025-08-26T17:00:00"
        },
        {
            "start": "2025-08-27T09:00:00",
            "end": "2025-08-27T17:00:00"
        },
        {
            "start": "2025-08-28T09:00:00",
            "end": "2025-08-28T17:00:00"
        }
    ]
}
```

### 3. Check Meeting Conflicts

**Endpoint:** `POST /api/meetings/conflicts`

**Request Body:**
```json
{
  "title": "Weekly Planning",
  "organizerId": "4062fcfd-feeb-46d9-a5c9-c9d3475da470",
  "start": "2025-08-20T10:00:00",
  "end": "2025-08-20T10:45:00",
  "participantIds": ["3ca4abfc-8d7f-4485-898b-46a782385d5f", "25a821dd-d674-4221-8c27-ae57a8928e98"]
}

```

**Response:**
```json
{
    "conflictedParticipantIds": [
        "3ca4abfc-8d7f-4485-898b-46a782385d5f",
        "25a821dd-d674-4221-8c27-ae57a8928e98"
    ]
}
```


