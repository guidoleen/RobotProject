package kleur_en_geluid;

import lejos.robotics.Color;
import lejos.utility.Delay;
import sensoren.TouchSensor;

import java.util.ArrayList;

import basisoefeningen.ColorSensor;
import basisoefeningen.Lcd;
import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.motor.UnregulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;

public class KleurNaarGeluidRGBLauncher 
{

	public static void main(String[] args) 
	{
		/////// Init vars //////
		// kleuren uit de db
//		ArrayList<KleurNaarGeluidRGB> kleurennoten = new KleurNaarGeluidRGBDAO().selecAlleNotenUitDB();
		ArrayList<KleurNaarGeluidRGB> kleurennoten = new KleurNaarGeluidRGBARR().getArrKleurennoten();
		ArrayList<KleurNaarGeluidRGB> muziekstuk = new ArrayList<>();
		
		// Instellen juiste sensor
		ColorSensor colorsensor = new ColorSensor(SensorPort.S1);
		colorsensor.setRGBMode();
		colorsensor.setFloodLight(Color.WHITE);
		Color rgb;
		
		// Stopwatch en TouchSensor en vergelijker
		Stopwatch stopwatch =  new Stopwatch();
		TouchSensor touch = new TouchSensor();
		KleurVergelijk compare = new KleurVergelijk();
		
		// Motoren
		final UnregulatedMotor motorL = new UnregulatedMotor(MotorPort.B);
		final UnregulatedMotor motorR = new UnregulatedMotor(MotorPort.C);
		
		/////// Start het process //////
		Lcd.print(1, "Druk om te beginnen");

		Button.LEDPattern(4); // flash green led and
		Sound.beepSequenceUp(); // make sound when ready.

		Button.waitForAnyPress();
		Button.LEDPattern(0);

		Delay.msDelay(1000);
		
		//kleurs op null zetten voor de start
		Color VorigkleurRgb = new Color(0,0,0); // colorsensor.getColor();
		Color HuidigkleurRgb;

		//Starten opname
        Lcd.clear(2);
		Lcd.print(2, "Rijden maar!");
        Button.waitForAnyPress();
        
        // set motors to 50% power en rijden maar.
        motorL.setPower(20);
        motorR.setPower(20);
        
        /////// Doorloop het process in while loop //////
        do
        {
        	// Sense de huidige kleur...
        	HuidigkleurRgb = colorsensor.getColor();
        	
        	// Bekijk de status van de kleur
        	if( !compare.vergelijkKleur( HuidigkleurRgb.getRed(), VorigkleurRgb.getRed() ) && !compare.vergelijkKleur( HuidigkleurRgb.getGreen(), VorigkleurRgb.getGreen() ) && !compare.vergelijkKleur( HuidigkleurRgb.getBlue(), VorigkleurRgb.getBlue() ) )
        	{
        		for (int i = 0; i < kleurennoten.size(); i++) 
            	{
                	if( compare.vergelijkKleur( VorigkleurRgb.getRed(), kleurennoten.get(i).getRood() ) && compare.vergelijkKleur( VorigkleurRgb.getGreen(), kleurennoten.get(i).getGroen() ) && compare.vergelijkKleur( VorigkleurRgb.getBlue(), kleurennoten.get(i).getBlauw() ) )
                	{
                		muziekstuk.add( new KleurNaarGeluidRGB( kleurennoten.get(i).getFrequentie(), stopwatch.toonDuur()) );
                		stopwatch.setStartTijd( System.currentTimeMillis() );
                		
                		Sound.setVolume( 50 );
                		Sound.playNote(Sound.PIANO, kleurennoten.get(i).getFrequentie(), 5);
                	}
    			}
        		
        		VorigkleurRgb = HuidigkleurRgb;
        	}
        	
        	// Stoppen of verder gaan...
        	Lcd.print(3, "rood=%d groen=%d blauw=%d", 
        			HuidigkleurRgb.getRed(),
        			HuidigkleurRgb.getGreen(),
        			HuidigkleurRgb.getBlue()
        			);
			Lcd.clear(4);
			Lcd.print(4, "Druk op escape om te stoppen");
        }
        while( !touch.getTouchSensorAanUit() && Button.ESCAPE.isUp() );
        
        //Stop motoren
		motorL.stop();
		motorR.stop();
		
		/////// Afspelen van muziek //////
		Lcd.clear(5);
		Lcd.print(5, "druk voor afspelen.");
		Button.waitForAnyPress();
		
		// Loop over de muziekarrayList
		for (int i = 0; i < muziekstuk.size(); i++) 
		{
			Sound.playNote(Sound.PIANO, muziekstuk.get(i).getFrequentie(), (int)muziekstuk.get(i).getDuur());
		}
		
		// free up resources.
		colorsensor.close();
		motorL.close(); 
		motorR.close();
		
		Sound.beepSequence(); // we are done.

		Button.LEDPattern(4);
	}
	
	// Sound.playNote(Sound.PIANO, 587, 5);
	
	// KleurenMeter
	public static void meetKleuren( Color rgb, ColorSensor sensor )
	{
		Button.waitForAnyPress();
		
		System.out.println("Get Color Waarde....");
		while( Button.ESCAPE.isUp() )
		{
			rgb = sensor.getColor();
			
			System.out.println( String.format("Rood: %d, Groen: %d, Blauw: %d", rgb.getRed(),
					rgb.getGreen(),
					rgb.getBlue()
					));
		}
	}
}
