<div dir="rtl">

# הקדמה למפגש השתלמות - פיתוח מערכות Spring Boot עם JPA

## מטרת המפגש

המפגש מתמקד בהבנת ארכיטקטורת Spring Boot ו-JPA דרך בניית מערכת ניהול סטודנטים. נעבור תהליך הדרגתי מיישום בסיסי ועד למערכת מורכבת עם קשרי גומלין מלאים בין ישויות.

## תוכן המפגש

### חלק ראשון - מבוא ויסודות (Stage 1)
- הכרת ארכיטקטורת שכבות: Controller, Service, Repository, Entity
- מימוש CRUD בסיסי עם Spring Data JPA
- StandardResponse - מבנה תגובה אחיד ב-REST API
- הרצת האפליקציה וניתוח לוגים
- בדיקת ה-API עם HTTP Client

### חלק שני - יחסי One-to-One (Stage 2)
- מיפוי קשר One-to-One בין Student ל-Address
- Bidirectional relationships וניהול הקשר משני הכיוונים
- Cascade operations והשלכותיהן
- שינויים בסכמת הנתונים

### חלק שלישי - יחסי One-to-Many (Stage 3)
- מיפוי קשר One-to-Many בין Teacher ל-Students
- אתגרי ביצועים בטעינת אוספים
- ניהול הקשר מצד ה-Many ומצד ה-One

### חלק רביעי - יחסי Many-to-Many (Stage 4)
- מיפוי קשר Many-to-Many בין Students ל-Courses
- טבלת Join והגדרתה
- ניהול מורכבות הקשרים

## נושאים מתקדמים

### Lazy Loading vs Eager Loading
- הבנת מנגנוני הטעינה ב-JPA
- בעיית ה-LazyInitializationException
- שימוש ב-DTOs כפתרון

### ניהול טרנזקציות
- אנוטציית @Transactional והשימוש בה
- Rollback behavior וטיפול בשגיאות
- Transaction boundaries וה-Proxy pattern

### בעיית N+1 Queries
- זיהוי הבעיה בלוגים
- השלכות על ביצועים
- פתרונות: Join Fetch, Entity Graphs

## עקרונות הנדסת תוכנה

### ארכיטקטורה ותכנון
- Separation of Concerns - הפרדת אחריות בין שכבות
- Dependency Injection ו-Inversion of Control
- SOLID principles ביישומי Spring Boot

### איכות קוד ותחזוקה
- Clean Code practices בסביבת Enterprise
- Error handling patterns
- Logging best practices

### ביצועים וסקלביליות - לעתיד  
- Database connection pooling
- Caching strategies
- Query optimization


##  קווים מנחים
- עקרונות תכנון נכון של Entities
- Best practices בעבודה עם JPA
- הנחיות לארכיטקטורה נכונה


</div>