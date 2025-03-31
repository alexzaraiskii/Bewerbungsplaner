package com.bewerbungsplanner.view;

import com.bewerbungsplanner.model.JobApplication;
import com.bewerbungsplanner.model.User;
import com.bewerbungsplanner.service.JobApplicationService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

import com.vaadin.flow.theme.lumo.Lumo;
import com.vaadin.flow.theme.lumo.LumoUtility;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Log4j2
@Route("/main")
public class MainView extends AppLayout {

    private JobApplicationService jobApplicationService;
    private Grid<JobApplication> grid = new Grid<>(JobApplication.class, false);
    private User currentUser = (User) VaadinSession.getCurrent().getAttribute("current_user");


    @Autowired
    public MainView(JobApplicationService jobApplicationService) {

        this.jobApplicationService = jobApplicationService;

        if (currentUser == null) {
            currentUser = new User();
            log.debug("Your session has expired. Please log in again");
            UI.getCurrent().getPage().setLocation("/");
        }

        navbarItems();

        updateView();
        createView();

        GridContextMenu<JobApplication> contextMenu = grid.addContextMenu();

        update(jobApplicationService, contextMenu);

        delete(jobApplicationService, contextMenu);

    }

    private void navbarItems() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();

        H1 title = new H1("Job Application Planner");
        title.getStyle().set("font-size", "var(--lumo-font-size-l)")
                .set("margin", "var(--lumo-space-m)");


        Span user = new Span("Welcome to your planner");
        Span username = new Span(currentUser.getUsername());


        Icon logOut = new Icon(VaadinIcon.SIGN_OUT);

        logOut.addClickListener(iconClickEvent -> {

            ConfirmDialog dialog = new ConfirmDialog();
            dialog.open();
            dialog.setHeader("Log out of your current session");
            dialog.setText(
                    "Do you really want to log out?");

            dialog.setCancelable(true);
            dialog.addCancelListener(event -> dialog.close());

            dialog.setConfirmText("Log out");
            dialog.setConfirmButtonTheme("error primary");
            dialog.addConfirmListener(event -> UI.getCurrent().getPage().setLocation("/"));

        });

        horizontalLayout.add(user, username, logOut);
        addToNavbar(title, horizontalLayout);
    }

    private void delete(JobApplicationService jobApplicationService, GridContextMenu<JobApplication> contextMenu) {
        contextMenu.addItem("Delete", d -> {
            d.getItem().ifPresent(jobApplication ->
                    jobApplicationService.delete(jobApplication.getId()));
            updateView();
        });
    }

    private void update(JobApplicationService jobApplicationService, GridContextMenu<JobApplication> contextMenu) {
        contextMenu.addItem("Edit", u -> {

            u.getItem().ifPresent(updateJobApplication -> {

                Dialog dialog = new Dialog();
                dialog.open();
                dialog.setHeaderTitle("Update current job application");
                HorizontalLayout dialogLayout = new HorizontalLayout();
                dialog.add(dialogLayout);

                TextField company = new TextField();
                company.setValue(updateJobApplication.getCompany());

                TextField position = new TextField();
                position.setValue(updateJobApplication.getPosition());

                ComboBox<String> status = new ComboBox<>();
                status.setItems(getJobApplicationList());
                status.setValue(updateJobApplication.getStatus());

                DatePicker dateOfApplication = new DatePicker();
                dateOfApplication.setValue(LocalDate.parse(updateJobApplication.getDateOfApplication()));

                TextArea jobDescription = new TextArea();
                jobDescription.setValue(updateJobApplication.getJobDescription());

                TextArea comments = new TextArea();
                comments.setValue(updateJobApplication.getComments());

                TextArea link = new TextArea();
                link.setValue(updateJobApplication.getJobApplicationLink());

                Button updateButton = new Button("Update");

                dialogLayout.add(company, position, status, dateOfApplication, jobDescription, comments, link, updateButton);

                updateButton.addClickListener(buttonClickEvent -> {

                    updateJobApplication.setCompany(company.getValue());
                    updateJobApplication.setPosition(position.getValue());
                    updateJobApplication.setStatus(status.getValue());
                    updateJobApplication.setDateOfApplication(String.valueOf(dateOfApplication.getValue()));
                    updateJobApplication.setJobDescription(jobDescription.getValue());
                    updateJobApplication.setComments(comments.getValue());
                    updateJobApplication.setJobApplicationLink(link.getValue());

                    jobApplicationService.update(updateJobApplication);

                    updateView();
                    dialog.close();
                });
            });
        });
    }

    private void createView() {

        tableView();

        GridListDataView<JobApplication> dataView = grid.setItems(jobApplicationService.findJobApplicationsByUserId(currentUser.getId()));

        HorizontalLayout horizontalLayout = new HorizontalLayout();

        TextField searchField = new TextField();
        searchField.setWidth("100%");
        searchField.setPlaceholder("Search");
        searchField.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        searchField.setValueChangeMode(ValueChangeMode.EAGER);

        searchField.addValueChangeListener(e -> dataView.refreshAll());

        dataView(dataView, searchField);

        Button newApplication = new Button("New job application");

        List<String> jobApplicationList = getJobApplicationList();

        newApplication(newApplication, jobApplicationList);

        horizontalLayout.add(searchField, newApplication);

        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.add(horizontalLayout, grid);
        setContent(verticalLayout);

    }

    private void tableView() {
        grid.addColumn(JobApplication::getCompany).setHeader("Company").setSortable(true);
        grid.addColumn(JobApplication::getPosition).setHeader("Position");
        grid.addColumn(JobApplication::getStatus).setHeader("Status").setSortable(true);
        grid.addColumn(JobApplication::getDateOfApplication).setHeader("Date of application").setSortable(true);
        grid.addColumn(JobApplication::getJobDescription).setHeader("Job description");
        grid.addColumn(JobApplication::getComments).setHeader("Comments");
        grid.addColumn(JobApplication::getJobApplicationLink).setHeader("Job application link");

        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.setAllRowsVisible(true);
//        grid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);

    }

    private void dataView(GridListDataView<JobApplication> dataView, TextField searchField) {
        dataView.addFilter(e -> {
            String searchTerm = searchField.getValue().trim();
            log.info(searchTerm);
            System.out.println(searchTerm);

            if (searchTerm.isEmpty())
                return true;

            boolean matchesCompany = matchesTerm(e.getCompany(),
                    searchTerm);
            boolean matchesPosition = matchesTerm(e.getPosition(), searchTerm);
            boolean matchesStatus = matchesTerm(e.getStatus(),
                    searchTerm);

            return matchesCompany || matchesPosition || matchesStatus;

        });
    }

    private void newApplication(Button newApplication, List<String> jobApplicationList) {
        newApplication.addClickListener(buttonClickEvent -> {

            Dialog addNewApplication = new Dialog();
            HorizontalLayout horizontalLayout1 = new HorizontalLayout();

            TextField company = new TextField(null, "Company");

            TextField position = new TextField(null, "Position");

            ComboBox<String> status = new ComboBox<>();
            status.setPlaceholder("Status");
            status.setItems(jobApplicationList);

            DatePicker dateOfApplication = new DatePicker();
            dateOfApplication.setPlaceholder("Date of application");

            TextArea jobDescription = new TextArea(null, "Job description");

            TextArea comments = new TextArea(null, "Comments");

            TextArea link = new TextArea(null, "Job application link");

            Button saveButton = new Button("Save");

            horizontalLayout1.add(company, position, status, dateOfApplication, jobDescription, comments, link, saveButton);
            addNewApplication.add(horizontalLayout1);
            addNewApplication.open();

            saveButton.addClickListener(buttonClickEvent1 -> {

                jobApplicationService.addJobApplication(
                        currentUser.getId(),
                        new JobApplication(company.getValue(),
                                position.getValue(),
                                status.getValue(),
                                String.valueOf(dateOfApplication.getValue()),
                                jobDescription.getValue(),
                                comments.getValue(),
                                link.getValue()));
                updateView();
                addNewApplication.close();
            });
        });
    }

    private static List<String> getJobApplicationList() {
        List<String> jobApplicationList = new ArrayList<>();
        jobApplicationList.add(0, "Ready to apply");
        jobApplicationList.add(1, "Applied");
        jobApplicationList.add(2, "Interview scheduled");
        jobApplicationList.add(3, "Archived");
        jobApplicationList.add(4, "Declined");
        return jobApplicationList;
    }

    private boolean matchesTerm(String value, String searchTerm) {
        return searchTerm == null || searchTerm.isEmpty()
                || value.toLowerCase().contains(searchTerm.toLowerCase());
    }

    private void updateView() {
        grid.setItems(jobApplicationService.findJobApplicationsByUserId(currentUser.getId()));
    }

}