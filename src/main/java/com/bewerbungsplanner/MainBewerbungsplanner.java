package com.bewerbungsplanner;


import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@Theme(variant = Lumo.LIGHT)
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class MainBewerbungsplanner implements AppShellConfigurator {

	public static void main(String[] args) {
		SpringApplication.run(MainBewerbungsplanner.class, args);
	}

}
