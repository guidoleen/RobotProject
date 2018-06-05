package kleur_en_geluid;

import java.util.ArrayList;
import java.util.List;
import Kleurherkennen.*;
import basisoefeningen.ColorSensor;
import basisoefeningen.Lcd;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.Color;
import lejos.robotics.ColorDetector;
import lejos.robotics.ColorIdentifier;
import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.port.SensorPort;
import lejos.robotics.Color;
import lejos.utility.Delay;
import lejos.hardware.Brick;
import lejos.hardware.BrickFinder;
import lejos.hardware.lcd.Font;
import lejos.hardware.lcd.TextLCD;
import lejos.hardware.motor.UnregulatedMotor;;

public class Kleur_En_Geluid_Launcher {

	public static void main(String[] args) {
		// Aanmaken sensor en het opstarten riedeltje

				String kleur = "Grijs";
				int arraylengte = 3;

				ColorSensor color = new ColorSensor(SensorPort.S3);

				System.out.println("Kleur van geluid");
				Lcd.print(2, "Druk op een toets");

				Button.LEDPattern(4); // flash green led and
				Sound.beepSequenceUp(); // make sound when ready.

				Button.waitForAnyPress();
				Button.LEDPattern(0);

				// Starten van de sensor

				Delay.msDelay(1000);

				color.setColorIdMode();
				color.setFloodLight(false);

				 // create two motor objects to control the motors.
		        UnregulatedMotor motorA = new UnregulatedMotor(MotorPort.B);
		        UnregulatedMotor motorB = new UnregulatedMotor(MotorPort.C);
		        
		        //Starten opname
		        Lcd.clear(3);
				Lcd.print(3, "Rijden maar!");
		        Button.waitForAnyPress();
		        
		        // set motors to 50% power en rijden maar.
		        motorA.setPower(20);
		        motorB.setPower(20);

		        // wait 2 seconds.
		        //Delay.msDelay(4000);
				
				ArrayList<String> test2 = new ArrayList<String>();
				// kleuren opslaan in ArrayList
				
				
				do {
					if (!kleur.equals(ColorSensor.colorName(color.getColorID()))){
					Lcd.clear(4);
					kleur = ColorSensor.colorName(color.getColorID());
					Lcd.print(4, "Kleur=%s", kleur);
					test2.add(kleur);
					Lcd.clear(5);
					Lcd.print(5, "Druk op escape om te stoppen");
					Geluid(kleur);
					}
					//Delay.msDelay(1000);
				}while (Button.ESCAPE.isUp());
				
				//Stop motoren
				 motorA.stop();
			     motorB.stop();
				
				//afspelen van de ArrayList
//				Lcd.clear(5);
//				Lcd.print(5, "druk voor afspelen.");
//				Button.waitForAnyPress();
//				
//				for(int i = 0; i<test2.size(); i++) {
//					Geluid(test2.get(i));
//					Lcd.clear(3);
//					Lcd.print(3, " %d isKleur=%s", i, test2.get(i));
//					Delay.msDelay(1000);
//				}
					
					
				// free up resources.
				color.close();
				 motorA.close(); 
			     motorB.close();
				
				Sound.beepSequence(); // we are done.

				Button.LEDPattern(4);
				//Button.waitForAnyPress();

			}
		//afspelen van de juiste noten		
	public static void Geluid(String kleur){
		switch (kleur) {
		case "Red": Sound.playTone(880, 40); //A5
			break;
		case "Blue": Sound.playTone(988, 40); //B5
			break;	
		case "Brown": Sound.playTone(523, 40); //C5
			break;
		case "Green": Sound.playTone(587, 40); //D5
		break;
	case "White": Sound.playTone(659, 40); //E5
		break;	
	case "Black": Sound.playTone(698, 40); //F5
		break;
	case "Yellow": Sound.playTone(784, 40); //G5
		break;
		default: Lcd.print(7, "Geen input");
		break;
		}
	}
		}
