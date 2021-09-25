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
    public static final long TIME_PER_DAY = 86400000l; //là thời gian một ngày tính theo giây
    public static final long REFRESH_EXP_DATE = 259200000l;//là thời gian 3*một ngày tính theo giây
    public static final long ACCESS_EXP_DATE = 10800000l;//là thời gian 3 * một giờ tính theo giây


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
}
