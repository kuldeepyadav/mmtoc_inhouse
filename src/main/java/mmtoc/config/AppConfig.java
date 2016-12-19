package mmtoc.config;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBuilder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import mmtoc.context.ApplicationContextProvider;
import mmtoc.dao.IMmtocRequestDao;
import mmtoc.dao.MmtocRequestDao;
import mmtoc.model.MmtocRequest;



@Configuration 
@EnableTransactionManagement
public class AppConfig { 
	@Value("${db.username}")
	private String dbUserName;
	@Value("${db.url}")
	private String dbUrl;
	@Value("${db.password}")
	private String dbPassword;
	
	
	@Bean  
        public IMmtocRequestDao mmtocRequestDao() {  
           return new MmtocRequestDao();  
        }
	@Bean
	public HibernateTemplate hibernateTemplate() {
		return new HibernateTemplate(sessionFactory());
	}
	@Bean
	public SessionFactory sessionFactory() {
		return new LocalSessionFactoryBuilder(getDataSource())
		   .addAnnotatedClasses(MmtocRequest.class)
		   .buildSessionFactory();
	}
	@Bean
	public DataSource getDataSource() {
	    BasicDataSource dataSource = new BasicDataSource();
	    dataSource.setDriverClassName("com.mysql.jdbc.Driver");
	    dataSource.setUrl(dbUrl);
	    dataSource.setUsername(dbUserName);
	    dataSource.setPassword(dbPassword);
	    dataSource.setTestOnBorrow(true);
	    dataSource.setValidationQuery("SELECT 1");
	 
	    return dataSource;
	}
	@Bean
	public HibernateTransactionManager hibTransMan(){
		return new HibernateTransactionManager(sessionFactory());
	}
	
	@Bean
	public ApplicationContextProvider applicationContextProvider(){
		return new ApplicationContextProvider();
	}
	
	@Bean
	public Config config(){
		return new Config();
	}
} 
