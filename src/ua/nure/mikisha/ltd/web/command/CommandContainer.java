package ua.nure.mikisha.ltd.web.command;

import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;

/**
 * Holder for all commands for Command-pattern
 * @author Mikisha Danylo
 */
public class CommandContainer {
	
	private static final Logger LOG = Logger.getLogger(CommandContainer.class);
	
	private static Map<String, Command> commands = new TreeMap<String, Command>();
	
	static {
		//out of control commands
		commands.put("login", new LoginCommand());
		commands.put("ban_user", new BanUserCommand());
		commands.put("ban_course", new BanCourseCommand());
		commands.put("create_answer", new CreateAnswerCommand());
		commands.put("create_message", new CreateMessageCommand());
		commands.put("create_question", new CreateQuestionCommand());
		commands.put("create_user", new CreateUserCommand());
		commands.put("create_test", new CreateTestCommand());
		commands.put("delete_answer", new DeleteAnswerCommand());
		commands.put("delete_message", new DeleteMessageCommand());
		commands.put("delete_question", new DeleteQuestionCommand());
		commands.put("delete_test", new DeleteTestCommand());
		commands.put("delete_theory", new DeleteTheoryCommand());
		//commands.put("delete_course", new DeleteCourseCommand());
		commands.put("display_course", new DisplayCourseCommand());
		commands.put("display_message", new DisplayMessageCommand());
		commands.put("display_test", new DisplayTestCommand());
		commands.put("display_theorical", new DisplayTheoricalCommand());
		commands.put("donate_user", new DonateUserCommand());
		commands.put("error_show", new ErrorShowCommand());
		commands.put("filter", new FilterCommand());
		commands.put("forward_change_pass", new ForwardChangePass());
		commands.put("forward_login", new ForwardLogin());
		commands.put("forward_register", new ForwardRegister());
		commands.put("register", new CreateUserCommand());
		commands.put("forward_to_notificate", new ForwardToNotificate());
		commands.put("forward_to_update_course", new ForwardToUpdateCourse());
		commands.put("forward_to_update_test", new ForwardToUpdateTest());
		commands.put("forward_to_update_theory", new ForwardToUpdateTheory());
		commands.put("get_all_users_command", new GetAllUsersCommand());
		commands.put("get_cources_command", new GetCourcesCommand());
		commands.put("get_course_info_command", new GetCourseInfoCommand());
		commands.put("get_messages_command", new GetMessagesCommand());
		commands.put("get_test_info", new GetTestInfoCommand());
		commands.put("get_theory_info_command", new GetTheoryInfoCommand());
		commands.put("logout", new LogoutCommand());
		commands.put("notificate_subscribers", new NotificateSubscribersCommand());
		commands.put("pass_test", new PassTestCommand());
		commands.put("rate_course", new RateCourseCommand());
		commands.put("safe_unsubscribe", new SafeUnsubscribeCommand());
		commands.put("subscribe", new SubscribeCommand());
		commands.put("unban_user", new UnbanCommand());
		commands.put("unban_course", new UnbanCourseCommand());
		commands.put("unsubscribe", new UnsubscribeCommand());
		commands.put("update_course", new UpdateCourseCommand());
		commands.put("update_test", new UpdateTestCommand());
		commands.put("update_theory", new UpdateTheoryCommand());
		commands.put("update_user", new LoginCommand());
		
		commands.put("main", new ForwardMain());
		commands.put("forward_pass_test", new ForwardPassTest());
		commands.put("forward_to_create_course", new ForwardToCreateCourse());
		commands.put("create_course", new CreateCourseCommand());
		commands.put("forward_to_update_theory", new ForwardToUpdateTh());
		commands.put("faq", new FAQCommand());
		commands.put("users_top", new ForwardRating());
		commands.put("forward_answer", new ForwardFAQAnswer());
		commands.put("ask", new CreateFAQ());
		commands.put("answer_question", new FAQAnswer());
		commands.put("personal", new DisplayUser());
		commands.put("become_admin", new BecomeAdmin());
		commands.put("become_student", new BecomeStudent());
		commands.put("become_teacher", new BecomeTeacher());
		LOG.debug("Command container was successfully initialized");
		LOG.trace("Number of commands --> " + commands.size());
		
	}

	/**
	 * Returns command object with the given name.
	 */
	public static Command get(String commandName) {
		if (commandName == null || !commands.containsKey(commandName)) {
			LOG.trace("Command not found, name --> " + commandName);
			return commands.get("noCommand"); 
		}
		
		return commands.get(commandName);
	}
	
}