package com.emiliosg23.utils;

import java.net.URL;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;

public class AppUtils {
	private static final String GREEN="rgb(160,255,120)";
  private static final String RED="rgb(255,95,95)";


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

	public static void showLoadingAlert(){
		Alert alert=new Alert(Alert.AlertType.INFORMATION,"Loading...");
		alert.setHeaderText(null);
		alert.show();
	}

	public static void changeButtonState(Button button, boolean isActive){
		if(isActive)
			changeButtonToRight(button);
		else
			changeButtonToWrong(button);
	}

	private static String buttonStyle(String color, String image) {
		URL imageUrl = AppUtils.class.getResource("/imagenes/" + image + ".png");
		return "-fx-background-color:" + color + ";" +
					"-fx-background-image: url('" + imageUrl + "');" +
					"-fx-background-size: cover;"; // mejora visual opcional
	}

	private static String bottonStyle(String color){
			return "-fx-background-color:"+color;
	}
	
	private static void changeButtonToWrong(Button b){
		b.setStyle(bottonStyle(RED));
	}
	private static void changeButtonToRight(Button b){
		b.setStyle(bottonStyle(GREEN));
	}
}
