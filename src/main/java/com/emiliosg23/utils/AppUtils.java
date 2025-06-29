package com.emiliosg23.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Pane;

public class AppUtils {
	public static void showErrorAlert(String msg){
		Alert alert=new Alert(Alert.AlertType.ERROR,msg);
		alert.setHeaderText(null);
		alert.showAndWait();
	}
	
	public static void showSuccessfulAlert(String msg){
		Alert alert=new Alert(Alert.AlertType.INFORMATION,msg);
		alert.setHeaderText(null);
		alert.showAndWait();
	}
	
	public static boolean showConfirmationAlert(String msg){
		Alert alert=new Alert(Alert.AlertType.CONFIRMATION,msg);
		alert.setHeaderText(null);
		return alert.showAndWait().get()==ButtonType.OK;
	}

	public static Alert showLoadingAlert(String msg){
		Alert alert=new Alert(Alert.AlertType.INFORMATION, msg);
		alert.setHeaderText(null);
		alert.show();
		return alert;
	}

	public static void changeButtonState(Button button, boolean isActive){
		if(isActive)
			changeButtonToRight(button);
		else
			changeButtonToWrong(button);
	}
	
	private static void changeButtonToWrong(Button b){
		b.getStyleClass().remove("right-state");
		b.getStyleClass().add("wrong-state");
	}
	private static void changeButtonToRight(Button b){
		b.getStyleClass().remove("wrong-state");
		b.getStyleClass().add("right-state");
	}

	public static void setSize(Pane pane, double width, double height){
		AppUtils.setWidth(pane, width);
		AppUtils.setHeight(pane, height);
	}
	public static void setWidth(Pane pane, double width){
		pane.setPrefWidth(width);
		pane.setMinWidth(width);
		pane.setMaxWidth(width);
	}
	public static void setHeight(Pane pane, double height){
		pane.setPrefHeight(height);
		pane.setMinHeight(height);
		pane.setMaxHeight(height);
	}
}
