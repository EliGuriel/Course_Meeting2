# מדריך מאוחד - הנדסת תוכנה ו-Spring Boot

<div dir="rtl">

## תוכן עניינים

1. [מבוא להנדסת תוכנה](#מבוא-להנדסת-תוכנה)
2. [עקרונות יסוד](#עקרונות-יסוד)
3. [מחזור חיי פיתוח תוכנה (SDLC)](#מחזור-חיי-פיתוח-תוכנה-sdlc)
4. [ארכיטקטורת תוכנה](#ארכיטקטורת-תוכנה)
5. [עקרונות SOLID](#עקרונות-solid)
6. [Design Patterns](#design-patterns)
7. [Spring Boot ו-JPA](#spring-boot-ו-jpa)
8. [יחסי גומלין ב-JPA](#יחסי-גומלין-ב-jpa)
9. [אופטימיזציה וביצועים](#אופטימיזציה-וביצועים)
10. [CI/CD ו-DevOps](#cicd-ו-devops)
11. [טיפול בשגיאות](#טיפול-בשגיאות)
12. [מגמות עתידיות](#מגמות-עתידיות)

---

## מבוא להנדסת תוכנה

הנדסת תוכנה היא תחום רב-תחומי המשלב בין מדעי המחשב, ניהול פרויקטים, ועקרונות הנדסיים. המטרה המרכזית היא יצירת תוכנה איכותית, אמינה וברת-תחזוקה תוך עמידה באילוצי זמן ותקציב.

### הגדרה פורמלית
הנדסת תוכנה היא היישום השיטתי, הממושמע והניתן לכימות של גישות לפיתוח, תפעול ותחזוקה של תוכנה.

## עקרונות יסוד

### עקרונות מנחים
1. **מודולריות** - חלוקה ליחידות עצמאיות
2. **הפשטה** - הסתרת פרטי מימוש
3. **אנקפסולציה** - הגנה על נתונים פנימיים
4. **שימוש חוזר** - כתיבת קוד גנרי
5. **תחזוקתיות** - קוד קריא ומתועד

## מחזור חיי פיתוח תוכנה (SDLC)

</div>

```mermaid
graph TD
    A[Requirements Analysis] --> B[System Design]
    B --> C[Implementation]
    C --> D[Testing]
    D --> E[Deployment]
    E --> F[Maintenance]
    F --> A
    
    style A fill:#222,stroke:#333,stroke-width:2px
    style B fill:#222,stroke:#333,stroke-width:2px
    style C fill:#222,stroke:#333,stroke-width:2px
    style D fill:#222,stroke:#333,stroke-width:2px
    style E fill:#333,stroke:#333,stroke-width:2px
    style F fill:#232,stroke:#333,stroke-width:2px
```

<div dir="rtl">

### שלבי פיתוח מפורטים

#### 1. ניתוח דרישות (Requirements Analysis)
- איסוף דרישות מהלקוח
- הגדרת דרישות פונקציונליות ולא-פונקציונליות
- יצירת מסמך SRS (Software Requirements Specification)
- אישור דרישות עם בעלי העניין

#### 2. תכנון מערכת (System Design)
- ארכיטקטורת מערכת
- תכנון מסד נתונים
- תכנון ממשקי משתמש
- בחירת טכנולוגיות

#### 3. מימוש (Implementation)
- כתיבת קוד לפי התכנון
- שימוש בעקרונות תכנות מונחה עצמים
- ניהול גרסאות עם Git
- Code Reviews

#### 4. בדיקות (Testing)

</div>

```mermaid
graph BT
    UT[Unit Testing]
    IT[Integration Testing]
    ST[System Testing]
    AT[Acceptance Testing]
    
    UT --> IT
    IT --> ST
    ST --> AT
    
    UT:::unitStyle
    IT:::integrationStyle
    ST:::systemStyle
    AT:::acceptanceStyle
    
%%    classDef unitStyle fill:#90EE90,stroke:#333,stroke-width:2px
%%    classDef integrationStyle fill:#87CEEB,stroke:#333,stroke-width:2px
%%    classDef systemStyle fill:#DDA0DD,stroke:#333,stroke-width:2px
%%    classDef acceptanceStyle fill:#F0E68C,stroke:#333,stroke-width:2px
```

<div dir="rtl">

## ארכיטקטורת תוכנה

### Software Architecture Patterns

</div>

```mermaid
graph TB
    subgraph "Layered Architecture"
        L1[Presentation Layer]
        L2[Business Logic Layer]
        L3[Data Access Layer]
        L4[Database Layer]
        L1 --> L2
        L2 --> L3
        L3 --> L4
    end
    
    subgraph "Microservices"
        M1[Service A]
        M2[Service B]
        M3[Service C]
        GW[API Gateway]
        GW --> M1
        GW --> M2
        GW --> M3
    end
    
    subgraph "MVC Pattern"
        V[View]
        C[Controller]
        M[Model]
        V <--> C
        C <--> M
    end
```

<div dir="rtl">

## עקרונות SOLID

### S - Single Responsibility Principle
כל מחלקה צריכה להיות אחראית על דבר אחד בלבד

</div>

```java
// UserController - אחראי רק על HTTP handling
@RestController
@RequestMapping("/api/users")
public class UserController {
    // רק מטפל ב-HTTP requests/responses
    // לא מכיל business logic
}

// UserService - אחראי רק על business logic
public interface UserService {
    UserDto createUser(UserDto userDto);
    UserDto updateUser(Long id, UserDto userDto);
    // רק לוגיקה עסקית, לא HTTP או DB
}

// UserRepository - אחראי רק על גישה לנתונים
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // רק queries ל-DB
}
```

<div dir="rtl">

### O - Open/Closed Principle
פתוח להרחבה, סגור לשינוי

</div>

```java
// JpaRepository מאפשר הרחבה ללא שינוי הקוד הקיים
public interface UserRepository extends JpaRepository<User, Long> {
    // אפשר להוסיף queries חדשות בלי לשנות את JpaRepository
    Optional<User> findByUsername(String username);
    List<User> findByActive(boolean active);
    
    // Custom queries - הרחבה ללא שינוי
    @Query("SELECT u FROM User u WHERE LOWER(u.username) LIKE %:name%")
    List<User> searchByName(@Param("name") String name);
}
```

<div dir="rtl">

### L - Liskov Substitution Principle
תת-מחלקות חייבות להיות תחליף הולם למחלקת-על

</div>

```java
// UserController משתמש ב-interface, לא ב-implementation
public class UserController {
    private final UserService userService; // interface
    
    @Autowired
    public UserController(UserService userService) {
        // אפשר להחליף את UserServiceImpl באימפלמנטציה אחרת
        this.userService = userService;
    }
}
```

<div dir="rtl">

### I - Interface Segregation Principle
ממשקים ספציפיים עדיפים על ממשק כללי אחד

</div>

```java
// במקום ממשק ענק אחד, יש הפרדה לממשקים ממוקדים:

// UserService - רק פעולות משתמשים
public interface UserService {
    List<UserDto> getAllUsers();
    UserDto createUser(UserDto userDto);
    UserDto assignRoleToUser(Long userId, Long roleId);
}

// RoleService - רק פעולות תפקידים
public interface RoleService {
    List<RoleBasicDto> getAllRoles();
    RoleDto createRole(RoleDto roleDto);
    boolean hasUsers(Long roleId);
}
```

<div dir="rtl">

### D - Dependency Inversion Principle
תלות בהפשטות, לא במימושים קונקרטיים

</div>

```java
// UserServiceImpl תלוי ב-interfaces, לא ב-implementations
@Service
public class UserServiceImpl implements UserService {
    // תלות ב-interfaces
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    
    @Autowired
    public UserServiceImpl(UserRepository userRepository,  // interface
                          RoleRepository roleRepository,   // interface
                          UserMapper userMapper) {         // concrete אבל @Component
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userMapper = userMapper;
    }
}
```

<div dir="rtl">

## Design Patterns

</div>

```mermaid
graph TD
    DP[Design Patterns]
    CR[Creational]
    ST[Structural]
    BH[Behavioral]
    
    DP --> CR
    DP --> ST
    DP --> BH
    
    CR --> S[Singleton]
    CR --> F[Factory]
    CR --> B[Builder]
    
    ST --> A[Adapter]
    ST --> D[Decorator]
    ST --> P[Proxy]
    
    BH --> O[Observer]
    BH --> STR[Strategy]
    BH --> I[Iterator]
```

<div dir="rtl">

### Design Patterns בקוד

#### DTO Pattern
הפרדה בין Domain Objects לבין Transfer Objects:

</div>

```java
// Entity - ייצוג במסד הנתונים
@Entity
public class User {
    private String password; // רגיש
    private Set<Role> roles; // יחסים מורכבים
}

// DTO - ייצוג לתקשורת
public class UserDto {
    // ללא password
    private Set<Long> roleIds; // רק IDs
}
```

<div dir="rtl">

#### Repository Pattern
הפשטה של גישה לנתונים:

</div>

```java
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    
    @Query("SELECT u FROM User u WHERE u.active = :active")
    List<User> findByActive(@Param("active") boolean active);
}
```

<div dir="rtl">

#### Builder Pattern

</div>

```java
@Builder
public class UserDto {
    // מאפשר יצירת אובייקטים בצורה נקייה
    UserDto user = UserDto.builder()
        .username("john")
        .email("john@example.com")
        .active(true)
        .build();
}
```

<div dir="rtl">

## Spring Boot ו-JPA

### מהו ORM?

ORM (Object-Relational Mapping) הוא פרדיגמת תכנות המאפשרת המרה בין מערכות שונות של ייצוג נתונים - בעיקר בין מודלים אובייקטיביים בשפת התכנות (כמו Java) למודל טבלאי של בסיס נתונים יחסי.

### יתרונות ORM:

1. **הפשטה מעל דיאלקטים שונים של SQL**
2. **אבטחה מובנית** - הגנה אוטומטית מפני SQL Injection
3. **ניהול מטמון (Caching)**
4. **ניהול קשרים (Relationships)**
5. **ניהול סכמה וגרסאות**
6. **ולידציה ברמת האובייקט**
7. **ניהול טרנזקציות**
8. **טעינה עצלה לעומת להוטה**

### היררכיית הטכנולוגיות

</div>

```mermaid
graph TD
    A["Spring Data JPA"] -->|"מעשיר ומפשט"| B["JPA"]
    B -->|"מוגדר על ידי"| C["Java Persistence API Specification"]
    B -->|"ממומש על ידי"| D["Hibernate"]
    D -->|"משתמש ב"| E["JDBC"]
    E -->|"מתקשר עם"| F["Database"]

    A -->|"מציע"| A1["Repository Interfaces"]
    A -->|"תומך ב"| A2["Query Methods"]
    A -->|"מאפשר"| A3["Pagination & Sorting"]
    
    B -->|"מגדיר"| B1["Entity Lifecycle"]
    B -->|"מציע"| B2["JPQL"]
    B -->|"מספק"| B3["Java Annotations"]
    
    D -->|"מספק"| D1["ORM Implementation"]
    D -->|"מממש"| D2["Caching"]
    D -->|"מציע"| D3["Dialect Handling"]
    
    classDef springData fill:#6DB33F,color:white,stroke-width:2px,stroke:#333;
    classDef jpa fill:#F89406,color:white,stroke-width:2px,stroke:#333;
    classDef hibernate fill:#59666C,color:white,stroke-width:2px,stroke:#333;
    classDef jdbc fill:#1E88E5,color:white,stroke-width:2px,stroke:#333;
    classDef db fill:#455A64,color:white,stroke-width:2px,stroke:#333;
    
    class A,A1,A2,A3 springData;
    class B,B1,B2,B3 jpa;
    class D,D1,D2,D3 hibernate;
    class E jdbc;
    class F db;
```

<div dir="rtl">

### Spring Data JPA - השכבה העליונה

**Spring Data JPA** היא הרמה הגבוהה ביותר - ספריית Spring שמפשטת עוד יותר את העבודה עם JPA:

#### 1. Repository Interfaces

</div>

```java
// במקום לכתוב קוד SQL או JPQL, פשוט מגדירים interface
public interface UserRepository extends JpaRepository<User, Long> {
    // Spring Data JPA יוצר את המימוש אוטומטית!
    Optional<User> findByUsername(String username);
    List<User> findByActiveTrue();
}
```

<div dir="rtl">

#### 2. Query Methods

</div>

```java
// Query methods - שמות מתודות שהופכות לשאילתות
List<User> findByEmailContaining(String email);
List<User> findByAgeBetween(int min, int max);
List<User> findTop10ByOrderByCreatedAtDesc();

// או שאילתות מותאמות אישית
@Query("SELECT u FROM User u WHERE u.roles.size > :count")
List<User> findUsersWithManyRoles(@Param("count") int count);
```

<div dir="rtl">

#### 3. Pagination & Sorting

</div>

```java
// תמיכה מובנית בעימוד ומיון
Page<User> findByActive(boolean active, Pageable pageable);

// שימוש:
PageRequest pageRequest = PageRequest.of(0, 20, Sort.by("username"));
Page<User> users = userRepository.findByActive(true, pageRequest);
```

<div dir="rtl">

### קונפיגורציה של Spring Boot

</div>

```properties
# MySQL Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/school_db?useSSL=false&serverTimezone=UTC
spring.datasource.username=school_user
spring.datasource.password=school_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

# Connection Pool Configuration
spring.datasource.hikari.connection-timeout=20000
spring.datasource.hikari.maximum-pool-size=5
```

<div dir="rtl">

## יחסי גומלין ב-JPA

### יחסי One-to-One

יחסי One-to-One מתקיימים כאשר לכל ישות מצד אחד של היחס יש בדיוק ישות אחת מהצד השני.

#### דו-כיווני (Bidirectional)

</div>

```java
@Entity
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    private String department;
    
    @OneToOne
    @JoinColumn(name = "details_id")
    private TeacherDetails details;
}

@Entity
public class TeacherDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String email;
    private String phone;
    
    @OneToOne(mappedBy = "details")
    private Teacher teacher;
}
```

<div dir="rtl">

### יחסי One-to-Many

יחסים אלה מתקיימים כאשר ישות אחת קשורה למספר ישויות מהצד השני.

#### דו-כיווני

</div>

```java
@Entity
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    
    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL)
    private List<Course> courses = new ArrayList<>();
    
    // Helper methods for managing relationships
    public void addCourse(Course course) {
        courses.add(course);
        course.setTeacher(this);
    }
}

@Entity
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String title;
    private String code;
    
    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;
}
```

<div dir="rtl">

### יחסי Many-to-Many

יחסי Many-to-Many מתקיימים כאשר ישויות מצד אחד יכולות להיות קשורות למספר ישויות מהצד השני, ולהיפך.

#### דו-כיווני

</div>

```java
@Entity
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String firstName;
    private String lastName;
    
    @ManyToMany
    @JoinTable(
        name = "student_course",
        joinColumns = @JoinColumn(name = "student_id"),
        inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private Set<Course> courses = new HashSet<>();
    
    // Helper methods
    public void enrollInCourse(Course course) {
        courses.add(course);
        course.getStudents().add(this);
    }
}

@Entity
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String title;
    
    @ManyToMany(mappedBy = "courses")
    private Set<Student> students = new HashSet<>();
}
```

<div dir="rtl">

#### Many-to-Many עם ישות קשר

</div>

```java
@Entity
public class Enrollment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;
    
    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;
    
    private LocalDate enrollmentDate;
    private Double grade;
    
    @Enumerated(EnumType.STRING)
    private EnrollmentStatus status;
}
```

<div dir="rtl">

## אופטימיזציה וביצועים

### בעיית N+1

בעיית N+1 היא בעיית ביצועים נפוצה בתחום מיפוי אובייקטים בבסיסי נתונים (ORM). הבעיה מתרחשת כאשר אפליקציה מבצעת שאילתת בסיס נתונים אחת כדי לאחזר רשימה של N פריטים, ואז מבצעת שאילתה נוספת עבור כל פריט.

#### פתרונות לבעיית N+1

##### 1. שימוש ב-JOIN FETCH

</div>

```java
@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    @Query("SELECT t FROM Teacher t LEFT JOIN FETCH t.students")
    List<Teacher> findAllWithStudents();
    
    @Query("SELECT t FROM Teacher t LEFT JOIN FETCH t.students s LEFT JOIN FETCH s.details")
    List<Teacher> findAllWithStudentsAndDetails();
}
```

<div dir="rtl">

##### 2. שימוש ב-EntityGraph

</div>

```java
@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    @EntityGraph(attributePaths = {"students", "students.details"})
    List<Teacher> findAll();
}
```

<div dir="rtl">

##### 3. שימוש ב-Batch Fetching

</div>

```java
@Entity
public class Teacher {
    @OneToMany(mappedBy = "teacher")
    @BatchSize(size = 30) // טעינת 30 תלמידים בבת אחת
    private List<Student> students;
}
```

<div dir="rtl">

### Lazy לעומת Eager Loading

JPA מציע שני מצבי טעינה עבור יחסים בין ישויות:

#### ברירות המחדל של JPA:
- `@ManyToOne, @OneToOne`: `FetchType.EAGER` (טעינה להוטה)
- `@OneToMany, @ManyToMany`: `FetchType.LAZY` (טעינה עצלה)

</div>

```java
@Entity
public class Student {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "details_id")
    private StudentDetails details;
}
```

<div dir="rtl">

## ניהול עסקאות ב-JPA

### עקרונות ACID

עסקאות בבסיסי נתונים נדרשות לעמוד בארבע תכונות בסיסיות:

1. **אטומיות (Atomicity)** - עסקה מתבצעת במלואה או לא מתבצעת כלל
2. **עקביות (Consistency)** - עסקה מעבירה את בסיס הנתונים ממצב תקין למצב תקין
3. **בידוד (Isolation)** - עסקאות מבודדות זו מזו
4. **עמידות (Durability)** - תוצאות העסקה נשמרות לתמיד

### שימוש ב-@Transactional

</div>

```java
@Service
public class UserService {
    @Transactional(
        propagation = Propagation.REQUIRED,
        isolation = Isolation.READ_COMMITTED,
        timeout = 30,
        readOnly = false,
        rollbackFor = {ResourceNotFoundException.class}
    )
    public UserDto createUser(UserDto userDto) {
        // כל הפעולות בתוך טרנזקציה אחת
        // אם משהו נכשל - הכל חוזר אחורה
        return userRepository.save(user);
    }
}
```

<div dir="rtl">

## CI/CD ו-DevOps

### מהו CI/CD?

CI/CD הוא תהליך אוטומטי המאפשר לצוותי פיתוח לשחרר קוד באופן מהיר ואמין:
- **CI (Continuous Integration)** - אינטגרציה רציפה של קוד
- **CD (Continuous Delivery/Deployment)** - אספקה/פריסה רציפה

### Pipeline Stages

</div>

```mermaid
graph TB
    subgraph "CI Pipeline"
        A[Code Commit] --> B[Build]
        B --> C[Unit Tests]
        C --> D[Integration Tests]
        D --> E[Code Analysis]
        E --> F[Package]
    end
    
    subgraph "CD Pipeline"
        F --> G[Deploy to Staging]
        G --> H[Acceptance Tests]
        H --> I[Deploy to Production]
        I --> J[Health Checks]
        J --> K[Monitoring]
    end
    
%%    style A fill:#e3f2fd
%%    style B fill:#bbdefb
%%    style C fill:#90caf9
%%    style D fill:#64b5f6
%%    style E fill:#42a5f5
%%    style F fill:#2196f3
%%    style G fill:#1e88e5
%%    style H fill:#1976d2
%%    style I fill:#1565c0
%%    style J fill:#0d47a1
%%    style K fill:#0a3d91
```

<div dir="rtl">

### GitHub Actions Example

</div>

```yaml
name: CI/CD Pipeline

on:
  push:
    branches: [ main, develop ]
  pull_request:
    branches: [ main ]

jobs:
  test:
    runs-on: ubuntu-latest
    
    steps:
    - uses: actions/checkout@v3
    
    - name: Set up JDK 17
      uses: actions/setup-java@v3
      with:
        java-version: '17'
        
    - name: Run tests
      run: ./mvnw test
      
    - name: Generate coverage report
      run: ./mvnw jacoco:report
      
  build:
    needs: test
    runs-on: ubuntu-latest
    
    steps:
    - uses: actions/checkout@v3
    
    - name: Build application
      run: ./mvnw clean package
      
    - name: Build Docker image
      run: docker build -t myapp:${{ github.sha }} .
```

<div dir="rtl">

### Dockerfile לאפליקציית Spring Boot

</div>

```dockerfile
# Multi-stage build
FROM maven:3.8-openjdk-17 AS builder
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

# Runtime stage
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 8080
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s \
  CMD curl -f http://localhost:8080/actuator/health || exit 1
ENTRYPOINT ["java", "-jar", "app.jar"]
```

<div dir="rtl">

## טיפול בשגיאות

### Exception Handling Pattern

</div>

```mermaid
graph TD
    E[Exception] --> GEH[GlobalExceptionHandler]
    
    GEH --> RNF[ResourceNotFoundException]
    GEH --> RAE[ResourceAlreadyExistsException]
    GEH --> IRE[InvalidRequestException]
    GEH --> VAL[ValidationException]
    
    RNF --> R404[404 Response]
    RAE --> R409[409 Response]
    IRE --> R400[400 Response]
    VAL --> R400V[400 with Details]
```

<div dir="rtl">

### GlobalExceptionHandler

</div>

```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<StandardResponse> handleNotFound(
            ResourceNotFoundException ex) {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(new StandardResponse("error", null, error));
    }
}
```

<div dir="rtl">

### תגובה אחידה (Consistent Response)

</div>

```java
public class StandardResponse {
    private String status;  // success/error
    private Object data;    // התוצאה
    private Object error;   // פרטי שגיאה
    private LocalDateTime timestamp;
}
```

<div dir="rtl">

### תיקוף נתונים (Validation)

</div>

```java
public class UserDto {
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 30)
    private String username;
    
    @Email(message = "Email should be valid")
    private String email;
    
    @Pattern(regexp = "^\\+?(972|0)([23459]\\d{7}|5\\d{8})$")
    private String phoneNumber;
}
```

<div dir="rtl">

## עקרונות Clean Code

### שמות ברורים ומשמעותיים

</div>

```java
// במקום: getUsersByRId(Long r)
public List<UserDto> getUsersByRoleId(Long roleId)

// במקום: check(Long id)
public boolean hasUsers(Long roleId)
```

<div dir="rtl">

### מתודות קטנות וממוקדות

</div>

```java
@Transactional
public UserDto assignRoleToUser(Long userId, Long roleId) {
    User user = findUserOrThrow(userId);
    Role role = findRoleOrThrow(roleId);
    validateRoleNotAssigned(user, role);
    user.addRole(role);
    return userMapper.toDto(userRepository.save(user));
}
```

<div dir="rtl">

### Defensive Programming

</div>

```java
public void updateEntityFromDto(User user, UserDto userDto) {
    if (userDto == null) {
        return; // הגנה מפני null
    }
    
    if (userDto.getUsername() != null) {
        user.setUsername(userDto.getUsername());
    }
    // בדיקה לפני כל עדכון
}
```

<div dir="rtl">

### Separation of Concerns

</div>

```java
// Controller - רק HTTP concerns
@GetMapping("/{id}")
public ResponseEntity<StandardResponse> getUserById(@PathVariable Long id) {
    UserDto user = userService.getUserById(id);
    return ResponseEntity.ok(new StandardResponse("success", user, null));
}

// Service - רק business logic
public UserDto getUserById(Long id) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    return userMapper.toDto(user);
}
```

<div dir="rtl">

### Security Considerations

</div>

```java
// הסתרת מידע רגיש ב-DTO
public UserDto toDto(User user) {
    UserDto userDto = UserDto.builder()
        .id(user.getId())
        .username(user.getUsername())
        .email(user.getEmail())
        // ללא password! - אבטחת מידע
        .build();
    return userDto;
}
```

<div dir="rtl">

## Testing Strategy

</div>

```mermaid
graph TD
    UT[Unit Tests] --> ST[Service Tests]
    IT[Integration Tests] --> RT[Repository Tests]
    E2E[End-to-End Tests] --> CT[Controller Tests]
    
    ST --> M[Mocking]
    RT --> TD[Test Database]
    CT --> MVC[MockMvc]
    
    All[All Tests] --> CI[CI Pipeline]
```

<div dir="rtl">

### דוגמה לבדיקת יחידה

</div>

```java
@Test
void testCreateUser_Success() {
    // Given
    UserDto inputDto = createValidUserDto();
    User savedUser = createUser();
    when(userRepository.save(any())).thenReturn(savedUser);
    
    // When
    UserDto result = userService.createUser(inputDto);
    
    // Then
    assertNotNull(result);
    assertEquals(inputDto.getUsername(), result.getUsername());
    verify(userRepository).save(any());
}
```

<div dir="rtl">

## Domain vs Application

### Domain (דומיין/תחום)
**Domain** מתייחס ל**תחום העסקי** או **הבעיה העסקית** שהתוכנה באה לפתור:

- **Entities של הדומיין**: User, Role, UserDetails
- **כללים עסקיים**: משתמש חייב email ייחודי, תפקיד לא ניתן למחיקה אם יש משתמשים

### Application (אפליקציה)
**Application** היא **המימוש הטכני** שפותר את בעיות הדומיין:

- קוד (Controllers, Services, Repositories)
- טכנולוגיות (Spring Boot, JPA, REST API)
- תשתיות (Database, Servers)

### Domain-Driven Design (DDD)

</div>

```java
// Domain Layer - הלוגיקה העסקית הטהורה
@Entity
public class User {
    // Domain rules
    public void assignRole(Role role) {
        if (roles.contains(role)) {
            throw new BusinessException("Role already assigned");
        }
        roles.add(role);
    }
}

// Application Layer - תיאום בין שכבות
@Service
public class UserService {
    // Application logic - משתמש ב-domain entities
    public UserDto assignRoleToUser(Long userId, Long roleId) {
        User user = repository.findById(userId);
        Role role = roleRepository.findById(roleId);
        user.assignRole(role); // Domain logic
        return mapper.toDto(repository.save(user));
    }
}
```

<div dir="rtl">

## מגמות עתידיות

</div>

```mermaid
timeline
    title Software Engineering Evolution
    
    2020 : Cloud-Native Adoption
         : DevOps Mainstream
    
    2022 : AI-Assisted Coding
         : Low-Code Platforms
    
    2024 : Quantum Computing Preparation
         : Edge Computing
    
    2026 : Full AI Integration
         : Autonomous Systems
```

<div dir="rtl">

### 1. AI ו-Machine Learning בפיתוח
- כלי פיתוח חכמים
- אוטומציה של משימות
- ניתוח קוד אוטומטי

### 2. Low-Code/No-Code
- פלטפורמות פיתוח ויזואליות
- האצת פיתוח
- דמוקרטיזציה של פיתוח תוכנה

### 3. Cloud-Native Development
- Serverless Architecture
- Containerization
- Microservices

### 4. DevSecOps
- אבטחה כחלק אינטגרלי מהפיתוח
- Shift-Left Testing
- Continuous Security

## סיכום

הנדסת תוכנה היא תחום דינמי ומתפתח המשלב בין טכנולוגיה, מתודולוגיה וניהול. ההצלחה בתחום דורשת:

1. **ידע טכני רחב** - שליטה בשפות תכנות, כלים וטכנולוגיות
2. **חשיבה מערכתית** - יכולת לראות את התמונה הרחבה
3. **כישורים רכים** - תקשורת, עבודת צוות, פתרון בעיות
4. **למידה מתמדת** - התעדכנות בטכנולוגיות ומתודולוגיות חדשות

המדריך המאוחד הזה מספק בסיס איתן להבנת עקרונות הנדסת תוכנה ויישומם המעשי ב-Spring Boot ו-JPA, עם דגש על:
- **ארכיטקטורה נקייה** עם הפרדת שכבות ברורה
- **SOLID principles** בכל רמה של המערכת
- **Design patterns** מתאימים לבעיות ספציפיות
- **קוד נקי ותחזוקתי** עם שמות ברורים ומתודות ממוקדות
- **טיפול מקיף בשגיאות** עם תגובות עקביות
- **אבטחה** בהסתרת מידע רגיש
- **תמיכה ב-CI/CD** עם מבנה פרויקט סטנדרטי

התחום ימשיך להתפתח עם אתגרים חדשים והזדמנויות מרגשות, כאשר הדגש על איכות, יעילות וחדשנות נשאר קבוע.

</div>