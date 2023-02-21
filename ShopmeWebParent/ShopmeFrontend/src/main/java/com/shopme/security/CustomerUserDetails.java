package com.shopme.security;

import com.shopme.common.entity.Customer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class CustomerUserDetails implements UserDetails {

	//UserDetails đại diện cho một principal nhưng theo một cách mở rộng và cụ thể hơn, cung cap cac phuong thuc nhu
	// trả về danh sách các quyền của người dùng , trả về password đã dùng trong qúa trình xác thực, trả về username đã dùng trong qúa trình xác thực

	private final Customer customer;
	
	public CustomerUserDetails(Customer customer) {
		this.customer = customer;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return null;
	}

	@Override
	public String getPassword() {
		return customer.getPassword();
	}

	@Override
	public String getUsername() {
		return customer.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return customer.isEnabled();
	}

	public String getFullName() {
		return customer.getFirstName() + " " + customer.getLastName();
	}

	public Customer getCustomer() {
		return this.customer;
	}
}
