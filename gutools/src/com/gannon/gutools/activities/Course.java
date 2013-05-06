package com.gannon.gutools.activities;

public class Course {
	private long id;
	private String name;
	private String professor;
	private String info;
	private String credit;
	private String time;
	private int isM;
	private int isT;
	private int isW;
	private int isTH;
	private int isF;
	  
	public String getProfessor() {
		return professor;
	}

	public void setProfessor(String professor) {
		this.professor = professor;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getCredit() {
		return credit;
	}

	public void setCredit(String credit) {
		this.credit = credit;
	}

	  public long getId() {
	    return id;
	  }

	  public void setId(long id) {
	    this.id = id;
	  }

	  public String getName() {
	    return name;
	  }

	  public void setName(String name) {
	    this.name = name;
	  }

	  // Will be used by the ArrayAdapter in the ListView
	  @Override
	  public String toString() {
	    return name;
	  }

	public int isM() {
		return isM;
	}

	public void setM(int isM) {
		this.isM = isM;
	}

	public int isW() {
		return isW;
	}

	public void setW(int isW) {
		this.isW = isW;
	}

	public int isT() {
		return isT;
	}

	public void setT(int isT) {
		this.isT = isT;
	}

	public int isF() {
		return isF;
	}

	public void setF(int isF) {
		this.isF = isF;
	}

	public int isTH() {
		return isTH;
	}

	public void setTH(int isTH) {
		this.isTH = isTH;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
}
