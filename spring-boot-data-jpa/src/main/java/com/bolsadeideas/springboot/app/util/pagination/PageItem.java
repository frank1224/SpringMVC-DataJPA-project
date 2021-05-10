package com.bolsadeideas.springboot.app.util.pagination;

public class PageItem {

	private int pageNumber;
	private boolean currentpageNumber;


	public PageItem(int pageNumber, boolean currentpageNumber) {
		this.pageNumber = pageNumber;
		this.currentpageNumber = currentpageNumber;
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public boolean isCurrentpageNumber() {
		return currentpageNumber;
	}

	

}
