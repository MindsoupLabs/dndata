package net.mindsoup.dndata.models;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.persistence.*;

/**
 * Created by Valentijn on 7-8-2019
 */
@Entity
@Table(name = "user_rights")
public class UserRight implements GrantedAuthority {

	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Long id;

	@Column(name = "user_id")
	private Long userId;

	private String right;

	@Override
	public String getAuthority() {
		return right;
	}

	public String getRight() {
		return right;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public void setRight(String right) {
		this.right = right;
	}
}
