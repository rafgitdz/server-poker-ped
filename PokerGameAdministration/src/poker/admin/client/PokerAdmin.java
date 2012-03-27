package poker.admin.client;

import poker.admin.client.widgets.AdminWidget;
import poker.admin.client.widgets.AuthentificationWidget;
import poker.admin.client.widgets.HelpWidget;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class PokerAdmin implements EntryPoint {

	public void onModuleLoad() {

		RootPanel rootPanel = RootPanel.get();

		Image img = new Image("/PokerLogo.jpg");
		rootPanel.add(img);
		img.setSize("200px", "100px");

		AdminWidget admin = new AdminWidget();

		HelpWidget helpButton = new HelpWidget();
		helpButton.setStyleName("HelpButton");
		rootPanel.add(helpButton);
		helpButton.setVisible(false);

		admin.setStyleName("AdminWidget");
		rootPanel.add(admin);

		AuthentificationWidget authWidget = new AuthentificationWidget(admin,
				helpButton);
		authWidget.setStyleName("AuthWidget");
		rootPanel.add(authWidget);

		admin.setVisible(false);
	}
}
