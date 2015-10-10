package frontend.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    // TODO: http://fruzenshtein.com/spring-mvc-security-mysql-hibernate/

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        // TODO: load user data via the rest interface
        boolean enabled = true;
        boolean accountNonExpired = true;
        boolean credentialsNonExpired = true;
        boolean accountNonLocked = true;
        List<GrantedAuthority> authList = new ArrayList<>();
        authList.add(new SimpleGrantedAuthority("ROLE_USER"));
        return new User("alma", "alma", enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authList);
    }

    /*
    public Collection<? extends GrantedAuthority> getAuthorities(Integer role) {
        List<GrantedAuthority> authList = getGrantedAuthorities(getRoles(role));
        return authList;
    }
    public List<String> getRoles(Integer role) {
    }
    public static List<GrantedAuthority> getGrantedAuthorities(List<String> roles) {
    }*/

}