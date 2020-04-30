package config;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("config")
public class fakemonConfig {

		@Bean
		public BasicDataSource dataSource(){
			BasicDataSource dataSource = new BasicDataSource();
			dataSource.setDriverClassName("com.mysql.jdbc.Driver"); 
			dataSource.setUrl("jdbc:mysql://localhost:3306/fakemon"); 
			dataSource.setUsername("root"); 
			dataSource.setPassword(""); 
			dataSource.setMaxTotal(10);
			return dataSource;
		}

	
}
