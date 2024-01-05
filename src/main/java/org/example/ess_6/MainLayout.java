package org.example.ess_6;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.theme.lumo.LumoUtility;

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
