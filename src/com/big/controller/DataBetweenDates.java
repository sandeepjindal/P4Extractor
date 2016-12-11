import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.addon.JFreeChartWrapper;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by sandeep on 19/8/15.
 */
public class DataBetweenDates extends CustomComponent implements View {
    public static final String NAME = "databetweendates";

    TabSheet tabsheet = new TabSheet();
    TabSheet fromtab = new TabSheet();
    TabSheet totab = new TabSheet();


    DateField fromDate = new DateField();
    DateField toDate =new DateField();
    JFreeChartWrapper jFreePieChartWrapper;
    JFreeChartWrapper jFreeBarChartWrapper;
    PieChaRt pc;
    BarChaRt barChaRt;
    ArrayList<Change> list =new ArrayList<>();
    HorizontalLayout charts;
    Notification note =new Notification("change");
    Label text = new Label();
    Label userNames = new Label();
    Label from = new Label();
    Label to = new Label();



    ChangeDetail changeDetail = new ChangeDetail();

    AllChanges service;


    TextField filter = new TextField();
    TextField userName = new TextField();
    Grid changeList = new Grid();
    Button newContact = new Button("Statistics" ,new Button.ClickListener() {
        @Override
        public void buttonClick(Button.ClickEvent event) {


          //  configureComponents();
            pc = new PieChaRt();
            pc.setDataset(list);
            jFreePieChartWrapper = pc.getJFreeChartWrapper();
            jFreePieChartWrapper.setHeight("80%");
            jFreePieChartWrapper.setWidth("70%");

            barChaRt = new BarChaRt();
            barChaRt.setDataset(list);

            jFreeBarChartWrapper = barChaRt.getJFreeChartWrapper();
            jFreeBarChartWrapper.setHeight("100%");
            jFreeBarChartWrapper.setWidth("100%");

            buildLayout();
            charts.setVisible(true);




        }
    });



    Button dataBetween = new Button("GetData", new Button.ClickListener() {
        @Override
        public void buttonClick(Button.ClickEvent event) {

            //betweenDate.setVisible(false);
            service= AllChanges.getModifiedData(fromDate.getValue(), toDate.getValue(), userName.getValue());
            changeList.setContainerDataSource(new BeanItemContainer<>(Change.class));
            refreshContacts();
            changeList.setSizeFull();
            charts.setVisible(false);
        }
    });


    Button allchanges = new Button("ALL", new Button.ClickListener() {

        @Override
        public void buttonClick(Button.ClickEvent event) {


            fromDate.setValue(new Date(0));
            toDate.setValue(new Date());
            service= AllChanges.getDatabetween(fromDate.getValue(),toDate.getValue());
            changeList.setContainerDataSource(new BeanItemContainer<>(Change.class));
            refreshContacts();
            charts.setVisible(false);

           // getUI().getNavigator().navigateTo(DataBetweenDates.NAME);;

        }
    });
    private int daysBetween(long t1, long t2) {
        return (int) ((t2 - t1) / (1000 * 60 * 60 * 24));
    }
    //Data by user ID;
    // Create a text field
    TextField tf = new TextField("UserName");
    TextField tf1 = new TextField("frequency@weekly");
    Button user_data = new Button("Enter", new Button.ClickListener() {
        @Override
        public void buttonClick(Button.ClickEvent event) {
            int count1;
            //betweenDate.setVisible(false);
            fromDate.setValue(new Date(0));
            toDate.setValue(new Date());
            service= AllChanges.getModifiedData(fromDate.getValue(),toDate.getValue(),tf.getValue());
            changeList.setContainerDataSource(new BeanItemContainer<>(Change.class));
            count1 = list.size();
            java.util.Calendar cal = java.util.Calendar.getInstance();
            java.util.Calendar cal1 = java.util.Calendar.getInstance();

            int diffInDays = (int)( (toDate.getValue().getTime() - fromDate.getValue().getTime())
                    / (1000 * 60 * 60 * 24) );

            tf1.setValue("" + (count1/(diffInDays/7) ));
            changeList.setSizeFull();

        }
    });



    Button last7days = new Button("last7days", new Button.ClickListener() {

        @Override
        public void buttonClick(Button.ClickEvent event) {


            fromDate.setValue(getPreviousWeekDate());
            toDate.setValue(new Date());
            service= AllChanges.getDatabetween(fromDate.getValue(),toDate.getValue());
            changeList.setContainerDataSource(new BeanItemContainer<>(Change.class));
            refreshContacts();
            charts.setVisible(false);
           // getUI().getNavigator().navigateTo(DataBetweenDates.NAME);

        }
    });



    Button logout = new Button("Logout", new Button.ClickListener() {

        @Override
        public void buttonClick(Button.ClickEvent event) {

            // "Logout" the user
            getSession().setAttribute("user", null);

            // Refresh this view, should redirect to login view
            getUI().getNavigator().navigateTo(NAME);
        }
    });


    private void configureComponents(){
       // newContact.addClickListener(e -> jFreePieChartWrapper.detach());

        filter.setInputPrompt("Filter contacts...");
        filter.addTextChangeListener(e -> refreshContacts(e.getText()));
        changeList.setContainerDataSource(new BeanItemContainer<>(Change.class));
        changeList.setDetailsGenerator(new Grid.DetailsGenerator() {
            @Override
            public Component getDetails(Grid.RowReference rowReference) {


                final Change change = (Change) rowReference.getItemId();

                Label label = new Label();
                label.setContentMode(ContentMode.HTML);
                if (change.getrQ().equals("false")) {
                    label.setValue("<p>Change " + change.getChangeId() + " by <a href=\"\">" + change.getUserId() + "</a> on " + change.getDatetime() + "</p><p>&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbsp;" + change.getSubject() + "</p>");
                } else {
                    label.setValue("<p>Change " + change.getChangeId() + " by <a href=\"\">" + change.getUserId() + "</a> on " + change.getDatetime() + "</p><p>&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbsp;" + change.getSubject() + "<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;(auto-submit " + change.getrQiD() + " after successfully running remote remote.all)<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href=\"\">" + change.getLinkToRq() + "</a></p>");

                }
                VerticalLayout layout = new VerticalLayout(label);
                layout.setSpacing(true);
                layout.setMargin(true);
                return layout;


            }
        });



        changeList.addItemClickListener(new ItemClickEvent.ItemClickListener() {
            @Override
            public void itemClick(ItemClickEvent event) {
                if (event.isDoubleClick()) {
                    Object itemId = event.getItemId();
                    changeList.setDetailsVisible(itemId, !changeList.isDetailsVisible(itemId));
                }
            }
        });

        changeList.setColumnOrder("changeId", "datetime", "userId", "test_Type","rQ", "subject");
        try {
            changeList.removeColumn("id");
            changeList.removeColumn("rQiD");
            changeList.removeColumn("afFileList");
            changeList.removeColumn("details");
            changeList.removeColumn("linkToRq");
        }catch (Exception e){}

        changeList.setSelectionMode(Grid.SelectionMode.SINGLE);
       try {
          //  changeList.addSelectionListener(e ->changeDetail.edit((Change)changeList.getSelectedRow()));
        }catch (Exception e){
        }


        VerticalLayout tab = new VerticalLayout();
        tab.addComponent(text);
        tabsheet.addTab(tab).setCaption(text.getValue());
        tab.addStyleName("blue");
        VerticalLayout tab1 = new VerticalLayout();
        tab1.addComponent(new Label("It shows all the changes done through p4 users till date.Use can use the table below to navigate through these changes"));
        tab1.addStyleName("blue");
        tabsheet.addTab(tab1).setCaption("DataWarehouse");
        VerticalLayout tab2 = new VerticalLayout();
        tab2.addComponent(new Label("These the most recent changes done by all the users in last seven days.User can also filter the changes by typing the name of person."));
        tabsheet.addTab(tab2).setCaption("LastSevenDays");
        VerticalLayout tab4 = new VerticalLayout();
        tab4.addComponent(new Label("Enable the logout feature Hope to see you soon"));
        tab4.addStyleName("green");
        tabsheet.addTab(tab4).setCaption("Logout");




        tabsheet.addListener(new TabSheet.SelectedTabChangeListener() {
            public void selectedTabChange(TabSheet.SelectedTabChangeEvent event) {
                if (tabsheet.getSelectedTab().equals(tab)) {
                } else if (tabsheet.getSelectedTab().equals(tab1)) {


                    fromDate.setValue(new Date(0));
                    toDate.setValue(new Date());
                    service= AllChanges.getDatabetween(fromDate.getValue(),toDate.getValue());
                    changeList.setContainerDataSource(new BeanItemContainer<>(Change.class));
                    refreshContacts();
                    charts.setVisible(false);


                } else if (tabsheet.getSelectedTab().equals(tab2)) {
                    fromDate.setValue(getPreviousWeekDate());
                    toDate.setValue(new Date());
                    service= AllChanges.getDatabetween(fromDate.getValue(),toDate.getValue());
                    changeList.setContainerDataSource(new BeanItemContainer<>(Change.class));
                    refreshContacts();
                    charts.setVisible(false);


                } else if (tabsheet.getSelectedTab().equals(tab4)) {
                    // "Logout" the user
                    getSession().setAttribute("user", null);

                    getUI().getNavigator().navigateTo(NAME);
                }
            }
        });



        refreshContacts();


    }

    private void buildLayout() {

        HorizontalLayout logindetail = new HorizontalLayout(tabsheet);
        logindetail.setWidth("100%");



        userName.setInputPrompt("user-name");
        dataBetween.setCaption("Get Result");
        HorizontalLayout betweenDate =new HorizontalLayout(fromDate,toDate,userName,dataBetween);
        HorizontalLayout frequency = new HorizontalLayout(tf, user_data ,tf1);
        betweenDate.setWidth("100%");
        // betweenDate.setWidth("100%");
        betweenDate.setMargin(true);
        betweenDate.setExpandRatio(dataBetween, 1);

        GridLayout changeLists = new GridLayout();
        changeLists.addComponent(changeList);
        changeLists.setColumnExpandRatio(6,14);
        changeLists.setWidth("100%");
        changeLists.setHeight("100%");
        HorizontalLayout actions = new HorizontalLayout(filter, newContact);
        actions.setWidth("100%");
        filter.setWidth("100%");
        actions.setExpandRatio(filter, 1);

        HorizontalLayout changeDetails = new HorizontalLayout( changeDetail);
        changeDetails.setWidth("100%");
        changeDetails.setSizeFull();

        charts = new HorizontalLayout(jFreePieChartWrapper,jFreeBarChartWrapper);
        charts.setWidth("100%");
        charts.setSizeFull();


        charts.setHeight("20%");
        VerticalLayout menuPanel = new VerticalLayout();
        menuPanel.setHeight("100%");
        menuPanel.addComponent(buildLabels("Welcome "));
        menuPanel.setSizeFull();



        VerticalLayout left = new VerticalLayout(menuPanel, logindetail,betweenDate,frequency,actions, changeLists,changeDetails,charts);
        left.setMargin(true);
        left.setSizeFull();
        changeList.setSizeFull();
//        left.setExpandRatio(changeList, 1);





        HorizontalLayout mainLayout = new HorizontalLayout(left);
        mainLayout.setSizeFull();
      //  mainLayout.setExpandRatio(menuPanel,1);
      //  mainLayout.setExpandRatio(left,9);

        mainLayout.setStyleName("white");
        mainLayout.setMargin(true);
        setCompositionRoot(mainLayout);

    }
    private Component buildLabels(String ss) {
        HorizontalLayout header = new HorizontalLayout();
        header.addStyleName("viewheader");
        header.setSpacing(true);;

        Label titleLabel = new Label(ss);
        titleLabel.setSizeUndefined();
        titleLabel.addStyleName(ValoTheme.LABEL_H2);
        titleLabel.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        header.addComponent(titleLabel);
        header.setMargin(true);
        return header;
    }
    void refreshContacts() {
        refreshContacts(filter.getValue());
    }

    private void refreshContacts(String stringFilter) {

        list = (ArrayList) service.findAll(stringFilter);
        changeList.setContainerDataSource(new BeanItemContainer<>(
                Change.class, list));

        changeDetail.setVisible(false);
    }
    private Date getPreviousWeekDate(){
        return new Date(System.currentTimeMillis()-7*24*60*60*1000);
    }


    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

        String username = String.valueOf(getSession().getAttribute("user"));
        text.setValue("Hello " + username.split("@")[0]);
        userNames.setValue("User-Name");
        newContact.focus();

        try{

            LoginView pp = (LoginView) viewChangeEvent.getOldView();
            fromDate.setValue(getPreviousWeekDate());
            toDate.setValue(new Date());

        }catch (Exception e){

        }

        service = AllChanges.getDatabetween(fromDate.getValue(),toDate.getValue());


        configureComponents();
        pc=new PieChaRt();
        pc.setDataset(list);
        jFreePieChartWrapper = pc.getJFreeChartWrapper();
        jFreePieChartWrapper.setStyleName("white");
        barChaRt= new BarChaRt();
        barChaRt.setDataset(list);
        jFreeBarChartWrapper=barChaRt.getJFreeChartWrapper();
        buildLayout();

        charts.setVisible(false);



    }
}
