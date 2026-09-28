package database;

import android.content.Context;

public class DatabaseSeeder {

    public static void seedDatabase() {

        seedAdminAccounts();
        seedTeacherAccounts();
        seedStudentAccounts();
        seedSchedules();
        seedAnnouncements();
    }

    private static void seedAdminAccounts() {
        String[] firstNames = {"Maria", "Juan", "Sofia", "Carlos", "Isabella", "Miguel", "Lucia", "Rafael", "Carmen", "Diego"};
        String[] middleNames = {"Santos", "Reyes", "Cruz", "Garcia", "Lopez", "Martinez", "Gonzales", "Rivera", "Torres", "Flores"};
        String[] lastNames = {"Dela Cruz", "Ramos", "Gonzales", "Santos", "Reyes", "Castro", "Mendoza", "Villanueva", "Pascual", "Hernandez"};

        for (int i = 0; i < 5; i++) {
            ModelAccount admin = new ModelAccount(
                    AccountType.ADMIN,
                    firstNames[i],
                    middleNames[i],
                    lastNames[i],
                    firstNames[i].toLowerCase() + ".admin@aguhon.edu.ph",
                    "0916" + String.format("%07d", 1000000 + i),
                    "admin" + (i + 1),
                    "admin123",
                    null
            );
            DatabaseManager.createAccount(AccountType.ADMIN, admin);
        }
    }

    private static void seedTeacherAccounts() {
        String[] firstNames = {"Pedro", "Ana", "Jose", "Rosa", "Manuel", "Elena", "Ricardo", "Patricia", "Fernando", "Gloria"};
        String[] middleNames = {"Antonio", "Maria", "Luis", "Carmen", "Jose", "Isabel", "Miguel", "Teresa", "Angel", "Pilar"};
        String[] lastNames = {"Aquino", "Bautista", "Cruz", "Diaz", "Espinosa", "Fernandez", "Gomez", "Hidalgo", "Ibanez", "Jimenez"};
        String[] gradeLevels = {"Nursery", "Nursery", "Kinder", "Kinder", "Prep", "Prep", "Nursery", "Kinder", "Prep", "Nursery"};

        for (int i = 0; i < 5; i++) {
            ModelAccount teacher = new ModelAccount(
                    AccountType.TEACHER,
                    firstNames[i],
                    middleNames[i],
                    lastNames[i],
                    gradeLevels[i],
                    firstNames[i].toLowerCase() + ".teacher@aguhon.edu.ph",
                    "0917" + String.format("%07d", 2000000 + i),
                    "teacher" + (i + 1),
                    "teacher123",
                    null
            );
            DatabaseManager.createAccount(AccountType.TEACHER, teacher);
        }
    }

    private static void seedStudentAccounts() {
        String[] firstNames = {"Gabriel", "Valentina", "Sebastian", "Camila", "Mateo", "Isabella", "Lucas", "Sofia", "Diego", "Emma"};
        String[] middleNames = {"Luis", "Marie", "Jose", "Anne", "Carlos", "Grace", "Miguel", "Faith", "Antonio", "Joy"};
        String[] lastNames = {"Santos", "Reyes", "Garcia", "Lopez", "Martinez", "Cruz", "Fernandez", "Gonzales", "Torres", "Rivera"};
        String[] gradeLevels = {"Nursery", "Nursery", "Kinder", "Kinder", "Prep", "Prep", "Nursery", "Kinder", "Prep", "Nursery"};
        String[] sections = {"A", "B", "B", "A", "B", "A", "B", "A", "B", "A"};
        String[] guardianFirstNames = {"Roberto", "Maria", "Carlos", "Ana", "Miguel", "Rosa", "Juan", "Elena", "Pedro", "Carmen"};
        String[] guardianLastNames = {"Santos", "Reyes", "Garcia", "Lopez", "Martinez", "Cruz", "Fernandez", "Gonzales", "Torres", "Rivera"};
        String[] relations = {"Parent", "Guardian", "Relative", "Parent", "Guardian", "Relative", "Parent", "Guardian", "Relative","Parent"};

        for (int i = 0; i < 5; i++) {
            ModelStudentAccount student = new ModelStudentAccount(
                    AccountType.STUDENT,
                    firstNames[i],
                    middleNames[i],
                    lastNames[i],
                    firstNames[i].toLowerCase() + ".student@aguhon.edu.ph",
                    "0918" + String.format("%07d", 3000000 + i),
                    "student" + (i + 1),
                    "student123",
                    "SC" + String.format("%04d", 1000 + i),
                    gradeLevels[i],
                    sections[i],
                    relations[i],
                    guardianFirstNames[i],
                    middleNames[i],
                    guardianLastNames[i],
                    null
            );
            DatabaseManager.createAccount(AccountType.STUDENT, student);
        }
    }

    private static void seedSchedules() {
/*
        String[] nurserySubjects = {"Math", "Science", "Social Studies", "Arts and Creativity", "Language", "Communication", "Physical Development", "Language", "Communication", "Physical Development"};
        String[] nurseryTeachers = {"Ms. Ana Cruz", "Ms. Rosa Diaz", "Ms. Elena Gomez", "Ms. Patricia Hidalgo", "Ms. Gloria Jimenez", "Ms. Ana Cruz", "Ms. Rosa Diaz", "Ms. Elena Gomez", "Ms. Patricia Hidalgo", "Ms. Gloria Jimenez"};
        String[] nurseryTimes = {"8:00AM-9:00AM", "9:00AM-10:00AM", "10:00AM-11:00AM", "11:00AM-12:00PM", "1:00PM-2:00PM", "2:00PM-3:00PM", "8:00AM-9:00AM", "9:00AM-10:00AM", "10:00AM-11:00AM", "11:00AM-12:00PM"};
        String[] nurseryDays = {"Monday, Wednesday, Friday", "Tuesday, Thursday", "Monday, Wednesday", "Tuesday, Thursday", "Monday, Wednesday, Friday", "Tuesday, Thursday", "Thursday, Friday", "Monday, Tuesday", "Wednesday, Friday", "Monday, Thursday"};

        for (int i = 0; i < 2; i++) {
            String[] times = nurseryTimes[i].split("-");
            ModelAccount randTeacher = DatabaseManager.getRandomTeacher();
            String firstName = randTeacher.getFirstName();
            String middleName = randTeacher.getMiddleName();
            String lastName = randTeacher.getLastName();

            String teacherName = firstName + " " +
                    (middleName != null && !middleName.isEmpty() ? middleName.substring(0,1) + ". " : "") +
                    lastName;
            ModelSchedule schedule = new ModelSchedule(
                    nurserySubjects[i],
                    "Nursery",
                    times[0],
                    times[1],
                    nurseryDays[i],
                    teacherName

            );
            DatabaseManager.createSchedule(schedule);
        }

        String[] kinderSubjects = {"Math", "Science", "Social Studies", "Arts and Creativity", "Language", "Communication", "Physical Development", "Language", "Communication", "Physical Development"};
        String[] kinderTeachers = {"Mr. Jose Cruz", "Ms. Ana Diaz", "Mr. Manuel Espinosa", "Ms. Elena Fernandez", "Mr. Ricardo Gomez", "Mr. Jose Cruz", "Ms. Ana Diaz", "Mr. Manuel Espinosa", "Ms. Elena Fernandez", "Mr. Ricardo Gomez"};
        String[] kinderTimes = {"8:00AM-9:30AM", "9:30AM-11:00AM", "11:00AM-12:00PM", "1:00PM-2:00PM", "2:00PM-3:00PM", "8:00AM-9:00AM", "9:00AM-10:00AM", "10:00AM-11:00AM", "11:00AM-12:00PM", "1:00PM-2:00PM"};
        String[] kinderDays = {"Monday, Wednesday, Friday", "Tuesday, Thursday", "Monday, Wednesday, Friday", "Tuesday, Thursday", "Monday, Wednesday", "Tuesday, Friday", "Monday, Thursday", "Wednesday, Friday", "Tuesday, Thursday", "Monday, Wednesday, Friday"};

        for (int i = 0; i < 2; i++) {
            String[] times = kinderTimes[i].split("-");
            ModelSchedule schedule = new ModelSchedule(
                    kinderSubjects[i],
                    "Kinder",
                    times[0],
                    times[1],
                    kinderDays[i],
                    kinderTeachers[i]
            );
            DatabaseManager.createSchedule(schedule);
        }

        String[] prepSubjects = {"Math", "Science", "Social Studies", "Arts and Creativity", "Language", "Communication", "Physical Development", "Language", "Communication", "Physical Development"};
        String[] prepTeachers = {"Mr. Manuel Espinosa", "Ms. Elena Fernandez", "Mr. Ricardo Gomez", "Ms. Patricia Hidalgo", "Ms. Gloria Jimenez", "Mr. Manuel Espinosa", "Ms. Elena Fernandez", "Mr. Ricardo Gomez", "Ms. Patricia Hidalgo", "Ms. Gloria Jimenez"};
        String[] prepTimes = {"8:00AM-9:30AM", "9:30AM-11:00AM", "11:00AM-12:30PM", "1:00PM-2:30PM", "2:30PM-3:30PM", "8:00AM-9:00AM", "9:00AM-10:30AM", "10:30AM-12:00PM", "1:00PM-2:00PM", "2:00PM-3:00PM"};
        String[] prepDays = {"Monday, Wednesday, Friday", "Tuesday, Thursday", "Monday, Wednesday, Friday", "Tuesday, Thursday", "Monday, Wednesday", "Tuesday, Thursday, Friday", "Monday, Wednesday", "Tuesday, Thursday", "Monday, Friday", "Wednesday, Thursday"};

        for (int i = 0; i < 2; i++) {
            String[] times = prepTimes[i].split("-");
            ModelSchedule schedule = new ModelSchedule(
                    prepSubjects[i],
                    "Prep",
                    times[0],
                    times[1],
                    prepDays[i],
                    prepTeachers[i]
            );
            DatabaseManager.createSchedule(schedule);
        }*/
    }

    private static void seedAnnouncements() {

        String[] nurseryTitles = {
                "Welcome to Nursery Class!",
                "Parent-Teacher Meeting Schedule",
                "Field Trip to the Zoo",
                "Art Supplies Needed",
                "Sports Day Announcement",
                "Holiday Break Schedule",
                "New Library Books Available",
                "Healthy Snack Guidelines",
                "Talent Show Invitation",
                "End of Term Assessment"
        };
        String[] nurseryDetails = {
                "We are excited to welcome all nursery students to the new school year. Let's make it fun and educational!",
                "Dear parents, we will have a meeting on December 5 to discuss your child's progress. Please attend.",
                "We will visit Manila Zoo on December 10. Permission slips must be submitted by December 3.",
                "Please prepare the following art supplies: crayons, coloring books, and safety scissors.",
                "Sports Day will be held on December 15. All students are encouraged to participate!",
                "School will be closed from December 20 to January 3 for the holiday break. Happy holidays!",
                "New storybooks are now available in the library. Visit during reading time!",
                "Please pack healthy snacks for your children: fruits, sandwiches, and water.",
                "Our nursery talent show will be on December 18. Sign up at the front desk!",
                "End of term assessments will begin on December 12. Good luck to all students!"
        };
        String[] nurseryDates = {"Nov 15, 2025", "Nov 20, 2025", "Nov 22, 2025", "Nov 25, 2025", "Nov 28, 2025", "Dec 01, 2025", "Dec 03, 2025", "Dec 05, 2025", "Dec 08, 2025", "Dec 10, 2025"};
        String[] nurseryTimes = {"8:00 AM", "9:00 AM", "10:00 AM", "11:00 AM", "1:00 PM", "2:00 PM", "8:30 AM", "9:30 AM", "10:30 AM", "11:30 AM"};

        for (int i = 0; i < 3; i++) {
            ModelAnnouncement announcement = new ModelAnnouncement(
                    nurseryTitles[i],
                    nurseryDetails[i],
                    nurseryDates[i],
                    nurseryTimes[i],
                    "Nursery"
            );
            DatabaseManager.createAnnouncement(announcement);
        }

        String[] kinderTitles = {
                "Kinder Class Orientation",
                "Science Fair Announcement",
                "Reading Program Launch",
                "School Uniform Reminder",
                "Math Competition",
                "Christmas Party Details",
                "Computer Lab Schedule",
                "Health and Safety Protocols",
                "Parent Volunteer Program",
                "Graduation Ceremony Info"
        };
        String[] kinderDetails = {
                "Kinder class orientation will be held on November 18. All parents are invited to attend.",
                "Our annual science fair will be on December 8. Start preparing your projects!",
                "We are launching a new reading program. Each student will receive a reading kit.",
                "Reminder: Please ensure your child wears the proper school uniform daily.",
                "Kinder students can join the math quiz bee on December 12. Registration is now open!",
                "Our Christmas party will be on December 19. Please bring food to share!",
                "Computer lab sessions are scheduled every Tuesday and Thursday from 2-3 PM.",
                "Please review the updated health protocols. Student safety is our priority.",
                "We need parent volunteers for our upcoming events. Sign up at the office!",
                "Kinder graduation ceremony will be on March 20, 2026. More details to follow."
        };
        String[] kinderDates = {"Nov 16, 2025", "Nov 19, 2025", "Nov 23, 2025", "Nov 26, 2025", "Nov 29, 2025", "Dec 02, 2025", "Dec 04, 2025", "Dec 06, 2025", "Dec 09, 2025", "Dec 11, 2025"};
        String[] kinderTimes = {"8:00 AM", "9:00 AM", "10:00 AM", "11:00 AM", "1:00 PM", "2:00 PM", "3:00 PM", "8:30 AM", "9:30 AM", "10:30 AM"};

        for (int i = 0; i < 3; i++) {
            ModelAnnouncement announcement = new ModelAnnouncement(
                    kinderTitles[i],
                    kinderDetails[i],
                    kinderDates[i],
                    kinderTimes[i],
                    "Kinder"
            );
            DatabaseManager.createAnnouncement(announcement);
        }


        String[] prepTitles = {
                "Prep Class Welcome Message",
                "Academic Excellence Awards",
                "Community Service Program",
                "School Musical Auditions",
                "Leadership Training Workshop",
                "Study Skills Seminar",
                "Career Day Announcement",
                "School Magazine Submissions",
                "Environmental Awareness Campaign",
                "Moving Up Ceremony"
        };
        String[] prepDetails = {
                "Welcome to Prep class! This year we focus on preparing you for elementary school.",
                "Outstanding students will be recognized at our awards ceremony on December 16.",
                "Join our community service program. We will visit a local nursing home on December 14.",
                "Auditions for the school musical are on December 7. All prep students are welcome!",
                "Leadership workshop for prep students will be held on December 9. Sign up now!",
                "Learn effective study techniques at our seminar on December 11. Parents welcome!",
                "Career Day is on December 13. Various professionals will share their experiences.",
                "Submit your poems, stories, or artwork for our school magazine by December 10.",
                "Join our tree-planting activity on December 17. Let's help save the environment!",
                "Moving up ceremony for prep students will be on March 25, 2026. Save the date!"
        };
        String[] prepDates = {"Nov 17, 2025", "Nov 21, 2025", "Nov 24, 2025", "Nov 27, 2025", "Nov 30, 2025", "Dec 01, 2025", "Dec 05, 2025", "Dec 07, 2025", "Dec 10, 2025", "Dec 12, 2025"};
        String[] prepTimes = {"8:00 AM", "9:00 AM", "10:00 AM", "11:00 AM", "1:00 PM", "2:00 PM", "3:00 PM", "8:30 AM", "9:30 AM", "10:30 AM"};

        for (int i = 0; i < 3; i++) {
            ModelAnnouncement announcement = new ModelAnnouncement(
                    prepTitles[i],
                    prepDetails[i],
                    prepDates[i],
                    prepTimes[i],
                    "Prep"
            );
            DatabaseManager.createAnnouncement(announcement);
        }
    }
}