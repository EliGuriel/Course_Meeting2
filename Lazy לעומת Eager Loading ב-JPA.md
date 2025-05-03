<div dir="rtl">

# Lazy לעומת Eager Loading ב-JPA

<div dir="rtl">

## מבוא למדיניות טעינה ב-JPA

אחד האתגרים הגדולים בעבודה עם ORM ובסיסי נתונים הוא ניהול יעיל של טעינת נתונים מקושרים. JPA מציע שתי אסטרטגיות עיקריות לטעינת ישויות קשורות: **Lazy Loading** (טעינה עצלה) ו-**Eager Loading** (טעינה להוטה). הבחירה בין השתיים משפיעה משמעותית על ביצועי האפליקציה ויעילותה.

## Lazy Loading (טעינה עצלה)

### עקרון הפעולה

ב-Lazy Loading, ישויות מקושרות אינן נטענות באופן מיידי כאשר האובייקט המקורי נשלף מבסיס הנתונים. במקום זאת, JPA יוצר "פרוקסי" - מעין מחזיק מקום שיטען את הנתונים האמיתיים רק כאשר יש גישה בפועל לנתונים אלה.

### דוגמת קוד

</div>

<div dir="ltr">

```java
@Entity
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;

    // here EAGER is the default, but in @OneToMany the default is LAZY
    @OneToMany(mappedBy = "teacher", fetch = FetchType.LAZY) 
    private List<Course> courses;
    
    // getters and setters or Lombok
}
```

<div dir="rtl">

### יתרונות

1. **יעילות זיכרון** - טוען רק את הנתונים שבאמת נדרשים
2. **שאילתות מהירות יותר** - פחות נתונים מועברים בכל בקשה ראשונית
3. **מתאים למודלים גדולים** - יעיל במיוחד כאשר יש קשרים רבים ומורכבים

### חסרונות

1. **LazyInitializationException** - שגיאה נפוצה כאשר מנסים לגשת לאובייקט לאחר סגירת ה-Session
2. **אתגרי תצורה** - דורש תשומת לב מיוחדת בתצורת האפליקציה ובתכנון השאילתות

## Eager Loading (טעינה להוטה)

### עקרון הפעולה

ב-Eager Loading, כל הישויות המקושרות נטענות מיד עם טעינת האובייקט המקורי. כאשר שולפים אובייקט מבסיס הנתונים, JPA יטען באופן אוטומטי את כל הקשרים המוגדרים כ-Eager.

### דוגמת קוד

</div>

<div dir="ltr">

```java
@Entity
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    
    @OneToOne(fetch = FetchType.EAGER) 
    @JoinColumn(name = "details_id")
    private TeacherDetails details;
    
    // getters and setters or Lombok
}
```
<div dir="rtl">

### יתרונות

1. **נוח לשימוש** - הנתונים זמינים תמיד, ללא שגיאות בגישה מאוחרת
2. **פחות קוד לניהול** - אין צורך בקוד מיוחד לטיפול בטעינת נתונים
3. **ביצועים טובים למודלים קטנים** - יעיל כשכמות הנתונים המקושרים מוגבלת

### חסרונות

1. **עומס זיכרון** - עלול לגרום לטעינת נתונים רבים שלא יהיה בהם שימוש
2. **שאילתות איטיות** - טעינת כל הנתונים בבת אחת יכולה להאט את הביצועים
3. **לא מתאים למודלים גדולים** - בעייתי כאשר יש מספר רב של יחסים

## ברירות מחדל ב-JPA

JPA מגדיר ברירות מחדל למדיניות הטעינה בהתאם לסוג היחס:

| סוג היחס     | ברירת מחדל | סיבה |
|--------------|-------------|------|
| OneToOne@    | EAGER | יחסים אלה בדרך כלל מכילים מידע משלים חיוני |
| ManyToOne@  | EAGER | לרוב נדרש האובייקט "הורה" כשמתייחסים לאובייקט "ילד" |
| OneToMany@  | LAZY | אוספים יכולים להיות גדולים ולא תמיד דרושים |
| ManyToMany@ | LAZY | אוספים עשויים להיות גדולים ולעיתים קרובות אינם הכרחיים מיידית |



## דוגמה משולבת: שימוש מאוזן

שילוב נכון בין Lazy ו-Eager יכול לשפר משמעותית את ביצועי האפליקציה:

</div>

<div dir="ltr">

```java
@Entity
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    
    // basic info that is always needed using Eager
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "details_id")
    private TeacherDetails details;
    
    // The collection that maybe large, we use Lazy
    @OneToMany(mappedBy = "teacher", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Course> courses;
    
    // Properties that can be large use Lazy
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] profilePicture;
    
    // getters and setters
}
```

<div dir="rtl">

## המלצות מעשיות

1. **השאר את רוב היחסים כ-Lazy** - זו אסטרטגיה בטוחה יותר שתמנע בעיות ביצועים
2. **השתמש ב-Eager רק עבור יחסים קטנים וחיוניים** - למשל, נתונים שכמעט תמיד יידרשו יחד
3. **השתמש בטעינה ממוקדת** - Load on demand באמצעות JOIN FETCH או EntityGraphs רק כשצריך
4. **הימנע מלגשת ליחסי Lazy מחוץ ל-transaction** - או השתמש בפתרונות כמו Open Session In View (עם הבנת המגבלות)
5. **בצע בדיקות ביצועים** - נטר את השאילתות לזיהוי בעיות N+1 או טעינת יתר

הבחירה הנכונה בין Lazy ו-Eager Loading היא אחד ההיבטים הקריטיים לביצועים טובים באפליקציות מבוססות JPA. מדיניות הטעינה משפיעה ישירות על:

- כמות הזיכרון שהאפליקציה צורכת
- מספר השאילתות שנשלחות לבסיס הנתונים
- זמן התגובה של האפליקציה
- המורכבות של קוד האפליקציה

ברוב המקרים, אסטרטגיה מומלצת היא להשתמש ב-Lazy Loading כברירת מחדל ולהעמיס נתונים נוספים רק כשצריך אותם באמת. מודעות לדפוסי הגישה לנתונים באפליקציה היא המפתח לבחירה הנכונה בין הגישות השונות.

</div>