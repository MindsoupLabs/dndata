package net.mindsoup.dndata.models;

import org.springframework.security.core.GrantedAuthority;

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

	private String role;

	public UserRight() {}

	public UserRight(String role, Long userId) {
		this.role = role;
		this.userId = userId;
	}

	@Override
	public String getAuthority() {
		return role;
	}

	public String getRole() {
		return role;
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

	public void setRole(String role) {
		this.role = role;
	}
}
