package ubicom.htwg.en.healthapp.Controller.BioHarnessBT;

import java.util.*;

public class ReceivedEvent extends EventObject 
{
	private byte[] _bytes;
	public byte[] getBytes() { return _bytes;}
	
	public ReceivedEvent(Object source, byte[] bytes) {
		super(source);
		_bytes = bytes;
	}	
}
