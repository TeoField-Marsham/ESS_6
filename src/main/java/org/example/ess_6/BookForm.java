package org.example.ess_6;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;
import org.example.ess_6.model.Author;
import org.example.ess_6.model.Book;
import org.example.ess_6.model.Publisher;
import org.example.ess_6.service.AuthorService;
import org.example.ess_6.service.PublisherService;

import java.util.List;

public class BookForm extends FormLayout {
    TextField title = new TextField("Title");
    ComboBox<Author> author = new ComboBox<>("Author");
    ComboBox<Publisher> publisher = new ComboBox<>("Publisher");
    AuthorService authorService;
    PublisherService publisherService;

    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");
    // Other fields omitted
    Binder<Book> binder = new BeanValidationBinder<>(Book.class);

    public BookForm(List<Author> authors, List<Publisher> publishers, AuthorService authorService, PublisherService publisherService) {
        addClassName("book-form");
        binder.bindInstanceFields(this);

        publisher.setItems(publishers);
        publisher.setItemLabelGenerator(Publisher::getName);
        author.setItems(authors);
        author.setItemLabelGenerator(Author::getName);

        this.authorService = authorService;
        this.publisherService = publisherService;
        refreshAuthors();
        refreshPublishers();

        add(title,
                publisher,
                author,
                createButtonsLayout());
    }

    public void refreshAuthors(){
        author.setItems(authorService.findAllAuthors(""));
    }

    public void refreshPublishers(){
        publisher.setItems(publisherService.findAllPublishers(""));
    }

    private Component createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, binder.getBean())));
        close.addClickListener(event -> fireEvent(new CloseEvent(this)));

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
        return new HorizontalLayout(save, delete, close);
    }

    private void validateAndSave() {
        if(binder.isValid()) {
            fireEvent(new SaveEvent(this, binder.getBean()));
        }
    }


    public void setBook(Book book) {
        binder.setBean(book);
    }

    // Events
    public static abstract class BookFormEvent extends ComponentEvent<BookForm> {
        private Book book;

        protected BookFormEvent(BookForm source, Book book) {
            super(source, false);
            this.book = book;
        }

        public Book getBook() {
            return book;
        }
    }

    public static class SaveEvent extends BookFormEvent {
        SaveEvent(BookForm source, Book book) {
            super(source, book);
        }
    }

    public static class DeleteEvent extends BookFormEvent {
        DeleteEvent(BookForm source, Book book) {
            super(source, book);
        }

    }

    public static class CloseEvent extends BookFormEvent {
        CloseEvent(BookForm source) {
            super(source, null);
        }
    }

    public Registration addDeleteListener(ComponentEventListener<DeleteEvent> listener) {
        return addListener(DeleteEvent.class, listener);
    }

    public Registration addSaveListener(ComponentEventListener<SaveEvent> listener) {
        return addListener(SaveEvent.class, listener);
    }
    public Registration addCloseListener(ComponentEventListener<CloseEvent> listener) {
        return addListener(CloseEvent.class, listener);
    }


}
