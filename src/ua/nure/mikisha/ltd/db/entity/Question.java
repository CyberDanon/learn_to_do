package ua.nure.mikisha.ltd.db.entity;

import java.util.List;

public class Question {
	private int id;
	private int test;
	private int rac;
	private String text;
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	private List<Answer> answers;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getTest() {
		return test;
	}
	public void setTest(int test) {
		this.test = test;
	}
	public int getRac() {
		return rac;
	}
	public void setRac(int rac) {
		this.rac = rac;
	}
	public List<Answer> getAnswers() {
		return answers;
	}
	public void setAnswers(List<Answer> answers) {
		this.answers = answers;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((answers == null) ? 0 : answers.hashCode());
		result = prime * result + id;
		result = prime * result + rac;
		result = prime * result + test;
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
		Question other = (Question) obj;
		if (answers == null) {
			if (other.answers != null)
				return false;
		} else if (!answers.equals(other.answers))
			return false;
		if (id != other.id)
			return false;
		if (rac != other.rac)
			return false;
		if (test != other.test)
			return false;
		return true;
	}
	
}
