# Book My Room

A booking system that allows companies to manage and book various resources like conference rooms, equipment, and other facilities.

## Features

- Multi-tenant architecture with company-based isolation
- Role-based access control (SUPER_ADMIN, COMPANY_ADMIN, USER)
- Resource management (conference rooms, equipment, etc.)
- Booking management with conflict detection
- JWT-based authentication
- MongoDB for data persistence

## PostmanCollection
 https://github.com/arunsri7/book-my-room/blob/main/BookMyRoom.postman_collection.json

## Key Entities

1. **Company**
   - Root entity that owns resources and users
   - Contains company details (name, address, contact info)
   - Has multiple offices

2. **User**
   - Belongs to a company
   - Has a specific role (SUPER_ADMIN, COMPANY_ADMIN, USER)

3. **Resource**
   - Belongs to a company and offices
   - Has a type (CONFERENCE_ROOM, EQUIPMENT, etc.)
   - Contains availability and capacity information

4. **Booking**
   - Links a user to a resource
   - Contains time slot information
   - Includes booking purpose and status

## Technical Stack

- **Backend**: Spring Boot
- **Database**: MongoDB
- **Authentication**: JWT
- **Build Tool**: Maven
- **Java Version**: 17

## Setup Instructions
```
1. Prerequisites
✅ Docker (or Colima if you're on macOS)

2. Setup Steps
# Clone the repository
git clone https://github.com/yourusername/book-my-room.git

# Navigate to the project directory
cd book-my-room

# Start the Docker daemon (or ensure Colima is running)
# Then spin up the application
docker-compose up --build
```

## Future Improvements


0. Change the name of the application. Should be something like book-my-resources :P

1. **API Enhancements**
   - Add better documentation
   - Implement API versioning
   - Add request validation
   - Implement rate limiting

2. **Security Enhancements**
   - Implement refresh tokens
   - Add password reset functionality
   - Add email verification

3. **Feature Additions**
   - Recurring bookings
   - Booking approval workflow
   - Notification system
   - Calendar integration
   - Resource availability calendar
   - Return Available slots
   - Store and retrieve images for the resources 

4. **Performance Optimizations**
   - Add caching

5. **Monitoring and Maintenance**
   - Add application metrics
   - Implement proper logging
   - Add performance monitoring
   - Implement error tracking

6. **Testing**
   - Add unit tests
   - Add integration tests

## Flow
![diagram](https://github.com/user-attachments/assets/f840116b-9450-4f07-a20a-9f3587f1b9b7)
You can find a more interactive diagram here -> https://gitdiagram.com/arunsri7/book-my-room
