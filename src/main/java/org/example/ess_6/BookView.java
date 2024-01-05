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
import org.example.ess_6.model.Book;
import org.example.ess_6.service.AuthorService;
import org.example.ess_6.service.BookService;
import org.example.ess_6.service.PublisherService;
import org.springframework.context.annotation.Scope;
@SpringComponent
@Scope("prototype")
@PermitAll
@Route(value = "book", layout = MainLayout.class)

public class BookView extends VerticalLayout{
    Grid<Book> grid = new Grid<>(Book.class);
    TextField filterText = new TextField();
    BookForm form;
    BookService service;
    AuthorService authorService;
    PublisherService publisherService;

    public BookView(BookService service) {
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
        form = new BookForm(authorService.findAllAuthors(""), publisherService.findAllPublishers(""));
        form.setWidth("25em");
        form.addSaveListener(this::saveBook); // <1>
        form.addDeleteListener(this::deleteBook); // <2>
        form.addCloseListener(e -> closeEditor()); // <3>
    }

    private void saveBook(BookForm.SaveEvent event) {
        service.saveBook(event.getBook());
        updateList();
        closeEditor();
    }

    private void deleteBook(BookForm.DeleteEvent event) {
        service.deleteBook(event.getBook().getId());
        updateList();
        closeEditor();
    }

    private void configureGrid() {
        grid.addClassNames("book-grid");
        grid.setSizeFull();
        grid.setColumns("title");
        grid.addColumn(book -> book.getAuthor().getName()).setHeader("Author");
        grid.addColumn(book -> book.getPublisher().getName()).setHeader("Publisher");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.asSingleSelect().addValueChangeListener(event ->
                editBook(event.getValue()));
    }

    private Component getToolbar() {
        filterText.setPlaceholder("Filter by name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addBookButton = new Button("Add book");
        addBookButton.addClickListener(click -> addBook());

        var toolbar = new HorizontalLayout(filterText, addBookButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    public void editBook(Book book) {
        if (book == null) {
            closeEditor();
        } else {
            form.setBook(book);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private void closeEditor() {
        form.setBook(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void addBook() {
        grid.asSingleSelect().clear();
        editBook(new Book());
    }


    private void updateList() {
        grid.setItems(service.findAllBooks(filterText.getValue()));
    }
}
