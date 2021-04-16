package pers.mortal.learn.springdata.security;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.Serializable;

public class ExamplePermissionEvaluator implements PermissionEvaluator {
    private static final GrantedAuthority ADMIN_AUTHORITY = new SimpleGrantedAuthority("ROLE_Admin");

    @Override//将要评估的对象作为第二个参数
    public boolean hasPermission(Authentication authentication, Object filter, Object permission) {

        if(filter instanceof  Integer){
            if("delete".equals(permission)){
                return authentication.getAuthorities().contains(ADMIN_AUTHORITY)
                        || (Integer)filter > 0;
            }
        }

        throw new UnsupportedOperationException(
                "hasPermission not supported for object <" + filter + ">" +
                        "and permission < " + permission + ">");
    }

    @Override//目标对象ID可以得到时候才有用，并将ID作为Serializable传入第二个参数。
    public boolean hasPermission(Authentication authentication, Serializable serializable, String s, Object o) {
        throw new UnsupportedOperationException();
    }
}
