package org.example.nplusone;

import org.example.nplusone.entity.Student;
import org.example.nplusone.entity.StudentDetails;
import org.example.nplusone.entity.Teacher;
import org.example.nplusone.repository.StudentRepository;
import org.example.nplusone.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DataInitializer implements CommandLineRunner {

    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;

    @Autowired
    public DataInitializer(StudentRepository studentRepository, TeacherRepository teacherRepository) {
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        // בדיקה אם כבר יש נתונים במסד הנתונים
        if (teacherRepository.count() == 0) {
            // יצירת המורים
            Teacher mathTeacher = new Teacher();
            mathTeacher.setName("דוד כהן");
            mathTeacher.setSubject("מתמטיקה");
            
            Teacher physicsTeacher = new Teacher();
            physicsTeacher.setName("שרה לוי");
            physicsTeacher.setSubject("פיזיקה");
            
            Teacher englishTeacher = new Teacher();
            englishTeacher.setName("מיכאל ברג");
            englishTeacher.setSubject("אנגלית");
            
            // שמירת המורים במסד הנתונים
            teacherRepository.save(mathTeacher);
            teacherRepository.save(physicsTeacher);
            teacherRepository.save(englishTeacher);
            
            // יצירת תלמידים עם פרטים ושיוך למורים
            
            // תלמיד 1 - משוייך למורה למתמטיקה
            Student moshe = new Student();
            moshe.setFirstName("משה");
            moshe.setLastName("כהן");
            moshe.setEmail("moshe.cohen@example.com");
            moshe.setTeacher(mathTeacher);

            StudentDetails mosheDetails = new StudentDetails();
            mosheDetails.setAddress("רחוב הרצל 123, תל אביב");
            mosheDetails.setPhoneNumber("054-1234567");
            
            moshe.setDetails(mosheDetails);
            studentRepository.save(moshe);
            
            // תלמיד 2 - משוייך למורה לפיזיקה
            Student yael = new Student();
            yael.setFirstName("יעל");
            yael.setLastName("לוי");
            yael.setEmail("yael.levy@example.com");
            yael.setTeacher(physicsTeacher);
            
            StudentDetails yaelDetails = new StudentDetails();
            yaelDetails.setAddress("שדרות בן גוריון 45, ירושלים");
            yaelDetails.setPhoneNumber("052-1234567");
            
            yael.setDetails(yaelDetails);
            studentRepository.save(yael);
            
            // תלמיד 3 - משוייך למורה לאנגלית
            Student noam = new Student();
            noam.setFirstName("נועם");
            noam.setLastName("מזרחי");
            noam.setEmail("noam.mizrachi@example.com");
            noam.setTeacher(englishTeacher);
            
            StudentDetails noamDetails = new StudentDetails();
            noamDetails.setAddress("רחוב ויצמן 78, חיפה");
            noamDetails.setPhoneNumber("053-1234567");
            
            noam.setDetails(noamDetails);
            studentRepository.save(noam);
            
            // תלמיד 4 - ללא מורה משוייך
            Student tamar = new Student();
            tamar.setFirstName("תמר");
            tamar.setLastName("גולדברג");
            tamar.setEmail("tamar.goldberg@example.com");
            
            StudentDetails tamarDetails = new StudentDetails();
            tamarDetails.setAddress("רחוב אלנבי 56, תל אביב");
            tamarDetails.setPhoneNumber("050-9876543");
            
            tamar.setDetails(tamarDetails);
            studentRepository.save(tamar);
            
            // תלמיד 5 - ללא מורה משוייך
            Student amit = new Student();
            amit.setFirstName("עמית");
            amit.setLastName("דהן");
            amit.setEmail("amit.dahan@example.com");
            
            StudentDetails amitDetails = new StudentDetails();
            amitDetails.setAddress("רחוב הנביאים 23, ירושלים");
            amitDetails.setPhoneNumber("058-7654321");
            
            amit.setDetails(amitDetails);
            studentRepository.save(amit);
            
            // תלמיד 6 - משוייך למורה למתמטיקה
            Student daniel = new Student();
            daniel.setFirstName("דניאל");
            daniel.setLastName("שמש");
            daniel.setEmail("daniel.shemesh@example.com");
            daniel.setTeacher(mathTeacher);
            
            StudentDetails danielDetails = new StudentDetails();
            danielDetails.setAddress("רחוב הגפן 12, רמת גן");
            danielDetails.setPhoneNumber("052-9876543");
            
            daniel.setDetails(danielDetails);
            studentRepository.save(daniel);
            
            // תלמיד 7 - משוייך למורה לפיזיקה
            Student liora = new Student();
            liora.setFirstName("ליאורה");
            liora.setLastName("כץ");
            liora.setEmail("liora.katz@example.com");
            liora.setTeacher(physicsTeacher);
            
            StudentDetails lioraDetails = new StudentDetails();
            lioraDetails.setAddress("שדרות רוטשילד 89, תל אביב");
            lioraDetails.setPhoneNumber("054-3456789");
            
            liora.setDetails(lioraDetails);
            studentRepository.save(liora);
            
            // תלמיד 8 - משוייך למורה לאנגלית
            Student ron = new Student();
            ron.setFirstName("רון");
            ron.setLastName("אברהם");
            ron.setEmail("ron.avraham@example.com");
            ron.setTeacher(englishTeacher);
            
            StudentDetails ronDetails = new StudentDetails();
            ronDetails.setAddress("רחוב בן יהודה 45, חיפה");
            ronDetails.setPhoneNumber("050-2345678");
            
            ron.setDetails(ronDetails);
            studentRepository.save(ron);
            
            // תלמיד 9 - ללא מורה משוייך
            Student maya = new Student();
            maya.setFirstName("מאיה");
            maya.setLastName("לוטן");
            maya.setEmail("maya.lotan@example.com");
            
            StudentDetails mayaDetails = new StudentDetails();
            mayaDetails.setAddress("רחוב הירקון 67, תל אביב");
            mayaDetails.setPhoneNumber("058-5678901");
            
            maya.setDetails(mayaDetails);
            studentRepository.save(maya);
            
            // תלמיד 10 - ללא מורה משוייך
            Student itay = new Student();
            itay.setFirstName("איתי");
            itay.setLastName("נחמני");
            itay.setEmail("itay.nachmani@example.com");
            
            StudentDetails itayDetails = new StudentDetails();
            itayDetails.setAddress("רחוב הנשיא 34, ירושלים");
            itayDetails.setPhoneNumber("052-6789012");
            
            itay.setDetails(itayDetails);
            studentRepository.save(itay);
            
            // שמירת המורים שוב כדי לוודא שכל הקשרים נשמרו
            teacherRepository.save(mathTeacher);
            teacherRepository.save(physicsTeacher);
            teacherRepository.save(englishTeacher);
            
            System.out.println("אתחול נתונים הושלם בהצלחה.");
        } else {
            System.out.println("מסד הנתונים כבר מכיל רשומות. דילוג על אתחול.");
        }
    }
}