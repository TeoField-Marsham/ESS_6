package org.example.ess_6;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLink;

public class MainLayout extends AppLayout{
    public MainLayout(){

        createDrawer();
    }
    private void createDrawer() {
        addToDrawer(new VerticalLayout(
                new RouterLink("Books", BookView.class),
                new RouterLink("Authors", AuthorView.class),
                new RouterLink("Publishers", PublisherView.class)
        ));
    }
}
