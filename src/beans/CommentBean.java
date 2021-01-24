package beans;

public class CommentBean {
	private int idComment;
	private String text;
	private String userName;
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public int getIdComment() {
		return idComment;
	}
	public void setIdComment(int idComment) {
		this.idComment = idComment;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}

}
