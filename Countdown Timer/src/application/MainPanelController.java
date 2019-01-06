/**
 * Sample Skeleton for 'MainPanel.fxml' Controller Class
 */

package application;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.LinkedList;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.TreeMap;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;

import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.print.PageLayout;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.util.Duration;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

public class MainPanelController implements Initializable{

    @FXML // fx:id="timerPane"
    private AnchorPane timerPane; // Value injected by FXMLLoader
    
    @FXML // fx:id="menuPane"
    private AnchorPane menuPane; // Value injected by FXMLLoader

    @FXML // fx:id="hoursTimer"
    private Text hoursTimer; // Value injected by FXMLLoader

    @FXML // fx:id="minutesTimer"
    private Text minutesTimer; // Value injected by FXMLLoader

    @FXML // fx:id="secondsTimer"
    private Text secondsTimer; // Value injected by FXMLLoader

    @FXML // fx:id="cancelButton"
    private JFXButton cancelButton; // Value injected by FXMLLoader

    @FXML // fx:id="hoursInput"
    private JFXComboBox<Integer> hoursInput; // Value injected by FXMLLoader

    @FXML // fx:id="minutesInput"
    private JFXComboBox<Integer> minutesInput; // Value injected by FXMLLoader

    @FXML // fx:id="secondsInput"
    private JFXComboBox<Integer> secondsInput; // Value injected by FXMLLoader

    @FXML // fx:id="buttonStart"
    private JFXButton buttonStart; // Value injected by FXMLLoader
    
    Map<Integer, String> numberMap;
    Integer currSeconds;
    Thread thrd, musicThread;
    Player player;
    
    @FXML
    void start(ActionEvent event) {
    	currSeconds = hmsToSeconds(hoursInput.getValue(), minutesInput.getValue(), secondsInput.getValue());
    	hoursInput.setValue(0);
    	minutesInput.setValue(0);
    	secondsInput.setValue(0);
    	scrollUp();
    }

    @SuppressWarnings("deprecation")
	@FXML
    void unStart(ActionEvent event) {
    	player.close();
    	thrd.stop();
    	scrollDown();
    }
    
    void startCountDown() {
    	thrd = new Thread(new Runnable() {
			@SuppressWarnings("deprecation")
			public void run() {
				try {
					while (true) {
						//countdown here
						setOutput();
						Thread.sleep(1000);
						if(currSeconds == 0) {
							musicThread = new Thread(new Runnable() {
								
								@Override
								public void run() {
									try {
										FileInputStream stream = new FileInputStream("alarm.mp3");
										player = new Player(stream);
										player.play();
										if(player.isComplete()) {
											scrollDown();
										}
									
									} catch (JavaLayerException | FileNotFoundException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}									
								}
							});
							
							musicThread.start();
							thrd.stop();
						}
						currSeconds -= 1;
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		});
    	thrd.start();
    }
    
    void setOutput() {
    	LinkedList<Integer> currHms = secondsTohms(currSeconds);
    	//System.out.println(currHms.get(0) +"-" +currHms.get(1) +"-" +currHms.get(2));
    	hoursTimer.setText(numberMap.get(currHms.get(0)));
    	minutesTimer.setText(numberMap.get(currHms.get(1)));
    	secondsTimer.setText(numberMap.get(currHms.get(2)));
    }
    
    Integer hmsToSeconds(Integer h, Integer m, Integer s) {
    	Integer hToSeconds = h * 3600;
    	Integer mToSeconds = m * 60;
    	Integer total = hToSeconds + mToSeconds + s;
    	return total;
    }
    
    
    LinkedList<Integer> secondsTohms(Integer currSecond){
    	Integer hours = currSecond / 3600;
    	currSecond = currSecond % 3600;
    	Integer minutes = currSecond / 60;
    	currSecond = currSecond % 60;
    	Integer seconds = currSecond;
    	LinkedList<Integer> answer = new LinkedList<>();
    	answer.add(hours);
    	answer.add(minutes);
    	answer.add(seconds);
    	return answer;
    }
    
    void scrollUp(){
    	TranslateTransition tr1 = new TranslateTransition();
    	tr1.setDuration(Duration.millis(100));
    	tr1.setToX(0);
    	tr1.setToY(-200);
    	tr1.setNode(menuPane);
    	TranslateTransition tr2 = new TranslateTransition();
    	tr2.setDuration(Duration.millis(100));
    	tr2.setFromX(0);
    	tr2.setFromY(200);
    	tr2.setToX(0);
    	tr2.setToY(0);
    	tr2.setNode(timerPane);
    	ParallelTransition pt = new ParallelTransition(tr1, tr2);
    	pt.setOnFinished(e -> {
    		try {
				System.out.println("start countdown....");
				startCountDown();
			} catch (Exception e2) {
				// TODO: handle exception
			}
    	});
    	pt.play();
    }
    
    void scrollDown(){
    	TranslateTransition tr1 = new TranslateTransition();
    	tr1.setDuration(Duration.millis(100));
    	tr1.setToX(0);
    	tr1.setToY(200);
    	tr1.setNode(timerPane);
    	TranslateTransition tr2 = new TranslateTransition();
    	tr2.setDuration(Duration.millis(100));
    	tr2.setFromX(0);
    	tr2.setFromY(-200);
    	tr2.setToX(0);
    	tr2.setToY(0);
    	tr2.setNode(menuPane);
    	ParallelTransition pt = new ParallelTransition(tr1, tr2); 
    	pt.play();
    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		ObservableList<Integer> hoursList = FXCollections.observableArrayList();
		ObservableList<Integer> minutesAndSecondsList = FXCollections.observableArrayList();
		
		//setting hours as 24 and minutes and seconds to 60
		for(int i = 0; i <= 60; i++) {
			if(0 <= i && i <= 24) {
				hoursList.add(new Integer(i));
			}
			minutesAndSecondsList.add(new Integer(i));
		}
		hoursInput.setItems(hoursList);
		hoursInput.setValue(0);
		
		minutesInput.setItems(minutesAndSecondsList);
		minutesInput.setValue(0);
		
		secondsInput.setItems(minutesAndSecondsList);
		secondsInput.setValue(0);
		
		numberMap = new TreeMap<>();
		for(Integer i = 0; i <= 60; i++) {
			if(0 <= i && i <= 9) {
				numberMap.put(i, "0"+i.toString());
			}
			else {
				numberMap.put(i, i.toString());
			}
		}
		
	}

}
