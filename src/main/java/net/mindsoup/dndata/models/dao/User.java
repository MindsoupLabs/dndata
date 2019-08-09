package net.mindsoup.dndata.models.dao;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Valentijn on 8-8-2019
 */
@Entity
@Table(name = "users")
public class User {

	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Long id;

	private String name;
	private String email;
	private String password;
	private boolean active;

	@OneToMany(mappedBy = "userId", fetch = FetchType.EAGER)
	private List<UserRight> roles;

	@Transient
	private List<String> rights;

	public List<String> getRights() {
		return rights;
	}

	public void setRights(List<String> rights) {
		this.rights = rights;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public List<UserRight> getRoles() {
		return roles;
	}

	public void setRoles(List<UserRight> roles) {
		this.roles = roles;
	}

	public boolean hasRight(String right) {
		if(roles == null) {
			return false;
		}

		return roles.stream().anyMatch(r -> r.getRole().equalsIgnoreCase(right));
	}
}
