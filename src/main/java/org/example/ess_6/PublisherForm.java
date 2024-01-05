package org.example.ess_6;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;
import org.example.ess_6.model.Publisher;

public class PublisherForm extends FormLayout {
    TextField name = new TextField("Name");

    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");
    Binder<Publisher> binder = new BeanValidationBinder<>(Publisher.class);

    public PublisherForm() {
        addClassName("publisher-form");
        binder.bindInstanceFields(this);

        add(name, createButtonsLayout());
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


    public void setPublisher(Publisher publisher) {
        binder.setBean(publisher);
    }

    // Events
    public static abstract class PublisherFormEvent extends ComponentEvent<PublisherForm> {
        private Publisher publisher;

        protected PublisherFormEvent(PublisherForm source, Publisher publisher) {
            super(source, false);
            this.publisher = publisher;
        }

        public Publisher getPublisher() {
            return publisher;
        }
    }

    public static class SaveEvent extends PublisherFormEvent {
        SaveEvent(PublisherForm source, Publisher publisher) {
            super(source, publisher);
        }
    }

    public static class DeleteEvent extends PublisherFormEvent {
        DeleteEvent(PublisherForm source, Publisher publisher) {
            super(source, publisher);
        }

    }

    public static class CloseEvent extends PublisherFormEvent {
        CloseEvent(PublisherForm source) {
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
