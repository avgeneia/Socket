package com.mhms.security;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.mhms.sqlite.entities.Account;
import com.mhms.sqlite.entities.QAccount;
import com.mhms.sqlite.entities.QUserRole;
import com.mhms.sqlite.entities.UserRole;
import com.mysema.query.jpa.impl.JPAQuery;

// DB에 연동되게끔 하는 곳
@Service("userDetailsService")  // 빈 등록하기
public class CustomUserDetailsService implements UserDetailsService {

	@PersistenceContext
	EntityManager entityManager;
	
	// 시큐리티의 내용 외 파라미터를 추가하고 싶을 때, 아래 사용
    //  제약조건: Controller 에서 Auth를 점검할 때, UserCustom 으로 받아야 함.
    //  예) (변경 전) @AuthenticationPrincipal User user => (변경 후) @AuthenticationPrincipal UserCustom user
    boolean enabled = true;
    boolean accountNonExpired = true;
    boolean credentialsNonExpired = true;
    boolean accountNonLocked = true;
	
    @SuppressWarnings("unused")
	@Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		JPAQuery query = new JPAQuery(entityManager);
		
		QAccount account = QAccount.account;
		QUserRole userRole = QUserRole.userRole;
		
		//로그인 사용자 인증
		Account user = query.from(account)
						    .where(account.usernm.eq(username))
						    .singleResult(account);

		query = new JPAQuery(entityManager);
		
		List<UserRole> userRoleList = query.from(userRole)
										   .join(userRole.account, account)
										   .where(userRole.account.uid.eq(user.getUid()))
										   .list(userRole);
		
		ArrayList<Integer> bidList = new ArrayList<>();
		
		for(int i = 0; i < userRoleList.size(); i++) {
			bidList.add(userRoleList.get(i).getBuilding().getBid());
		}
		
        if(user == null){
            throw new UsernameNotFoundException("UsernameNotFoundException");
        }

        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority(user.getRole()));

        UserContext accountContext = new UserContext(user.getUsernm(), user.getUserpw()
        		, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked
        		, roles, user.getUid(), bidList);
        
        return accountContext;
    }
}