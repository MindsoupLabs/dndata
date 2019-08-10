package net.mindsoup.dndata.models.dao;

import net.mindsoup.dndata.enums.ObjectStatus;

import javax.persistence.*;

/**
 * Created by Valentijn on 10-8-2019
 */
@Entity
@Table(name = "object_status")
public class ObjectStatusDAO {
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Long id;
	//private User editor;
	@Enumerated(EnumType.STRING)
	private ObjectStatus status;
	private String comment;
	//private DataObject object;
	private Long editor_id;
	private Long object_id;
	private int object_revision;

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

	public Long getEditor_id() {
		return editor_id;
	}

	public void setEditor_id(Long editor_id) {
		this.editor_id = editor_id;
	}

	public Long getObject_id() {
		return object_id;
	}

	public void setObject_id(Long object_id) {
		this.object_id = object_id;
	}

	public int getObject_revision() {
		return object_revision;
	}

	public void setObject_revision(int object_revision) {
		this.object_revision = object_revision;
	}
}
