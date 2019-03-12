import java.awt.Color;

public class m2mtester {
	
	private static Thread transmitter;
	
	/**
	 * Simulates the transmission of 4 numbers every 100us.
	 * The first three numbers are pseudo random, and scaled to form a sort of sawtooth waveform.
	 * The fourth number is a 1kHz sine wave.
	 * This is used to check for proper autoscaling of charts, etc.
	 */
	public static void startTransmission() {
		
		transmitter = new Thread(new Runnable() {
			@Override public void run() {
				
				double counter = 0;
				int val1 = 10;
				int val2 = 60;
				int val3 = 10;
				boolean incr1 = true;
				boolean incr2 = false;
				boolean incr3 = true;
				
				while(true) {
					
					try {
						if(incr1) {
							val1++;
							if(val1 == 40) {
								Controller.addPoints(0);
								incr1=false;
							}
						}
						else {
							val1--;
							if(val1 == 10)
								incr1=true;	
						}
						if(incr2) {
							val2++;
							if(val2 == 60) {
								Controller.addPoints(1);
								incr2 = false;
							}
						}
						else {
							val2--;
							if(val2 == 0)
								incr2 = true;	
						}
						
						if(incr3) {
							val3+=2;
							if(val3 == 80) {
								Controller.addPoints(2);
								incr3 = false;
							}
						}
						else {
							val3 -= 2;
							if(val3 == 10)
								incr3 = true;	
						}
						
						//float scalar = ((System.currentTimeMillis() % 30000) - 15000) / 100.0f;
						float[] newSamples = new float[] {
							val1,
							val2,
							val3
						};
						for(int i = 0; i < 10; i++) {
							Controller.getDatasetByIndex(0).add(newSamples[0]);
							Controller.getDatasetByIndex(1).add(newSamples[1]);
							Controller.getDatasetByIndex(2).add(newSamples[2]);
						}
						
						Thread.sleep(100);
						
					} catch(InterruptedException e) {
						
						// stop and end this thread if we get interrupted
						return;
						
					}
				}
				
			}
		});
		transmitter.setPriority(Thread.MAX_PRIORITY);
		transmitter.setName("Test Devices");
		transmitter.start();
		
	}
	
	public static void stopTransmission() {
		
		if(transmitter != null && transmitter.isAlive()) {
			transmitter.interrupt();
			while(transmitter.isAlive()); // wait
		}
		
	}
	
	public static void populateDataStructure() {
		
		Controller.removeAllDatasets();
		
		int location = 0;
		BinaryFieldProcessor processor = BinaryPacket.getBinaryFieldProcessors()[0];
		String name = "";
		Color color = null;
		String unit = "Volts";
		float conversionFactorA = 1;
		float conversionFactorB = 1;
		
		location = 0;
		name = "Tap";
		color = Color.RED;
		Controller.insertDataset(location, processor, name, color, unit, conversionFactorA, conversionFactorB);
		
		location = 1;
		name = "Squeeze";
		color = Color.GREEN;
		Controller.insertDataset(location, processor, name, color, unit, conversionFactorA, conversionFactorB);
		
		location = 2;
		name = "Spin";
		color = Color.BLUE;
		Controller.insertDataset(location, processor, name, color, unit, conversionFactorA, conversionFactorB);
		
		
	}

}
