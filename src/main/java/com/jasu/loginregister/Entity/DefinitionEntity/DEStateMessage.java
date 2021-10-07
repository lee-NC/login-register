package com.jasu.loginregister.Entity.DefinitionEntity;

public class DEStateMessage {

    //state
    public static final String STATE_CREATE = "CREATE";
    public static final String STATE_APPLY = "APPLY";
    public static final String STATE_APPROVED = "APPROVED";
    public static final String STATE_CANCELED = "CANCELED";
    public static final String STATE_REJECTED = "REJECTED";

    public static final String STATE_INVITING = "INVITING";
    public static final String STATE_WAITING = "WAITING";
    public static final String STATE_PROCESSING = "PROCESSING";
    public static final String STATE_DONE = "DONE";

//    state:
//    CREATE: tutor/student create a class
//    APPLY: tutor/student apply to a class
//    APPROVED: tutor/student approve a student/tutor for their class
//    CANCELED: tutor/student canceled an application to a class
//    REJECTED: tutor/student rejected student/tutor

//    INVITED: student invite tutor to teach a class
//    WAITING: class is waiting for student or tutor sign up
//    STARTING: class is starting
//    PROCESSING: class is processing
//    DONE: class is done

    //define number
    public static final int YEAR_ACHIEVEMENT = 1970;//thoi gian nam bat dau, gioi han
    public static final int MAX_FAILED_LOGIN = 5;//gioi han dang nhap
    public static final long TIME_PER_HOUR = 3600000l; //là thời gian một giờ tính theo giây
    public static final long REFRESH_EXP_DATE = 259200000l;//là thời gian 3 ngày tính theo giây
    public static final long OTP_TIME_TRACKING = 900000l;//là thời gian 15 phút tính theo giây
    public static final long DELAY_TIME_REFRESH = 300000l;//là thời gian 5 phút tính theo giây



    //Define String
    public static final String USER = "user";//string in header
    public static final String SECRET = "daycaidaynaychinhlachukycuabandungdelorangoaidaynhenguyhiemchetnguoidayhihihi";

    //Define message
    public static final String ACTION_UNSUCCESSFULLY= "Have some wrong, please try it later";
    public static final String ACTION_SUCCESSFULLY= "Ok, your action has been successfully recorded";
    public static final String ACTION_APPLY_SUCCESSFUL = "You have applied for this class";
    public static final String ACTION_APPLY_BEFORE = "You have applied for this class before";
    public static final String ACTION_APPLY_NOT_FOUND = "This user could not be found applying for this class";
    public static final String ACTION_CANCEL_APPLY = "You cancel application to this class";


    //Define email subject
    public static final String VERIFICATION_SUBJECT = "Verify email.";
    public static final String WELCOME_SUBJECT = "Welcome to JASU";
    public static final String TUTOR_SUBJECT = "Registry tutor successfully";
    public static final String STUDENT_SUBJECT = "Registry student successfully";
    public static final String CREATE_CLASS_SUBJECT = "Create class successfully";
    public static final String APPLY_CLASS_SUBJECT = "Apply class successfully";
    public static final String HAVE_NEW_APPLICATION_SUBJECT = "Have new application";
    public static final String APPROVE_STUDENT_SUBJECT = "Approve student successfully";
    public static final String APPROVE_TUTOR_SUBJECT = "Approve tutor successfully";
    public static final String USER_APPROVED_SUBJECT = "Be approved to a class";
    public static final String USER_REJECTED_SUBJECT = "Apply class unsuccessfully";
    public static final String CANCEL_APPLY_CLASS_SUBJECT = "Apply class successfully";
    public static final String CLASS_BEGINNING_SUBJECT = "Class is beginning";
    public static final String CLASS_CREATE_CANCEL_SUBJECT = "A class you created was cancelled";


    //Define email body
    public static String VERIFICATION_CONTENT = "Verifying that you are registering with us.\n" +
            "Verification code: ";

    public static final String WELCOME_CONTENT = "Welcome to JASU!!\n" +
            "JASU is a network of connections between students and tutors," +
            " where you can choose the right tutor without the middleman.\n" +
            "Have a good time!ヾ(≧▽≦*)o";

    public static final String TUTOR_CONTENT = "Now you are a tutor!\n" +
            "Find or create several classes to find the suitable student for you.\n" +
            "Have a nice time with JASU!ヾ(≧▽≦*)o";

    public static final String STUDENT_CONTENT = "Now you are a student!\n" +
            "Find or create several classes to find the suitable tutor for you.\n" +
            "Have a nice time with JASU!ヾ(≧▽≦*)o";

    public static final String CREATE_CLASS_CONTENT = "You create class successfully!\n" +
            "Please wait a while for others to sign up for your class!\n" +
            "Classes will be open for 2 weeks.\n" +
            "When the time is up and you have not approved any one, you can extend the time for another 2 weeks until you find the right one.\n" +
            "Have a nice time with JASU!ヾ(≧▽≦*)o";

    public static final String APPLY_CLASS_CONTENT = "Apply class successfully\n" +
            "Please wait for the creator of this class to approve you.\n" +
            "We will notify you as soon as possible\n" +
            "If you are not approved when the class expires, the registration fee will be refunded to you\n" +
            "Have a nice time with JASU!ヾ(≧▽≦*)o";

    public static final String HAVE_NEW_APPLICATION_CONTENT = "You have a new application\n" +
            "Please check the created class information to see new application\n" +
            "Have a nice time with JASU!ヾ(≧▽≦*)o";

    public static final String APPROVE_STUDENT_CONTENT = "You have approved this student for class!\n" +
            "The current student is already on the approved list.\n" +
            "When the class begin, please check your class in your classroom manager's processing classes section to see tutor information.\n" +
            "Have a good time with JASU!ヾ(≧▽≦*)o";

    public static final String APPROVE_TUTOR_CONTENT = "You have approved this tutor for class!\n" +
            "Class has begun!\n" +
            "Please check your class in your classroom manager's processing classes section to see tutor information.\n" +
            "Have a good time with JASU!ヾ(≧▽≦*)o";

    public static final String USER_APPROVED_CONTENT = "You have been approved into a class\n" +
            "Please check the information in the approved class to see the class information\n" +
            "If you want to cancel your application, we will refund your application fee\n" +
            "Have a good time with JASU!ヾ(≧▽≦*)o";

    public static final String USER_REJECTED_CONTENT =  "So sad, class has started without you\n" +
            "We have already refunded you, please check your account again\n" +
            "There are still many classes waiting for you\n" +
            "Please search for more suitable classes in JASU\n" +
            "Have a good time with JASU!ヾ(≧▽≦*)o";
    public static final String CANCEL_APPLY_CLASS_CONTENT = "You have successfully canceled your class application!\n" +
            "The registration amount has been refunded to your account.\n" +
            "If you want to re-register for this class, please check in the canceled classes.\n" +
            "Have a good time with JASU!ヾ(≧▽≦*)o";


    public static final String CLASS_BEGINNING_CONTENT = "A class has begun!\n" +
            "Please check your class in your classroom manager's processing classes section to see tutor information.\n" +
            "Have a good time with JASU!ヾ(≧▽≦*)o";

    public static final String CLASS_CREATE_CANCEL_CONTENT = "A class you created has been overdue and cancelled, the users applied with you have been cancelled.\n" +
            "Please check the class information in the list of canceled classes\n" +
            "If you want to recreate the class, choose to recreate the class so other users can apply in your class\n" +
            "Have a good time with JASU!ヾ(≧▽≦*)o";

}
