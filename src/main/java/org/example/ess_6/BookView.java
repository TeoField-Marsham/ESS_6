package org.example.ess_6;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import jakarta.annotation.security.PermitAll;
import org.example.ess_6.model.Book;
import org.example.ess_6.service.BookService;
import org.springframework.context.annotation.Scope;
@SpringComponent
@Scope("prototype")
@PermitAll
@Route(value = "", layout = MainLayout.class)

public class BookView extends VerticalLayout{
    Grid<Book> grid = new Grid<>(Book.class);
    TextField filterText = new TextField();
    BookForm form;
    BookService service;
}
