package net.mindsoup.dndata.models.dao;

import net.mindsoup.dndata.enums.ObjectStatus;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Valentijn on 10-8-2019
 */
@Entity
@Table(name = "object_status")
public class ObjectStatusDAO {
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Long id;
	@Enumerated(EnumType.STRING)
	private ObjectStatus status;
	private String comment;
	@Column(name = "editor_id")
	private Long editorId;
	@Column(name = "object_id")
	private Long objectId;
	@Column(name = "object_revision")
	private int objectRevision;

	@Column(name = "status_date")
	private Date date = new Date();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ObjectStatus getStatus() {
		return status;
	}

	public void setStatus(ObjectStatus status) {
		this.status = status;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Long getEditorId() {
		return editorId;
	}

	public void setEditorId(Long editorId) {
		this.editorId = editorId;
	}

	public Long getObjectId() {
		return objectId;
	}

	public void setObjectId(Long objectId) {
		this.objectId = objectId;
	}

	public int getObjectRevision() {
		return objectRevision;
	}

	public void setObjectRevision(int objectRevision) {
		this.objectRevision = objectRevision;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
}
