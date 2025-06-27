# מצגת הנדסת תוכנה ו-Spring Boot

<div dir="rtl">

## תוכן עניינים

- מבוא להנדסת תוכנה
- עקרונות יסוד
- מחזור חיי פיתוח תוכנה (SDLC)
- ארכיטקטורת תוכנה
- עקרונות SOLID
- Design Patterns
- Spring Boot ו-JPA
- יחסי גומלין ב-JPA
- אופטימיזציה וביצועים
- ניהול עסקאות
- CI/CD ו-DevOps
- טיפול בשגיאות
- עקרונות Clean Code
- Testing Strategy
- Domain vs Application
- מגמות עתידיות

---

## מבוא להנדסת תוכנה

### הגדרה
היישום השיטתי, הממושמע והניתן לכימות של גישות לפיתוח, תפעול ותחזוקה של תוכנה

### מטרות
- תוכנה איכותית
- אמינה וברת-תחזוקה
- עמידה באילוצי זמן ותקציב

---

## עקרונות יסוד

- **מודולריות** - חלוקה ליחידות עצמאיות
- **הפשטה** - הסתרת פרטי מימוש
- **אנקפסולציה** - הגנה על נתונים פנימיים
- **שימוש חוזר** - כתיבת קוד גנרי
- **תחזוקתיות** - קוד קריא ומתועד

---

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

---

## שלבי SDLC

### 1. ניתוח דרישות
- איסוף דרישות מהלקוח
- דרישות פונקציונליות ולא-פונקציונליות
- מסמך SRS
- אישור עם בעלי עניין

### 2. תכנון מערכת
- ארכיטקטורת מערכת
- תכנון מסד נתונים
- ממשקי משתמש
- בחירת טכנולוגיות

---

## שלבי SDLC (המשך)

### 3. מימוש
- כתיבת קוד
- עקרונות OOP
- ניהול גרסאות
- Code Reviews

### 4. בדיקות
- Unit Testing
- Integration Testing
- System Testing
- Acceptance Testing

---

## היררכיית בדיקות

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
    
    classDef unitStyle fill:#90EE90,stroke:#333,stroke-width:2px
    classDef integrationStyle fill:#87CEEB,stroke:#333,stroke-width:2px
    classDef systemStyle fill:#DDA0DD,stroke:#333,stroke-width:2px
    classDef acceptanceStyle fill:#F0E68C,stroke:#333,stroke-width:2px
```

<div dir="rtl">

---

## ארכיטקטורת תוכנה

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

---

## עקרונות SOLID

### S - Single Responsibility Principle
כל מחלקה אחראית על דבר אחד בלבד

### O - Open/Closed Principle
פתוח להרחבה, סגור לשינוי

### L - Liskov Substitution Principle
תת-מחלקות כתחליף למחלקת-על

### I - Interface Segregation Principle
ממשקים ממוקדים וספציפיים

### D - Dependency Inversion Principle
תלות בהפשטות, לא במימושים

---

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

---

## Design Patterns בפרויקט

### DTO Pattern
- הפרדה בין Entity ל-Transfer Object
- אבטחת מידע רגיש
- גמישות בתקשורת

### Repository Pattern
- הפשטה של גישה לנתונים
- CRUD operations
- Custom queries

### Builder Pattern
- יצירת אובייקטים מורכבים
- קוד קריא
- Immutability

---

## Spring Boot ו-JPA

### מהו ORM?
Object-Relational Mapping - המרה בין אובייקטים לטבלאות

### יתרונות ORM
- הפשטה מעל SQL
- אבטחה מובנית
- ניהול מטמון
- ניהול קשרים
- ניהול טרנזקציות

---

## היררכיית הטכנולוגיות

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

---

## Spring Data JPA - היכולות

### Repository Interfaces
- פעולות CRUD אוטומטיות
- מימוש אוטומטי בזמן runtime

### Query Methods
- שמות מתודות שהופכות לשאילתות
- תמיכה בפעולות מורכבות

### Pagination & Sorting
- עימוד אוטומטי
- מיון גמיש

---

## קונפיגורציה של Spring Boot

### חיבור למסד נתונים
- URL חיבור
- משתמש וסיסמה
- Driver class

### הגדרות JPA
- ddl-auto: update/create/validate
- show-sql: הצגת שאילתות
- dialect: סוג מסד הנתונים

### Connection Pool
- connection-timeout
- maximum-pool-size

---

## יחסי גומלין ב-JPA

### One-to-One
- מורה ↔ פרטי מורה
- יחס 1:1 בין ישויות

### One-to-Many
- מורה → קורסים
- יחס 1:N בין ישויות

### Many-to-Many
- סטודנטים ↔ קורסים
- יחס M:N בין ישויות

---

## מאפייני יחסים

### בעלות על היחס
- Owning side vs Inverse side
- JoinColumn vs mappedBy

### אופציות Cascade
- ALL, PERSIST, MERGE, REMOVE

### אסטרטגיות טעינה
- LAZY vs EAGER
- ברירות מחדל לפי סוג היחס

---

## אופטימיזציה - בעיית N+1

### הבעיה
- שאילתה אחת לרשימה
- N שאילתות נוספות לפרטים
- ביצועים גרועים

### פתרונות
- JOIN FETCH
- EntityGraph
- Batch Fetching
- הגדרת Lazy Loading נכונה

---

## JOIN FETCH

### יתרונות
- שאילתה אחת במקום N+1
- שליטה מלאה בטעינה
- ביצועים משופרים

### מגבלות
- אוסף אחד בלבד ב-JOIN FETCH
- Cartesian Product
- שימוש ב-DISTINCT

---

## Lazy vs Eager Loading

### Lazy Loading
- טעינה רק בעת הצורך
- חיסכון בזיכרון
- ברירת מחדל ל-Collections

### Eager Loading
- טעינה מיידית
- מניעת LazyInitializationException
- ברירת מחדל ל-@ManyToOne

---

## ניהול עסקאות

### עקרונות ACID
- **Atomicity** - הכל או כלום
- **Consistency** - שמירת תקינות
- **Isolation** - בידוד בין עסקאות
- **Durability** - קביעות השינויים

### @Transactional
- ניהול אוטומטי
- Rollback בשגיאות
- הגדרות גמישות

---

## רמות בידוד

- **READ_UNCOMMITTED** - הנמוכה ביותר
- **READ_COMMITTED** - מונעת Dirty Read
- **REPEATABLE_READ** - מונעת Non-repeatable Read
- **SERIALIZABLE** - הגבוהה ביותר

---

## CI/CD Pipeline

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
    
    style A fill:#e3f2fd
    style B fill:#bbdefb
    style C fill:#90caf9
    style D fill:#64b5f6
    style E fill:#42a5f5
    style F fill:#2196f3
    style G fill:#1e88e5
    style H fill:#1976d2
    style I fill:#1565c0
    style J fill:#0d47a1
    style K fill:#0a3d91
```

<div dir="rtl">

---

## יתרונות CI/CD

### Continuous Integration
- גילוי מוקדם של בעיות
- אינטגרציה תכופה
- פידבק מהיר

### Continuous Deployment
- פריסה אוטומטית
- זמן קצר לשוק
- Rollback מהיר

---

## כלי CI/CD

- Jenkins
- GitHub Actions
- GitLab CI
- CircleCI
- Azure DevOps
- TeamCity

---

## Docker

### יתרונות
- סביבה אחידה
- Portability
- Isolation
- Scalability

### Multi-stage build
- שלב build נפרד
- Image קטן יותר
- אבטחה משופרת

---

## טיפול בשגיאות

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

---

## GlobalExceptionHandler

### יתרונות
- טיפול מרכזי בחריגות
- תגובות אחידות
- קודי HTTP מתאימים

### StandardResponse
- מבנה אחיד לתגובות
- status: success/error
- data/error
- timestamp

---

## Validation

### Bean Validation
- @NotBlank
- @Size
- @Email
- @Pattern

### יתרונות
- תיקוף אוטומטי
- הודעות שגיאה ברורות
- שימוש חוזר

---

## עקרונות Clean Code

### שמות משמעותיים
- שמות המסבירים את המטרה
- הימנעות מקיצורים לא ברורים

### פונקציות קטנות
- פונקציה עושה דבר אחד
- רמת הפשטה אחידה

### DRY
- Don't Repeat Yourself
- שימוש חוזר בקוד

---

## נוספים עקרונות

### Defensive Programming
- בדיקת null
- ולידציה של קלט
- טיפול בשגיאות

### Separation of Concerns
- הפרדת אחריויות
- כל שכבה במקומה

### Security Considerations
- הסתרת מידע רגיש
- הצפנת סיסמאות
- תיקוף קלט

---

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

---

## סוגי בדיקות

### Unit Tests
- בדיקת יחידות קוד
- Mocking
- מהירות גבוהה

### Integration Tests
- בדיקת אינטגרציה
- Test Database
- בדיקת flows

### E2E Tests
- בדיקה מקצה לקצה
- סימולציית משתמש
- בדיקה מלאה

---

## Domain vs Application

### Domain
- הבעיה העסקית
- מושגים עסקיים
- כללים עסקיים
- "מה המערכת עושה"

### Application
- הפתרון הטכני
- קוד ותשתיות
- טכנולוגיות
- "איך המערכת עושה"

---

## Domain-Driven Design (DDD)

### Domain Layer
- לוגיקה עסקית טהורה
- Entities
- Value Objects
- Domain Services

### Application Layer
- תיאום בין שכבות
- Use Cases
- Application Services

---

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

---

## נושאים חמים

### AI ו-Machine Learning
- כלי פיתוח חכמים
- אוטומציה של משימות
- ניתוח קוד אוטומטי

### Low-Code/No-Code
- פלטפורמות ויזואליות
- האצת פיתוח
- דמוקרטיזציה

---

## נושאים חמים (המשך)

### Cloud-Native
- Serverless Architecture
- Containerization
- Microservices

### DevSecOps
- אבטחה מובנית
- Shift-Left Testing
- Continuous Security

---

## סיכום

### הנדסת תוכנה דורשת:
- ידע טכני רחב
- חשיבה מערכתית
- כישורים רכים
- למידה מתמדת

### Spring Boot ו-JPA מספקים:
- פיתוח מהיר
- קוד נקי
- ביצועים טובים
- תחזוקה קלה

---

## תודה על ההקשבה!

### שאלות?

</div>