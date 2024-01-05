package org.example.ess_6;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import jakarta.annotation.security.PermitAll;
import org.example.ess_6.model.Publisher;
import org.example.ess_6.service.PublisherService;
import org.springframework.context.annotation.Scope;
@SpringComponent
@Scope("prototype")
@PermitAll
@Route(value = "publisher", layout = MainLayout.class)

public class PublisherView extends VerticalLayout{
    Grid<Publisher> grid = new Grid<>(Publisher.class);
    TextField filterText = new TextField();
    PublisherForm form;
    PublisherService service;

    public PublisherView(PublisherService service) {
        this.service = service;
        addClassName("list-view");
        setSizeFull();
        configureGrid();
        configureForm();

        add(getToolbar(), getContent());
        updateList();
        closeEditor();
    }
    private HorizontalLayout getContent() {
        HorizontalLayout content = new HorizontalLayout(grid, form);
        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, form);
        content.addClassNames("content");
        content.setSizeFull();
        return content;
    }

    private void configureForm() {
        form = new PublisherForm();
        form.setWidth("25em");
        form.addSaveListener(this::savePublisher); // <1>
        form.addDeleteListener(this::deletePublisher); // <2>
        form.addCloseListener(e -> closeEditor()); // <3>
    }

    private void savePublisher(PublisherForm.SaveEvent event) {
        service.savePublisher(event.getPublisher());
        updateList();
        closeEditor();
    }

    private void deletePublisher(PublisherForm.DeleteEvent event) {
        service.deletePublisher(event.getPublisher().getId());
        updateList();
        closeEditor();
    }

    private void configureGrid() {
        grid.addClassNames("Publisher-grid");
        grid.setSizeFull();
        grid.setColumns("name");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.asSingleSelect().addValueChangeListener(event ->
                editPublisher(event.getValue()));
    }

    private Component getToolbar() {
        filterText.setPlaceholder("Filter by name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addPublisherButton = new Button("Add Publisher");
        addPublisherButton.addClickListener(click -> addBook());

        var toolbar = new HorizontalLayout(filterText, addPublisherButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    public void editPublisher(Publisher publisher) {
        if (publisher == null) {
            closeEditor();
        } else {
            form.setPublisher(publisher);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private void closeEditor() {
        form.setPublisher(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void addBook() {
        grid.asSingleSelect().clear();
        editPublisher(new Publisher());
    }


    private void updateList() {
        grid.setItems(service.findAllPublishers(filterText.getValue()));
    }
}
