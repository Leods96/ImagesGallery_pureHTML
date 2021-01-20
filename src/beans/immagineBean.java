package beans;

import java.util.Date;

public class immagineBean {
	private int idImmagine;
	private String titolo;
	private Date data;
	private String descrizione;
	private String path;
	
	public int getIdImmagine() {
		return idImmagine;
	}
	public void setIdImmagine(int idImmagine) {
		this.idImmagine = idImmagine;
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
	public String getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
}
