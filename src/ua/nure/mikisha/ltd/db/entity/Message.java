package ua.nure.mikisha.ltd.db.entity;

public class Message {
	private int id;
	private String topic;
	private int course_id;
	private String user_login;
	private String text;
	private long time;
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTopic() {
		return topic;
	}
	public void setTopic(String topic) {
		this.topic = topic;
	}
	public int getCourse_id() {
		return course_id;
	}
	public void setCourse_id(int course_id) {
		this.course_id = course_id;
	}
	public String getUser_login() {
		return user_login;
	}
	public void setUser_login(String user_login) {
		this.user_login = user_login;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + course_id;
		result = prime * result + id;
		result = prime * result + ((text == null) ? 0 : text.hashCode());
		result = prime * result + ((topic == null) ? 0 : topic.hashCode());
		result = prime * result + ((user_login == null) ? 0 : user_login.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Message other = (Message) obj;
		if (course_id != other.course_id)
			return false;
		if (id != other.id)
			return false;
		if (text == null) {
			if (other.text != null)
				return false;
		} else if (!text.equals(other.text))
			return false;
		if (topic == null) {
			if (other.topic != null)
				return false;
		} else if (!topic.equals(other.topic))
			return false;
		if (user_login == null) {
			if (other.user_login != null)
				return false;
		} else if (!user_login.equals(other.user_login))
			return false;
		return true;
	}
}
