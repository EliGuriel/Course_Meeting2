<div dir="rtl">

# קשרים דו-כיווניים ב-JPA: הנחיות מעשיות

## מבוא

קשרים דו-כיווניים בין ישויות ב-JPA מאפשרים לנו לנווט בין אובייקטים מקושרים בשני הכיוונים. לדוגמה, מסטודנט לפרטי הסטודנט שלו, ומפרטי הסטודנט בחזרה לסטודנט. אולם, קשרים אלה דורשים תשומת לב מיוחדת כדי למנוע בעיות כמו רקורסיה אינסופית בסיריאליזציה או ב-`()toString`.

## סוגי קשרים

### One-to-One (אחד לאחד)

קשר בו לכל ישות מסוג אחד יש לכל היותר ישות אחת מסוג אחר. לדוגמה, לכל סטודנט יש סט אחד של פרטים נוספים.

### One-to-Many / Many-to-One (אחד לרבים / רבים לאחד)

קשר בו ישות אחת יכולה להיות מקושרת למספר ישויות מסוג אחר, אך כל ישות מהסוג השני יכולה להיות מקושרת רק לישות אחת מהסוג הראשון. לדוגמה, מורה אחד יכול ללמד מספר סטודנטים, אך כל סטודנט שייך למורה אחד בלבד.

### Many-to-Many (רבים לרבים)

קשר בו ישות מסוג אחד יכולה להיות מקושרת למספר ישויות מסוג אחר, וגם להפך. לדוגמה, מורה יכול ללמד מספר סטודנטים, וסטודנט יכול ללמוד אצל מספר מורים.

## מושגי יסוד בקשרים דו-כיווניים

### צד בעלים (Owning Side) וצד לא-בעלים (Non-Owning Side)

בכל קשר דו-כיווני, אחד הצדדים חייב להיות מוגדר כ"בעלים" של הקשר. הצד השני מוגדר באמצעות `mappedBy`.

- **צד הבעלים**: הצד שמכיל את מפתח הזר (foreign key) במסד הנתונים
- **צד לא-בעלים**: הצד שמשתמש ב-`mappedBy` לציון שהוא משתמש בקשר שכבר מוגדר בצד השני

### המאפיין `mappedBy`

`mappedBy` הוא מאפיין שמשתמשים בו בצד הלא-בעלים של קשר דו-כיווני. הוא מציין את שם השדה בישות השנייה (הבעלים) שמחזיק את הקשר.

לדוגמה:

</div>

```java
// בישות StudentDetails (צד לא-בעלים)
@OneToOne(mappedBy = "details")
private Student student;

// בישות Student (צד בעלים)
@OneToOne
@JoinColumn(name = "details_id")
private StudentDetails details;
```

<div dir="rtl">

במקרה זה, `"mappedBy = details"` מציין ש"details" הוא שם השדה בישות Student שמגדיר את הקשר.

### איזה צד צריך להיות הבעלים?

ההמלצה הכללית:

1. **בקשרי One-to-One**: הצד שסביר יותר שיהיה מאותחל עם הישות האחרת. לרוב זו הישות "העיקרית" (למשל Student במקום StudentDetails).

2. **בקשרי One-to-Many/Many-to-One**: תמיד הצד ה-"Many" הוא הבעלים (למשל, Employee ב-Employee-Department).

3. **בקשרי Many-to-Many**: אין חשיבות מכרעת, אך מומלץ לבחור בצד שיותר הגיוני מבחינה עסקית לנהל את הקשר.

## מניעת בעיות נפוצות

### אנוטציית `JsonIgnore@`

משמשת למניעת רקורסיה אינסופית בעת המרה לפורמט JSON.

**היכן להשתמש**:
- בקשרי One-to-One: בצד הלא-בעלים (הצד עם `mappedBy`)
- בקשרי One-to-Many/Many-to-One: בדרך כלל בצד ה-"Many-to-One" (הילד)
- בקשרי Many-to-Many: באחד הצדדים לפחות, בהתאם לדרישות העסקיות

לדוגמה:

</div>

```java
// בישות StudentDetails
@OneToOne(mappedBy = "details")
@JsonIgnore
private Student student;
```

<div dir="rtl">



### אנוטציית `ToString.Exclude@`

משמשת למניעת רקורסיה אינסופית בעת קריאה למתודת `()toString`.

**היכן להשתמש**:
- בכל קשר דו-כיווני, בשני הצדדים (או לפחות באחד מהם)
- ניתן להשתמש גם ברמת המחלקה עם `ToString(exclude = {"field1", "field2"})@`

לדוגמה:

</div>

```java
// ברמת השדה
@OneToOne(mappedBy = "details")
@ToString.Exclude
private Student student;

// או ברמת המחלקה
@ToString(exclude = {"student"})
public class StudentDetails { 
      }
```

<div dir="rtl">

### אנוטציית `EqualsAndHashCode.Exclude@`

משמשת למניעת רקורסיה אינסופית במתודות `()equals` ו-`()hashCode`.

**היכן להשתמש**:
- בקשרים דו-כיווניים, בדומה ל-`ToString.Exclude@`
- ניתן להשתמש גם ברמת המחלקה עם `EqualsAndHashCode(exclude = {"field1", "field2"})@`

## דוגמאות קוד מפורטות

### One-to-One: Student ↔ StudentDetails

#### Student (צד בעלים)

</div>

```java
@Entity
@Data
@ToString(exclude = "details")
public class Student {
    @Id
    @GeneratedValue
    private Long id;
    
    // ... שדות אחרים
    
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "details_id")
    private StudentDetails details;
}
```

<div dir="rtl">

#### StudentDetails (צד לא-בעלים)

</div>

```java
@Entity
@Data
@ToString(exclude = "student")
public class StudentDetails {
    @Id
    @GeneratedValue
    private Long id;
    
    // ... שדות אחרים
    
    @OneToOne(mappedBy = "details")
    @JsonIgnore
    private Student student;
}
```

<div dir="rtl">

### Many-to-Many: Student ↔ Teacher

#### Teacher (צד בעלים)

</div>

```java
@Entity
@Data
@ToString(exclude = "students")
public class Teacher {
    @Id
    @GeneratedValue
    private Long id;
    
    // ... שדות אחרים
    
    @ManyToMany
    @JoinTable(
        name = "teacher_student",
        joinColumns = @JoinColumn(name = "teacher_id"),
        inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    private Set<Student> students = new HashSet<>();
    
    // מתודות עזר לניהול הקשר
    public void addStudent(Student student) {
        students.add(student);
        student.getTeachers().add(this);
    }
    
    public void removeStudent(Student student) {
        students.remove(student);
        student.getTeachers().remove(this);
    }
}
```
<div dir="rtl">

#### Student (צד לא-בעלים)

</div>

```java
@Entity
@Data
@ToString(exclude = "teachers")
public class Student {
    @Id
    @GeneratedValue
    private Long id;
    
    // ... שדות אחרים
    
    @ManyToMany(mappedBy = "students")
    @JsonIgnore
    private Set<Teacher> teachers = new HashSet<>();
}
```

<div dir="rtl">


## סיכום והמלצות

1. **היררכיית קשרים**:
    - הגדר צד אחד כבעלים ואת השני כלא-בעלים באמצעות `mappedBy`
    - בחר בצד הבעלים בהתאם לסוג הקשר והלוגיקה העסקית

2. **מניעת רקורסיה**:
    - השתמש ב-`JsonIgnore@` בצד הלא-בעלים למניעת רקורסיה בסיריאליזציה לJSON
    - השתמש ב-`ToString.Exclude@` בקשרים דו-כיווניים למניעת רקורסיה בהדפסה
    - שקול שימוש ב-`EqualsAndHashCode.Exclude@` למניעת בעיות בהשוואת אובייקטים

3. **אתחול אוספים**:
    - תמיד אתחל אוספים (כמו `List`, `Set`) בעת הגדרתם למניעת NullPointerException
    - לדוגמה: `private Set<Student> students = new HashSet<>();`

4. **מתודות עזר**:
    - ספק מתודות עזר לניהול קשרים מסוג Many-to-Many ו-One-to-Many
    - דאג לעדכן את שני צדדי הקשר בכל פעולה

5. **שיקולי עיצוב**:
    - שקול באיזה צד לשים `JsonIgnore@` בהתאם לצרכי ה-API (האם אתה רוצה שמורה יחזיר את כל הסטודנטים שלו? האם אתה רוצה שסטודנט יחזיר את כל המורים שלו?)
    - בקשרי One-to-One דו-כיווניים, ה-`JsonIgnore@` בדרך כלל שייך לצד עם ה-`mappedBy`
    - שימוש באנוטציות של Lombok (`Data`, `@ToString`, וכו') מפשט מאוד את הקוד

</div>