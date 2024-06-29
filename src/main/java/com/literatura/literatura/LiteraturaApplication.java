package com.literatura.literatura;

import com.literatura.literatura.controller.Menu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class LiteraturaApplication {

	@Autowired
	private Menu menu;

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(LiteraturaApplication.class, args);
		LiteraturaApplication app = context.getBean(LiteraturaApplication.class);
		app.ejecutarMenu();
	}

	public void ejecutarMenu() {
		menu.mostrarMenu();
	}
}
