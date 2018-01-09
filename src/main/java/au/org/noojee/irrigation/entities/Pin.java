package au.org.noojee.irrigation.entities;

import java.time.Duration;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinProvider;

import au.org.noojee.irrigation.types.Amperage;
import au.org.noojee.irrigation.types.EndPointType;
import au.org.noojee.irrigation.types.PinActivationType;
import au.org.noojee.irrigation.types.PinStatus;

@Entity
public class Pin
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    // Name of the device attached to the pin.
	private String deviceName;
	private PinActivationType activationType;
	private EndPointType endPointType;
	
	// We store the gpio pin no. here.
	private int pinNo;
	
	// The amount of current activating this pin causes the device to draw.
	private Amperage startAmps;
	// the amount of current the device draws when this pin is active (post startup spike).
	private Amperage runningAmps;
	// The amount of time the 'startAmps' is drawn once the pin is activiated before
	// the current draw settles to the 'runningAmps'
	private Duration startupInterval;
	
	public void setOn()
	{
		if (activationType == PinActivationType.HIGH_IS_ON)
			setPinHigh();
		else 
			setPinLow();
	}
	
	public void setOff()
	{
		if (activationType == PinActivationType.HIGH_IS_ON)
			setPinLow();
		else 
			setPinHigh();
	}
	
	
	private void setPinHigh()
	{
		GpioPinDigitalOutput gpioPin = this.getPiPin(this.pinNo);
		gpioPin.high();
		
	}

	public void setPiPin(com.pi4j.io.gpio.Pin piPin)
	{
		
		this.pinNo = piPin.getAddress();
	}

	private com.pi4j.io.gpio.GpioPinDigitalOutput getPiPin(int pinNo)
	{
		final GpioController gpio = GpioFactory.getInstance();
		com.pi4j.io.gpio.Pin internalPin = PinProvider.getPinByAddress(pinNo);

		GpioPinDigitalOutput gpioPin = (GpioPinDigitalOutput) gpio.getProvisionedPin(internalPin);
		
		return gpioPin;

		
	}

	public com.pi4j.io.gpio.Pin getPiPin()
	{
		return 	PinProvider.getPinByAddress(pinNo);
	}

	
	private void setPinLow()
	{
		GpioPinDigitalOutput gpioPin = this.getPiPin(this.pinNo);
		gpioPin.low();
		
	}

	public void setDeviceName(String deviceName)
	{
		this.deviceName = deviceName;
	}

	public void setPinActiviationType(PinActivationType pinActiviationType)
	{
		this.activationType = pinActiviationType;
	}

	public void setEndPointType(EndPointType endPointType)
	{
		this.endPointType = endPointType;
	}


	public void setStartAmps(Amperage startAmps)
	{
		this.startAmps = startAmps;
	}

	public void setRunningAmps(Amperage runningAmps)
	{
		this.runningAmps = runningAmps;
	}

	public void setStartupInterval(Duration startupInterval)
	{
		this.startupInterval = startupInterval;
	}
	
	public String getDeviceName()
	{
		return this.deviceName;
	}

	public long getId()
	{
		return id;
	}

	@Override
	public String toString()
	{
		return "Pin [id=" + id + ", deviceName=" + deviceName + ", activationType=" + activationType + ", endPointType="
				+ endPointType + ", pinNo=" + pinNo + "]";
	}

	public EndPointType getEndPointType()
	{
		return this.endPointType;
	}

	public PinActivationType getPinActiviationType()
	{
		return this.activationType;
	}

	public PinStatus getCurrentStatus()
	{
		final GpioController gpio = GpioFactory.getInstance();
		com.pi4j.io.gpio.Pin internalPin = PinProvider.getPinByAddress(pinNo);

		GpioPinDigitalOutput gpioPin = (GpioPinDigitalOutput) gpio.getProvisionedPin(internalPin);

		return PinStatus.getStatus(this, gpioPin.isHigh());
	}

	public int getPinNo()
	{
		return this.pinNo;
	}



	

}