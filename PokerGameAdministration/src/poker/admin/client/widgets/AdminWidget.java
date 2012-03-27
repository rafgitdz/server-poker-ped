package poker.admin.client.widgets;

import java.util.ArrayList;
import java.util.List;

import poker.admin.client.RequestHandler;
import poker.admin.client.exception.ParametersException;
import poker.admin.client.model.Parameter;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.HasKeyboardPagingPolicy.KeyboardPagingPolicy;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent.Handler;
import com.google.gwt.view.client.SingleSelectionModel;

public class AdminWidget extends Composite {

	private static final String GWT_TEXT_AREA_LOGIN = "gwt-TextArea-Login";
	private static final String GWT_CELLLIST_ADMIN = "gwt-Celllist-admin";
	private static final String GWT_LABEL_PGA = "gwt-LabelPGA";
	private static final String POKER_GAME_ADMINISTRATION = "Poker Game Administration";
	private static final String STYLE_GWT_BUTTON_LOGIN = "gwt-Button-Login";
	private static final String DELETE_PARAMETER = "Are you sure to delete this parameter?";

	public TextBox gameNameTextBox;
	public TextBox playerNumberTextBox;
	public TextBox playerTokenTextBox;
	public TextBox buyInTextBox;
	public TextBox speakTimeTextBox;
	public TextBox smallBlindTextBox;
	public TextBox factorUpdateBlindTextBox;
	public TextBox updateBlindTimeTextBox;
	public TextBox potTypeTextBox;
	public TextBox numberOfWinnersTextBox;

	public List<String> listName;
	public CellList<String> cellList;

	public Button saveGameTypeButton;
	public Button newGameTypeButton;
	public Button modifyGameTypeButton;
	public Button deleteGameTypeButton;

	private static final String BLANK = "";

	private String gameName;
	private int buyIn;
	private int playerNumber;
	private int playerTokens;
	private int speakTime;
	private int smallBlind;
	private int factorUpdateBlind;
	private int updateTimeBlind;
	private int potType;
	private int numberOfWinners;

	public SingleSelectionModel<String> selectionModel;

	public List<Parameter> listParameters;

	private FlowPanel flowPanel;

	private String consumerKeyAdmin;

	public AdminWidget() {

		listParameters = new ArrayList<Parameter>();
		listName = new ArrayList<String>();

		VerticalPanel verticalPanel = new VerticalPanel();
		verticalPanel.setBorderWidth(2);
		initWidget(verticalPanel);

		Label lblPokerGameAdministration = new Label(POKER_GAME_ADMINISTRATION);
		lblPokerGameAdministration.setStyleName(GWT_LABEL_PGA);

		verticalPanel.add(lblPokerGameAdministration);
		verticalPanel.setCellHorizontalAlignment(lblPokerGameAdministration,
				HasHorizontalAlignment.ALIGN_CENTER);

		HorizontalPanel horizontalPanel = new HorizontalPanel();
		verticalPanel.add(horizontalPanel);
		verticalPanel.setCellHorizontalAlignment(horizontalPanel,
				HasHorizontalAlignment.ALIGN_CENTER);

		Grid grid = new Grid(1, 1);
		grid.setBorderWidth(1);

		horizontalPanel.add(grid);

		selectionModel = new SingleSelectionModel<String>();
		TextCell textCell = new TextCell();
		cellList = new CellList<String>(textCell);
		cellList.setFocus(true);
		cellList.setStyleName(GWT_CELLLIST_ADMIN);

		grid.getCellFormatter().setHeight(0, 0, "330");
		grid.getCellFormatter().setWidth(0, 0, "345");
		grid.getCellFormatter().setVerticalAlignment(0, 0,
				HasVerticalAlignment.ALIGN_TOP);
		grid.getCellFormatter().setHorizontalAlignment(0, 0,
				HasHorizontalAlignment.ALIGN_LEFT);

		cellList.setFocus(true);
		grid.setWidget(0, 0, cellList);
		grid.getCellFormatter().setStyleName(0, 0, GWT_CELLLIST_ADMIN);

		cellList.setKeyboardPagingPolicy(KeyboardPagingPolicy.INCREASE_RANGE);
		cellList.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.BOUND_TO_SELECTION);
		cellList.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);

		cellList.setRowData(0, listName);
		cellList.setRowCount(listName.size(), true);
		cellList.setSelectionModel(selectionModel);

		FlexTable flexTable = new FlexTable();
		flexTable.setBorderWidth(1);
		horizontalPanel.add(flexTable);
		horizontalPanel.setCellHorizontalAlignment(flexTable,
				HasHorizontalAlignment.ALIGN_CENTER);

		Label lblGameName = new Label("Game name:");
		flexTable.setWidget(0, 0, lblGameName);
		flexTable.getCellFormatter().setStyleName(0, 0, "gwt-Label-loginBis");

		gameNameTextBox = new TextBox();
		gameNameTextBox.setMaxLength(25);
		gameNameTextBox.setStyleName(GWT_TEXT_AREA_LOGIN);
		flexTable.setWidget(0, 1, gameNameTextBox);

		Label lblNewLabel = new Label("BuyIn:");
		flexTable.setWidget(1, 0, lblNewLabel);
		flexTable.getCellFormatter().setStyleName(1, 0, "gwt-Label-loginBis");

		buyInTextBox = new TextBox();
		buyInTextBox.setStyleName(GWT_TEXT_AREA_LOGIN);
		flexTable.setWidget(1, 1, buyInTextBox);

		Label lblNewLabel_4 = new Label("Players Number:");
		flexTable.setWidget(2, 0, lblNewLabel_4);
		flexTable.getCellFormatter().setStyleName(2, 0, "gwt-Label-loginBis");

		playerNumberTextBox = new TextBox();
		playerNumberTextBox.setStyleName(GWT_TEXT_AREA_LOGIN);
		flexTable.setWidget(2, 1, playerNumberTextBox);

		Label playerTokensLabel = new Label("Player tokens:");
		flexTable.setWidget(3, 0, playerTokensLabel);
		flexTable.getCellFormatter().setStyleName(3, 0, "gwt-Label-loginBis");

		playerTokenTextBox = new TextBox();
		playerTokenTextBox.setStyleName(GWT_TEXT_AREA_LOGIN);
		flexTable.setWidget(3, 1, playerTokenTextBox);

		Label lblNewLabel_2 = new Label("Speak time:");
		flexTable.setWidget(4, 0, lblNewLabel_2);
		flexTable.getCellFormatter().setStyleName(4, 0, "gwt-Label-loginBis");

		speakTimeTextBox = new TextBox();
		speakTimeTextBox.setStyleName(GWT_TEXT_AREA_LOGIN);
		flexTable.setWidget(4, 1, speakTimeTextBox);

		Label lblSmallBlind = new Label("Small Blind:");
		flexTable.setWidget(5, 0, lblSmallBlind);
		flexTable.getCellFormatter().setStyleName(5, 0, "gwt-Label-loginBis");

		smallBlindTextBox = new TextBox();
		smallBlindTextBox.setStyleName(GWT_TEXT_AREA_LOGIN);
		flexTable.setWidget(5, 1, smallBlindTextBox);

		Label lblMultiFact = new Label("Blind update factor:");
		flexTable.setWidget(6, 0, lblMultiFact);
		flexTable.getCellFormatter().setStyleName(6, 0, "gwt-Label-loginBis");

		factorUpdateBlindTextBox = new TextBox();
		factorUpdateBlindTextBox.setStyleName(GWT_TEXT_AREA_LOGIN);
		flexTable.setWidget(6, 1, factorUpdateBlindTextBox);

		Label lblNewLabel_3 = new Label("Update blinds time:");
		flexTable.setWidget(7, 0, lblNewLabel_3);
		flexTable.getCellFormatter().setStyleName(7, 0, "gwt-Label-loginBis");

		updateBlindTimeTextBox = new TextBox();
		updateBlindTimeTextBox.setStyleName(GWT_TEXT_AREA_LOGIN);
		flexTable.setWidget(7, 1, updateBlindTimeTextBox);

		Label potTypeLabel = new Label("Pot type:");
		flexTable.setWidget(8, 0, potTypeLabel);
		flexTable.getCellFormatter().setStyleName(8, 0, "gwt-Label-loginBis");

		potTypeTextBox = new TextBox();
		potTypeTextBox.setStyleName(GWT_TEXT_AREA_LOGIN);
		flexTable.setWidget(8, 1, potTypeTextBox);

		Label numberOfWinnersLabel = new Label("Number of winners:");
		flexTable.setWidget(9, 0, numberOfWinnersLabel);
		flexTable.getCellFormatter().setStyleName(9, 0, "gwt-Label-loginBis");

		numberOfWinnersTextBox = new TextBox();
		numberOfWinnersTextBox.setStyleName(GWT_TEXT_AREA_LOGIN);
		flexTable.setWidget(9, 1, numberOfWinnersTextBox);

		flexTable.getCellFormatter().setHorizontalAlignment(0, 0,
				HasHorizontalAlignment.ALIGN_RIGHT);
		flexTable.getCellFormatter().setHorizontalAlignment(1, 0,
				HasHorizontalAlignment.ALIGN_RIGHT);
		flexTable.getCellFormatter().setHorizontalAlignment(2, 0,
				HasHorizontalAlignment.ALIGN_RIGHT);
		flexTable.getCellFormatter().setHorizontalAlignment(3, 0,
				HasHorizontalAlignment.ALIGN_RIGHT);
		flexTable.getCellFormatter().setHorizontalAlignment(4, 0,
				HasHorizontalAlignment.ALIGN_RIGHT);
		flexTable.getCellFormatter().setHorizontalAlignment(5, 0,
				HasHorizontalAlignment.ALIGN_RIGHT);
		flexTable.getCellFormatter().setHorizontalAlignment(6, 0,
				HasHorizontalAlignment.ALIGN_RIGHT);
		flexTable.getCellFormatter().setHorizontalAlignment(7, 0,
				HasHorizontalAlignment.ALIGN_RIGHT);
		flexTable.getCellFormatter().setHorizontalAlignment(8, 0,
				HasHorizontalAlignment.ALIGN_RIGHT);
		flexTable.getCellFormatter().setHorizontalAlignment(9, 0,
				HasHorizontalAlignment.ALIGN_RIGHT);

		flexTable.getCellFormatter().setWidth(0, 0, "250px");

		flowPanel = new FlowPanel();
		verticalPanel.add(flowPanel);
		verticalPanel.setCellHorizontalAlignment(flowPanel,
				HasHorizontalAlignment.ALIGN_CENTER);

		/*
		 * Third panel => Button handlers
		 */

		saveGameTypeButton = new Button("Save");
		newGameTypeButton = new Button("New");
		modifyGameTypeButton = new Button("Modify");
		deleteGameTypeButton = new Button("Delete");

		configureButton(saveGameTypeButton, getSaveButtonHandler());
		configureButton(newGameTypeButton, getNewButtonHandler());
		configureButton(modifyGameTypeButton, getModifyButtonHandler());
		configureButton(deleteGameTypeButton, getDeleteButtonHandler());

		selectionModel.addSelectionChangeHandler(getSelectionMenuHandler());
	}

	private void configureButton(Button button, ClickHandler clickHandler) {
		button.addClickHandler(clickHandler);
		button.setStyleName(STYLE_GWT_BUTTON_LOGIN);
		flowPanel.add(button);
	}

	public ClickHandler getSaveButtonHandler() {

		final AdminWidget admin = this;

		return new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				try {

					if (gameNameTextBox.getText().equals(BLANK))
						Window.alert("Please, enter a name for the game type");
					else {

						updateValues();

						RequestHandler handler = new RequestHandler();
						try {
							handler.addGameType(admin, consumerKeyAdmin,
									gameName, buyIn, playerNumber,
									playerTokens, speakTime, smallBlind,
									factorUpdateBlind, updateTimeBlind,
									potType, numberOfWinners);

						} catch (ParametersException ex) {
							Window.alert(ex.getMessage());
						}

					}

				} catch (NumberFormatException er) {
					Window.alert("Please Enter a number");
				}
			}
		};

	}

	public ClickHandler getNewButtonHandler() {

		return new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				cellList.setFocus(false);

				gameNameTextBox.setText(BLANK);
				buyInTextBox.setText(BLANK);
				factorUpdateBlindTextBox.setText(BLANK);
				playerNumberTextBox.setText(BLANK);
				smallBlindTextBox.setText(BLANK);
				speakTimeTextBox.setText(BLANK);
				updateBlindTimeTextBox.setText(BLANK);
				numberOfWinnersTextBox.setText(BLANK);
				potTypeTextBox.setText(BLANK);
				playerTokenTextBox.setText(BLANK);

				cellList.setRowData(listName);
				modifyGameTypeButton.setVisible(false);
				saveGameTypeButton.setVisible(true);
				deleteGameTypeButton.setVisible(false);
			}
		};
	}

	public ClickHandler getModifyButtonHandler() {

		final AdminWidget admin = this;

		return new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {

				cellList.setFocus(false);

				updateValues();

				RequestHandler handler = new RequestHandler();
				try {
					handler.updateGameType(admin, consumerKeyAdmin, gameName,
							buyIn, playerNumber, playerTokens, speakTime,
							smallBlind, factorUpdateBlind, updateTimeBlind,
							potType, numberOfWinners);

				} catch (ParametersException ex) {
					Window.alert(ex.getMessage());
				}

			}
		};
	}

	public ClickHandler getDeleteButtonHandler() {

		final AdminWidget admin = this;

		return new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				String selected = selectionModel.getSelectedObject();
				if (selected != null) {
					for (Parameter param : listParameters) {
						if (param.getNameGame().equals(selected)) {
							if (Window.confirm(DELETE_PARAMETER) == true) {

								updateValues();

								RequestHandler handler = new RequestHandler();
								try {
									handler.deleteGameType(admin,
											consumerKeyAdmin, gameName);

								} catch (ParametersException ex) {
									Window.alert(ex.getMessage());
								}
							}
						}
					}
				}

			}
		};
	}

	public Handler getSelectionMenuHandler() {

		return new SelectionChangeEvent.Handler() {
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				String selected = selectionModel.getSelectedObject();
				if (selected != null) {
					for (Parameter param : listParameters) {
						if (param.getNameGame().equals(selected)) {

							gameNameTextBox.setText(param.getNameGame().trim());

							buyInTextBox.setText(String.valueOf(param
									.getBuyIn()));
							playerNumberTextBox.setText(String.valueOf(param
									.getPlayerNumber()));
							playerTokenTextBox.setText(String.valueOf(param
									.getPlayerTokens()));

							speakTimeTextBox.setText(param.getSpeakTime()
									+ BLANK);

							smallBlindTextBox.setText(String.valueOf(param
									.getSmallBlind()));

							factorUpdateBlindTextBox.setText(String
									.valueOf(param.getFactorUpdateBlind()));

							updateBlindTimeTextBox.setText(String.valueOf(param
									.getUpdateBlindTime()));

							potTypeTextBox.setText(String.valueOf(param
									.getPotType()));

							numberOfWinnersTextBox.setText(String.valueOf(param
									.getNumberOfWinners()));

							// update show buttons and cellList
							saveGameTypeButton.setVisible(false);
							modifyGameTypeButton.setVisible(true);
							cellList.setRowData(0, listName);
							cellList.setFocus(true);
							deleteGameTypeButton.setVisible(true);
							return;
						}

					}
				}
			}
		};
	}

	public void displayExistindGameTypes(String consumerKey) {

		RequestHandler handler = new RequestHandler();
		try {
			consumerKeyAdmin = consumerKey;
			listParameters = handler.getAllGamesTypes(this, consumerKey);
		} catch (ParametersException ex) {
			Window.alert(ex.getMessage());
		}
	}

	private void updateValues() {

		gameName = gameNameTextBox.getText();
		buyIn = Integer.parseInt(buyInTextBox.getText());
		playerNumber = Integer.parseInt(playerNumberTextBox.getText());
		playerTokens = Integer.parseInt(playerTokenTextBox.getText());
		speakTime = Integer.parseInt(speakTimeTextBox.getText());
		smallBlind = Integer.parseInt(smallBlindTextBox.getText());
		factorUpdateBlind = Integer
				.parseInt(factorUpdateBlindTextBox.getText());
		updateTimeBlind = Integer.parseInt(updateBlindTimeTextBox.getText());
		potType = Integer.parseInt(potTypeTextBox.getText());
		numberOfWinners = Integer.parseInt(numberOfWinnersTextBox.getText());
	}

	public void updateSaveGameType(String selected) {

		listParameters.add(new Parameter(gameName, buyIn, playerNumber,
				playerTokens, speakTime, smallBlind, factorUpdateBlind,
				updateTimeBlind, potType, numberOfWinners));

		listName.add(gameName.trim());
		cellList.setRowData(listName);
		Window.alert("The game type " + gameName + " is saved");

		gameNameTextBox.setText(BLANK);
		buyInTextBox.setText(BLANK);
		factorUpdateBlindTextBox.setText(BLANK);
		playerNumberTextBox.setText(BLANK);
		smallBlindTextBox.setText(BLANK);
		speakTimeTextBox.setText(BLANK);
		updateBlindTimeTextBox.setText(BLANK);
		potTypeTextBox.setText(BLANK);
		numberOfWinnersTextBox.setText(BLANK);
		playerTokenTextBox.setText(BLANK);
	}

	public void updateModifyGameType(String selected) {

		for (Parameter param : listParameters) {

			if (param.getNameGame().equals(selected)) {

				listName.remove(selected);
				param.setNameGame(gameNameTextBox.getText());
				param.setPlayerTokens(Integer.parseInt(playerTokenTextBox
						.getText()));
				param.setPlayerNumber(Integer.parseInt(playerNumberTextBox
						.getText()));
				param.setBuyIn(Integer.parseInt(buyInTextBox.getText()));
				param.setSpeakTime(Integer.parseInt(speakTimeTextBox.getText()));
				param.setSmallBlind(Integer.parseInt(smallBlindTextBox
						.getText()));
				param.setFactorUpdateBlind(Integer
						.parseInt(factorUpdateBlindTextBox.getText()));
				param.setUpdateBlindTime(Integer
						.parseInt(updateBlindTimeTextBox.getText()));
				param.setPotType(Integer.parseInt(potTypeTextBox.getText()));
				param.setNumberOfWinners(Integer
						.parseInt(numberOfWinnersTextBox.getText()));

				listName.add(gameNameTextBox.getText().trim());
				cellList.setRowData(0, listName);
				modifyGameTypeButton.setVisible(false);
				saveGameTypeButton.setVisible(true);
				Window.alert("Modifications are saved");

				return;
			}
		}
	}

	public void updateDeleteGameType(String selected) {

		for (Parameter param : listParameters) {

			if (param.getNameGame().equals(selected)) {

				listName.remove(selected);
				listParameters.remove(param);
				cellList.removeStyleName(selected);
				cellList.setRowData(listName);
				deleteGameTypeButton.setVisible(false);
				saveGameTypeButton.setVisible(true);
				modifyGameTypeButton.setVisible(false);
				cellList.setFocus(false);

				return;
			}
		}
	}

	public void updateDisplay() {

		for (Parameter param : listParameters) {

			listName.add(param.getNameGame().trim());
			cellList.setRowData(0, listName);
			cellList.setFocus(true);
		}
	}
}
