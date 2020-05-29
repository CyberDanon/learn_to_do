package ua.nure.mikisha.ltd;

/**
 * Path holder (jsp pages, controller commands).
 * @author Mikisha Danylo
 */
public final class Path {
	//////////////////MY PATHS////////////////////////////////////
	
	
	public static final String PAGE_SETTINGS = "/WEB-INF/jsp/settings.jsp";
	public static final String PAGE_RECOMS = "WEB-INF/jsp/common/recom_list.jsp";
	public static final String VIEW_RECOMS = "/controller?command=recomendations";
	public static final String VIEW_USER_LIST = "/controller?command=user_list";
	public static final String PAGE_EDITION_INFO = "/WEB-INF/jsp/common/edition_info.jsp";
	public static final String PAGE_USER_INFO = "/WEB-INF/jsp/common/personal_data.jsp";
	public static final String PAGE_USER_LIST = "/WEB-INF/jsp/admin/user_list.jsp";
	public static final String PAGE_EDITION_ADD = "/WEB-INF/jsp/admin/edition_add.jsp";
	public static final String PAGE_EDITION_UPDATE = "/WEB-INF/jsp/admin/edition_update.jsp";
	public static final String PAGE_PERSONAL_DATA = "WEB-INF/jsp/common/personal_data.jsp";
	public static final String PAGE_PUBLICS="/WEB-INF/jsp/admin/publics.jsp";
	
	//------------------------------------------------------------
	public static final String PAGE_ERROR_PAGE = "/WEB-INF/pages/error_page.jsp";
	public static final String PAGE_LOGIN = "/WEB-INF/pages/sign-in.jsp";
	public static final String PAGE_REGISTER = "/WEB-INF/pages/sign-up.jsp";
	public static final String PAGE_USER_UPDATE = "/WEB-INF/pages/change-password-page.jsp";
	public static final String PAGE_COURSE_INFO = "";
	public static final String FORWARD_TEST_EDIT = "/controller?command=forward_test_edit&method=GET";
	public static final String PAGE_COURSE_EDIT = "";
	public static final String FORWARD_THEORY_EDIT = "controller?command=forward_theory_edit&method=GET&theory_id=";
	public static final String PAGE_THEORY_EDIT = "";
	public static final String FORWARD_COURSE_EDIT = "/controller?command=forward_course_edit&method=GET&course_id=";
	public static final String FORWARD_TO_LOGIN = "controller?command=login_forward&method=GET";
	public static final String PAGE_TEST_EDIT = "";
	public static final String PAGE_COURCES = "";
	public static final String FORWARD_GET_TEACHES = "/controller?command=get_courses&type=teach&method=GET";
	public static final String FORWARD_GET_RECOMS = "/controller?command=get_courses?type=recoms&method=GET";
	public static final String FORWARD_GET_SUBS = "/controller?command=get_cources?type=subs&method=GET";
	public static final String PAGE_COURSE_DISPLAY = "/WEB-INF/pages?course-passing-page.jsp";
	//public static final String PAGE_THEORY_DISPLAY = "";
	//public static final String PAGE_TEST_DISPLAY = "";
	public static final String FORWARD_COURSE_DISPLAY = "/controller?command=display_course&method=GET&course_id=";
	public static final String FORWARD_THEORY_DISPLAY = "/controller?command=display_theorymethod=GET&theory_id=";
	public static final String PAGE_MESSAGES = "";
	public static final String PAGE_DISPLAY_MESSAGE = "";
	public static final String FORWARD_GET_MESSAGES = "/controller?command=get_messages&method=GET";
	public static final String FORWARD_GET_ALL_USERS = "/controller?command=get_users&method=GET";
	public static final String PAGE_GET_ALL_USERS = "";
	public static final String FORWARD_GET_ALL_UNBANNED_COURCES = "/controller?command=get_all_unbanned_cources&method=GET";
	public static final String FORWARD_GET_ALL_BANNED_COURCES = "/controller?command=get_all_banned_cources&method=GET";
	public static final String PAGE_NOTIFICATE = "";
	public static final String PAGE_MAIN = "/WEB-INF/index.jsp";
	public static final String PAGE_COURSE_PREVIEW = "/WEB-INF/pages/course-details-page.jsp";
	public static final String PAGE_TEST_DISPLAY = "";
	public static final String PAGE_CREATE_COURSE = "/WEB-INF/pages/create-course-page.jsp";
	public static final String FORWARD_GET_COURSES = "/controller?command=get_cources_command&method=GET";
	public static final String PAGE_FAQ = "/WEB-INF/pages/faq-page.jsp";
	public static final String PAGE_USERS_TOP = "/WEB-INF/pages/top-students-page.jsp";
	public static final String PAGE_FAQ_ANSWER = "/WEB-INF/pages/faq-create-answer.jsp";
	public static final String FORWARD_FAQ = "/controller?command=faq&method=GET";
	public static final String FORWARD_PERSONAL = "/controller?command=personal&method=GET";
	public static final String PAGE_PERSONAL = "/WEB--INF/pages/user-profile-page.jsp";
}