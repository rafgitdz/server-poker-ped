package poker.admin.client.widgets;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;

public class HelpWidget extends Composite {

	private static final String HELP_POKER_ADMINISTRATION = "Help Poker Administration";
	private static final String GWT_BUTTON_LOGIN = "gwt-Button-Login";
	VerticalPanel textHelping;

	public HelpWidget() {

		// Create the dialog box
		final DialogBox dialogBox = createDialogBox();
		dialogBox.setGlassEnabled(true);
		dialogBox.setAnimationEnabled(true);

		// Create a button to show the dialog Box
		Button helpButton = new Button("Help", new ClickHandler() {
			public void onClick(ClickEvent sender) {
				dialogBox.center();
				dialogBox.show();
			}
		});

		helpButton.setStyleName(GWT_BUTTON_LOGIN);
		initWidget(helpButton);
	}

	/**
	 * Create the help dialog box
	 * 
	 * @return the new dialog box
	 */
	private DialogBox createDialogBox() {
		// Create a dialog box and set the caption text
		final DialogBox dialogBox = new DialogBox();
		dialogBox.setText(HELP_POKER_ADMINISTRATION);

		// Create a table to layout the content
		VerticalPanel dialogContents = new VerticalPanel();
		dialogContents.setSpacing(4);
		dialogBox.setWidget(dialogContents);

		// Add some text to the top of the dialog

		textHelping = new VerticalPanel();
		textHelping.setBorderWidth(1);

		addText("Game name : The name of the game");
		addText("Pot type : the type of the money to play (1:CASH or 2:TOKEN)");
		addText("BuyIn : The amount a player spends to get into a game or tournament");
		addText("Factor Update Blinds : The number to multiply the blind when they are updated");
		addText("Small blind : The value of the small blind (note : that the big blind is the double)");
		addText("Player tokens : the value of the initial tokens for all players");
		addText("Player number : The number of players in the game");
		addText("Speak time : The timeout for a player to do an poker action");
		addText("Update blind timeout : The timeout to update the blinds ");
		addText("Number of winners : The number of the final winners of the game");

		dialogContents.add(textHelping);
		dialogContents.setCellHorizontalAlignment(textHelping,
				HasHorizontalAlignment.ALIGN_CENTER);

		// Add a close button at the bottom of the dialog
		Button closeButton = new Button("Close", new ClickHandler() {
			public void onClick(ClickEvent event) {
				dialogBox.hide();
			}
		});
		dialogContents.add(closeButton);
		if (LocaleInfo.getCurrentLocale().isRTL()) {
			dialogContents.setCellHorizontalAlignment(closeButton,
					HasHorizontalAlignment.ALIGN_LEFT);

		} else {
			dialogContents.setCellHorizontalAlignment(closeButton,
					HasHorizontalAlignment.ALIGN_RIGHT);
		}

		// Return the dialog box
		return dialogBox;
	}

	private void addText(String html) {

		HTML text = new HTML(html);
		text.setStyleName("HelpDialogBox");
		textHelping.add(text);
	}
}