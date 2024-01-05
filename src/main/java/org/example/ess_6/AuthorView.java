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
import org.example.ess_6.model.Author;
import org.example.ess_6.service.AuthorService;
import org.springframework.context.annotation.Scope;
@SpringComponent
@Scope("prototype")
@PermitAll
@Route(value = "author", layout = MainLayout.class)

public class AuthorView extends VerticalLayout{
    Grid<Author> grid = new Grid<>(Author.class);
    TextField filterText = new TextField();
    AuthorForm form;
    AuthorService service;

    public AuthorView(AuthorService service) {
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
        form = new AuthorForm();
        form.setWidth("25em");
        form.addSaveListener(this::saveAuthor);
        form.addDeleteListener(this::deleteAuthor);
        form.addCloseListener(e -> closeEditor());
    }

    private void saveAuthor(AuthorForm.SaveEvent event) {
        service.saveAuthor(event.getAuthor());
        updateList();
        closeEditor();
    }

    private void deleteAuthor(AuthorForm.DeleteEvent event) {
        service.deleteAuthor(event.getAuthor().getId());
        updateList();
        closeEditor();
    }

    private void configureGrid() {
        grid.addClassNames("author-grid");
        grid.setSizeFull();
        grid.setColumns("name");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.asSingleSelect().addValueChangeListener(event ->
                editAuthor(event.getValue()));
    }

    private Component getToolbar() {
        filterText.setPlaceholder("Filter by name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addAuthorButton = new Button("Add author");
        addAuthorButton.addClickListener(click -> addBook());

        var toolbar = new HorizontalLayout(filterText, addAuthorButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    public void editAuthor(Author author) {
        if (author == null) {
            closeEditor();
        } else {
            form.setAuthor(author);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private void closeEditor() {
        form.setAuthor(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void addBook() {
        grid.asSingleSelect().clear();
        editAuthor(new Author());
    }


    private void updateList() {
        grid.setItems(service.findAllAuthors(filterText.getValue()));
    }
}
