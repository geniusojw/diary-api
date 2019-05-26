package org.jerrioh.diary.domain;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "diary_group")
public class DiaryGroup {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "diary_group_id")
	private long diaryGroupId;

	@Column(name = "diary_group_name")
	private String diaryGroupName;
	
	@Column(name = "host_author_id")
	private String hostAuthorId;

	@Column(name = "keyword")
	private String keyword;
	
	@Column(name = "max_author_count")
	private int maxAuthorCount;
	
	@Column(name = "language")
	private String language;
	
	@Column(name = "country")
	private String country;
	
	@Column(name = "time_zone_id")
	private String timeZoneId;

	@Column(name = "start_time")
	private Timestamp startTime;

	@Column(name = "end_time")
	private Timestamp endTime;

	public long getDiaryGroupId() {
		return diaryGroupId;
	}

	public String getDiaryGroupName() {
		return diaryGroupName;
	}

	public void setDiaryGroupName(String diaryGroupName) {
		this.diaryGroupName = diaryGroupName;
	}

	public String getHostAuthorId() {
		return hostAuthorId;
	}

	public void setHostAuthorId(String hostAuthorId) {
		this.hostAuthorId = hostAuthorId;
	}

	public int getMaxAuthorCount() {
		return maxAuthorCount;
	}

	public void setMaxAuthorCount(int maxAuthorCount) {
		this.maxAuthorCount = maxAuthorCount;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getTimeZoneId() {
		return timeZoneId;
	}

	public void setTimeZoneId(String timeZoneId) {
		this.timeZoneId = timeZoneId;
	}

	public Timestamp getStartTime() {
		return startTime;
	}

	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}

	public Timestamp getEndTime() {
		return endTime;
	}

	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}

	public void setDiaryGroupId(long diaryGroupId) {
		this.diaryGroupId = diaryGroupId;
	}
}
