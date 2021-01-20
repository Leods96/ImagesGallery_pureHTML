package beans;

import java.util.Date;

public class albumBean {
	private int idAlbum;
	private String titolo;
	private Date data;
	
	public int getIdAlbum() {
		return idAlbum;
	}
	public void setIdAlbum(int idAlbum) {
		this.idAlbum = idAlbum;
	}
	public String getTitolo() {
		return titolo;
	}
	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}
	public Date getData() {
		return data;
	}
	public void setData(Date data) {
		this.data = data;
	}
}
