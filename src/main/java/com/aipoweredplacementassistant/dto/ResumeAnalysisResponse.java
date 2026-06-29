package com.aipoweredplacementassistant.dto;

import java.util.List;

public class ResumeAnalysisResponse {
	
    private String name;
    private String email;
    private String phone;
    private List<String> skills;
    private Integer experienceYears;
    private List<String> education;
    private String summary;
    
    
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public List<String> getSkills() {
		return skills;
	}
	public void setSkills(List<String> skills) {
		this.skills = skills;
	}
	public Integer getExperienceYears() {
		return experienceYears;
	}
	public void setExperienceYears(Integer experienceYears) {
		this.experienceYears = experienceYears;
	}
	public List<String> getEducation() {
		return education;
	}
	public void setEducation(List<String> education) {
		this.education = education;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
    
    
    

}
