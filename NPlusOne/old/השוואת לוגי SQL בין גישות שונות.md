<div dir="rtl">

# השוואת לוגי SQL בין גישות שונות

## הדגמת בעיית N+1 מהלוגים

בלוגים שסיפקת ניתן לראות בבירור את בעיית ה-N+1 כאשר קוראים ל-`getAllTeachersWithN1Problem()`:

</div>

```
2025-04-07T11:53:25.583 DEBUG org.hibernate.SQL: 
    select
        t1_0.id,
        t1_0.name,
        t1_0.subject 
    from
        teachers t1_0

// לאחר מכן, עבור כל מורה:
2025-04-07T11:53:25.616 DEBUG org.hibernate.SQL: 
    select
        s1_0.teacher_id,
        s1_0.id,
        s1_0.details_id,
        s1_0.email,
        s1_0.first_name,
        s1_0.last_name 
    from
        students s1_0 
    where
        s1_0.teacher_id=?

// לאחר מכן, עבור כל תלמיד:
2025-04-07T11:53:25.631 DEBUG org.hibernate.SQL: 
    select
        sd1_0.id,
        sd1_0.address,
        sd1_0.phone_number 
    from
        student_details sd1_0 
    where
        sd1_0.id=?
```

<div dir="rtl">

הלוגים מראים בדיוק את דפוס הבעיה שהוזכר במסמך ההשוואה:
1. שאילתה ראשית אחת לקבלת כל המורים
2. שאילתה נפרדת לכל מורה לקבלת התלמידים שלו
3. שאילתה נפרדת לכל תלמיד לקבלת הפרטים שלו

בסך הכל בדוגמה שלך רואים **7 שאילתות SQL**:
- 1 שאילתה לטעינת המורים
- 3 שאילתות לטעינת התלמידים (אחת לכל מורה)
- 3 שאילתות לטעינת פרטי התלמידים (אחת לכל תלמיד)

## הפתרון עם JOIN FETCH

השאילתה שהייתה מופעלת עם `getAllTeachersWithoutN1Problem()` אינה מוצגת בלוגים שסיפקת, אבל היא תהיה דומה לזו:

</div>

```sql
SELECT 
    t1_0.id, t1_0.name, t1_0.subject,
    s1_0.id, s1_0.details_id, s1_0.email, s1_0.first_name, s1_0.last_name, s1_0.teacher_id,
    sd1_0.id, sd1_0.address, sd1_0.phone_number
FROM 
    teachers t1_0
LEFT JOIN 
    students s1_0 ON t1_0.id = s1_0.teacher_id
LEFT JOIN 
    student_details sd1_0 ON s1_0.details_id = sd1_0.id
```

<div dir="rtl">

## הפתרון עם EntityGraph

בגישת EntityGraph, השאילתה המופעלת תהיה בפורמט דומה לשאילתת JOIN FETCH, אך היא נוצרת באופן אוטומטי על ידי JPA:

</div>

```sql
SELECT 
    t1_0.id, t1_0.name, t1_0.subject,
    s1_0.id, s1_0.details_id, s1_0.email, s1_0.first_name, s1_0.last_name, s1_0.teacher_id,
    sd1_0.id, sd1_0.address, sd1_0.phone_number
FROM 
    teachers t1_0
LEFT JOIN 
    students s1_0 ON t1_0.id = s1_0.teacher_id
LEFT JOIN 
    student_details sd1_0 ON s1_0.details_id = sd1_0.id
```

<div dir="rtl">

## השוואת ביצועים

בלוגים לא מוצג זמן הריצה המדויק של כל גישה, אבל ניתן לראות את מספר שאילתות ה-SQL המופעלות:

| גישה | מספר שאילתות SQL | הערכת ביצועים |
|------|-----------------|---------------|
| עם בעיית N+1 | 7 | איטית (גדל עם כמות המורים והתלמידים) |
| JOIN FETCH | 1 | מהירה משמעותית |
| EntityGraph | 1 | מהירה משמעותית |

## מסקנות מהלוגים

1. הלוגים מאמתים בבירור את קיום בעיית ה-N+1 כפי שתוארה במסמך.
2. כמות השאילתות בגישה הרגילה גדלה ליניארית עם מספר הישויות הקשורות.
3. גישות JOIN FETCH ו-EntityGraph פותרות את הבעיה על ידי הורדת מספר השאילתות לשאילתה אחת.

כדי לקבל השוואה מלאה, מומלץ להפעיל גם את נקודות הקצה האחרות ולראות את השאילתות המופעלות בכל אחת מהגישות, וכן למדוד את זמני הביצוע בפועל.

</div>