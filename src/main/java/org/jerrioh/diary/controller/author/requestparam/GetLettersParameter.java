package org.jerrioh.diary.controller.author.requestparam;

import javax.validation.constraints.Pattern;

import org.jerrioh.diary.domain.AuthorLetter.LetterType;

public class GetLettersParameter {
	public static class Range {
		public static final String INCOMMING = "in";
		public static final String OUTGOING = "out";
		public static final String ALL = "all";
	}
	
	@Pattern(regexp = "in|out|all")
	private String range = Range.ALL;
	private int type = LetterType.NORMAL;

	public String getRange() {
		return range;
	}

	public void setRange(String range) {
		this.range = range;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
}
