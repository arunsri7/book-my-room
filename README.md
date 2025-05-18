# Book My Room

A booking system that allows companies to manage and book various resources like conference rooms, equipment, and other facilities.

## Features

- Multi-tenant architecture with company-based isolation
- Role-based access control (SUPER_ADMIN, COMPANY_ADMIN, USER)
- Resource management (conference rooms, equipment, etc.)
- Booking management with conflict detection
- JWT-based authentication
- MongoDB for data persistence


### Key Entities

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

1. **Prerequisites**
   - Java 17 or higher
   - MongoDB
   - Maven

2. **Database Setup**
   ```bash
   # Start MongoDB
   mongod --dbpath /path/to/data/db
   ```

3. **Application Setup**
   ```bash
   # Clone the repository
   git clone https://github.com/yourusername/book-my-room.git
   
   # Navigate to project directory
   cd book-my-room
   
   # run the docker daemon or colima


   run docker-compose up --build
   ```

## Future Improvements


0. Change the name of the application. Should be something like book-my-resources

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