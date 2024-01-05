package org.example.ess_6;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import jakarta.annotation.security.PermitAll;
import org.springframework.context.annotation.Scope;
@SpringComponent
@Scope("prototype")
@PermitAll
@Route(value = "", layout = MainLayout.class)
public class MainView extends VerticalLayout {
    public MainView(){
        add(new H1("hello"));
    }
}
