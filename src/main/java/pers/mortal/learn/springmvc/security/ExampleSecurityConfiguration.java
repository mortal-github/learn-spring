package pers.mortal.learn.springmvc.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebMvcSecurity
//@EnableWebSecurity注解将会启用Web安全功能。
//是使用Spring MVC开发的，那么就应该考虑使 用@EnableWebMvcSecurity替代它，
//@EnableWebMvcSecurity注解还配置了一 个Spring MVC参数解析解析器（argument resolver），这样的话处理器 方法就能够通过带有@AuthenticationPrincipal注解的参数获 得认证用户的principal（或username）。
//它同时还配置了一个bean， 在使用Spring表单绑定标签库来定义表单时，这个bean会自动添加一 个隐藏的跨站请求伪造（cross-site request forgery，CSRF）token输入 域。
public class ExampleSecurityConfiguration extends WebSecurityConfigurerAdapter {
    //，Spring Security必须配置在一个实现了 WebSecurityConfigurer的bean中，或者（简单起见）扩 展WebSecurityConfigurerAdapter。
    //通过重载WebSecurityConfigurerAdapter的三 个configure()方法来配置Web安全性，这个过程中会使用传递进 来的参数设置行为。

//    @Autowired
//    private DataSource dataSource;

    @Override//通过重载，配置Spring Security的Filter 链
    public void configure(WebSecurity webSecurity)throws Exception{

    }

    @Override//通过重载，配置如何通过拦截器保护 请求
    public void configure(HttpSecurity httpSecurity)throws Exception{
//        httpSecurity
//                .authorizeRequests()
//                    .anyRequest().authenticated()
//                .and().httpBasic();
        httpSecurity
                .httpBasic()
                    .realmName("realmFiled")
                .and().formLogin()
                    .loginPage("/login.jsp")   //用户未登录时，访问资源跳转的登录页面。
                    .loginProcessingUrl("/login") //登录表单form中action的地址，也就是处理认证请求的路径。
                    .usernameParameter("uname") //登录表单form中用户输入框input的name名，不修改则默认未username。
                    .passwordParameter("pword") //登录表达form中密码输入框input的那么名，不修改默认未password。
                    .defaultSuccessUrl("/index.jsp")    //登录认证成功后默认跳转的路径。
                .and().rememberMe()
                    .tokenValiditySeconds(60)
                    .key("Learn-SpringMVC")
                .and().logout()
                    .logoutUrl("/signout")
                    .logoutSuccessUrl("/")
                .and()
                    .authorizeRequests()
                        .antMatchers("/home")
                            .anonymous()
                        .antMatchers(HttpMethod.POST, "/multipart")
                            .authenticated()//注意此时，只能进行**get**请求无法进行**post**请求因为默认开启**csrf**跨域拦截，关闭跨域拦截就ok了
                        .anyRequest()
                            .permitAll()
                .and()
                    .requiresChannel()
                        .antMatchers("/home")
                            .requiresSecure()
              .and().csrf()
                .disable()
                    ;
    }

    @Override//通过重载，配置user-detail服务
    public void configure(AuthenticationManagerBuilder builder)throws Exception{
        // Spring Security 5.0 中新增了多种加密方式，也改变了密码的格式。
        //故需要指定passwordEncoder加密
        builder.inMemoryAuthentication().passwordEncoder(new BCryptPasswordEncoder())
                .withUser("zhongjingwen").password(new BCryptPasswordEncoder().encode("123456789")).roles("Manager", "Admin")//roles方法自动添加`ROLE_`前缀。
                .and()
                .withUser("zhongdongxiao").password(new BCryptPasswordEncoder().encode("987654321")).roles("Manager");

//        //为 了配置Spring Security使用以JDBC为支撑的用户存储，我们可以使 用jdbcAuthentication()。
//        //默认的最少配置要求数据库模式满足以下要求。
//        String DEF_USERS_BY_USERNAME_QUERY =
//                "SELECT username, password, enabled " +
//                        "FROM users " +
//                        "where username = ? ";
//        String DEF_AUTHORITIES_BY_USERNAME_QUERY =
//                "SELECT username, authority " +
//                        "FROM authorities " +
//                        "WHERE username = ? ";
//        String DEF_GROUP_AUTHORITIES_BY_USERNAME_QUERY =
//                "SELECT g.id, g.group_name, ga.authority " +
//                        "FROM groups g, group_members gm, group_authorities ga " +
//                        "WHERE gm.username = ? " +
//                        "   AND g.id = ga.group_id " +
//                        "   AND g.id = gm.group_id";
//
//        builder.jdbcAuthentication().dataSource(dataSource)
//        //将默认的SQL查询替换为自定义的设计时，很重要的一点就是要遵循 查询的基本协议。所有查询都将用户名作为唯一的参数。
//        .usersByUsernameQuery(DEF_USERS_BY_USERNAME_QUERY)
//        .authoritiesByUsernameQuery(DEF_AUTHORITIES_BY_USERNAME_QUERY)//权限查询会选取零行或多行包 含该用户名及其权限信息的数据。
//        .groupAuthoritiesByUsername(DEF_GROUP_AUTHORITIES_BY_USERNAME_QUERY)//群组权限查询会选取零行或多行数据，每行数据中都会包含群组ID、群组名称以及权限。
//        .passwordEncoder(new BCryptPasswordEncoder());
//        //passwordEncoder()方法可以接受Spring Security中 PasswordEncoder接口的任意实现。
//        // Spring Security的加密模块包括 了三个这样的实 现：BCryptPasswordEncoder、NoOpPasswordEncoder和 StandardPasswordEncoder。
//        //public interface PasswordEncoder{
//        //  String encode(CharSequence rawPassword);
//        //  boolean matches(CharSequence rawPassword, String encodedPasword);
//
//        builder.userDetailsService(new UserDetailsService() {
//            @Override
//            public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
//                String username = s;
//                String password = s;
//                List<GrantedAuthority> authorities = new ArrayList<>();
//                authorities.add(new SimpleGrantedAuthority("ROLE_MEMBER"));
//                authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
//                authorities.add(new SimpleGrantedAuthority("ROLE_MANAGER"));
//
//                return new User(username,password,authorities);
//            }
//        })
    }
}
