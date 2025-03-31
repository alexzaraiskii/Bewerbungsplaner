package com.bewerbungsplanner.view;

import com.bewerbungsplanner.model.User;
import com.bewerbungsplanner.service.UserService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Log4j2
@Route("/")
public class LoginView extends VerticalLayout {

    private UserService service;
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    public LoginView(UserService service) {

        this.service = service;

        Dialog signIn = new Dialog();
        signIn.setModal(false);

        LoginForm loginForm = new LoginForm();
        loginForm.setForgotPasswordButtonVisible(false);
        Button createAccount = new Button("Create account");
        createAccount.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        signIn.getFooter().add(createAccount);

        errorMessage(loginForm);

        loginEvent(service, loginForm);

        createAccount(service, createAccount, signIn);

        signIn.add(loginForm);
        signIn.open();
        add(signIn);


    }

    private void loginEvent(UserService service, LoginForm loginForm) {
        loginForm.addLoginListener(loginEvent -> {

            try {
                User user = service.findByName(loginEvent.getUsername());
                boolean match = passwordEncoder.matches(loginEvent.getPassword(), user.getPassword());

                if (match == true) {
                    loginForm.getUI().ifPresent(ui -> {
                        VaadinSession.getCurrent().setAttribute("current_user", service
                                .findByName(loginEvent.getUsername()));
                        ui.navigate(MainView.class);
                    });
                } else
                    loginForm.setError(true);
                loginForm.setEnabled(true);
            } catch (Exception e) {
                loginForm.setError(true);
                loginForm.setEnabled(true);
            }
        });
    }

    private static void errorMessage(LoginForm loginForm) {
        LoginI18n i18n = LoginI18n.createDefault();
        LoginI18n.ErrorMessage i18nErrorMessage = i18n.getErrorMessage();
        i18nErrorMessage.setTitle("Incorrect username or password");
        i18nErrorMessage.setMessage(
                "Check that you have entered the correct username and password and try again.");
        i18n.setErrorMessage(i18nErrorMessage);
        loginForm.setI18n(i18n);
    }

    private void createAccount(UserService service, Button createAccount, Dialog signIn) {
        createAccount.addClickListener(buttonClickEvent -> {

            signIn.close();
            Dialog newAccount = new Dialog();
            newAccount.setHeaderTitle("Create account");
            VerticalLayout verticalLayout = new VerticalLayout();

            newAccount.setModal(false);
            newAccount.open();

            TextField username = new TextField("Username");
            PasswordField password = new PasswordField("Password");
            Button saveButton = new Button("Create");
            saveButton.addClickListener(buttonClickEvent1 -> {
                String encodedPassword = passwordEncoder.encode(password.getValue());
                service.save(new User(username.getValue(), encodedPassword));
                newAccount.getUI().ifPresent(ui -> {
                    VaadinSession.getCurrent().setAttribute("current_user", service
                            .findByName(username.getValue()));
                    ui.navigate(MainView.class);
                });
            });
            verticalLayout.add(username, password);
            saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            Button cancelButton = new Button("Cancel", e -> {
                newAccount.close();
                signIn.open();
            });
            newAccount.add(verticalLayout);
            newAccount.getFooter().add(cancelButton, saveButton);
            add(newAccount);
        });
    }

}
