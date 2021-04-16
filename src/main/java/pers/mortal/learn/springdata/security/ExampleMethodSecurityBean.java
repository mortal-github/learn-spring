package pers.mortal.learn.springdata.security;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreFilter;
import org.springframework.stereotype.Component;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@Component
public class ExampleMethodSecurityBean {

    //接受String数组，认证用户必须至少具备其中的一个权限才能进行方法调用。
    //否则，抛出Spring Security异常：`AuthenticationException`或`AccessDeniedException`的子列。
    //它们是非检查型异常，但这个异常最终必须要被捕获和处 理。
    //如果被保护的方法是在Web请求中调用的，这个异常会被Spring Security的过滤器自动处理。
    //否则的话，你需要编写代码来处理这个 异常。
    @Secured({"ROLE_Admin"})
    public void useSecured(){
        System.out.println("@Secured");
    }

    @RolesAllowed({"ROLE_Admin"})
    public void useRolesAllowed(){
        System.out.println("@RolesAllowed");
    }


    @PreAuthorize(
            "(hasRole(\"ROLE_Manager\")" +//在方法前验证权限
            "and #count > 0) " +
            "or hasRole(\"ROLE_Admin\")"//增加更详细的安全规则。
    )
    //使用@PostAuthorize时我们可以使用内置的表达式returnObject表示方法的返回值。
    @PostAuthorize(
            "returnObject < 100"//验证返回结果，失败则抛出异常。
    )
    public int useAuthorize(int count){
        System.out.println("@PreAuthorize");
        return count * 10;
    }

    @PreAuthorize("hasAnyRole({\"ROLE_Admin\", \"ROLE_Manager\"})")
    //filterObject是使用@PreFilter和@PostFilter时的一个内置表达式，表示集合中的当前对象。
    //当@PreFilter标注的方法拥有多个集合类型的参数时，需要通过@PreFilter的filterTarget属性指定当前@PreFilter是针对哪个参数进行过滤的。
    @PreFilter(//将输入集合计算结果未false的成员移除。
            filterTarget = "list",
            value = "hasRole(\"ROLE_Admin\") || " +
                    "filterObject > 0"
    )
    @PostFilter(//将输出集合计算结果未false的成员移除。
            "hasRole(\"ROLE_Admin\") || " +
                    "filterObject < 100"
    )
    public List<Integer> useFilter(List<Integer> list){
        return list;
    }

    @PreFilter("hasPermission(filterObject, 'delete')")
    public List<Integer> usePermissionEvaluator(List<Integer> list){
        return list;
    }
}
