package com.barcode.mobile.common;


/**
 * 分页信息
 * @author linyaojian
 *
 */
public class PageInfo {
	
	public static final String PAGE_NO="pageNo";
	public static final String PAGE_SIZE="pageSize";
	
	public static final int MODE_FIRST_PAGE=0x10;
	public static final int MODE_PRE_PAGE=0x11;
	public static final int MODE_NEXT_PAGE=0x12;
	public static final int MODE_LAST_PAGE=0x13;

	private int pageNo=1;	//页码
	private int pageSize=10;	//每页条数
	private int totalPageCount=0;	//总页数
	private int totalCount=0;	//总条数
	private int start=0;	//当前页显示第start条到第end条
	private int end=0;
	
	public PageInfo(){}
	
	public PageInfo(int pageNo, int pageSize, int totalPageCount, int totalCount) {
		this.pageNo = pageNo;
		this.pageSize = pageSize;
		this.totalPageCount = totalPageCount;
		this.totalCount = totalCount;
		
		this.start=(this.pageNo-1)*this.pageSize+1;
		if(this.pageNo<this.totalPageCount){
			this.end=this.pageNo*this.pageSize;
		}else{
			this.end=this.totalCount;
		}
	}
	
	public PageInfo(PageInfo page, int mode){
		switch(mode){
			case PageInfo.MODE_FIRST_PAGE:{
				this.pageNo=1;
				break;
			}
			case PageInfo.MODE_PRE_PAGE:{
				this.pageNo=page.getPageNo()-1;
				
				break;
			}
			case PageInfo.MODE_NEXT_PAGE:{
				this.pageNo=page.getPageNo()+1;
				break;
			}
			case PageInfo.MODE_LAST_PAGE:{
				this.pageNo=page.getTotalPageCount();
				break;
			}
			default:break;
		}
		this.pageSize=page.getPageSize();
	}
	
	public String getPageInfoString(){
		if(totalPageCount>0){
			return "第"+this.pageNo+"页，共"+this.totalPageCount+"页，共"+this.totalCount+"条记录，显示"+this.start+"到"+this.end+"条记录.";
		}else{
			return "无记录.";
		}
	}
	
	public boolean canNextPage(){
		if(pageNo==totalPageCount){
			return false;
		}
		return true;
	}
	
	public boolean canPrePage(){
		if(1==pageNo){
			return false;
		}
		return true;
	}
	
	public int getPageNo() {
		return pageNo;
	}
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getTotalPageCount() {
		return totalPageCount;
	}
	public void setTotalPageCount(int totalPageCount) {
		this.totalPageCount = totalPageCount;
	}
	public int getTotalCount() {
		return totalCount;
	}
	public int getStart() {
		return start;
	}
	public int getEnd() {
		return end;
	}
	
	
}
